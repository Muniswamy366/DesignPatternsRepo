# Spring Cloud Gateway with OAuth2, Keycloak & Rate Limiting Guide

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Keycloak Setup](#keycloak-setup)
3. [API Gateway Configuration](#api-gateway-configuration)
4. [Backend Services Configuration](#backend-services-configuration)
5. [JWT Extraction & MDC](#jwt-extraction--mdc)
6. [Centralized Security Library](#centralized-security-library)
7. [Rate Limiting & Throttling](#rate-limiting--throttling)
8. [Testing](#testing)

---

## Architecture Overview

```
Customer → API Gateway (OAuth2 + Keycloak) → Product Service (Port 8081)
                                            → Order Service (Port 8082)
```

**Flow:**
1. Client requests protected resource
2. Gateway redirects to Keycloak login
3. User authenticates with Keycloak
4. Gateway receives JWT token
5. Gateway forwards requests to backend services with token
6. Backend services validate token

---

## Keycloak Setup

### Start Keycloak (Docker)
```bash
docker run -d -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev
```

### Configure Keycloak (http://localhost:8180)
1. **Create Realm:** `ecommerce-realm`
2. **Create Client:** `ecommerce-gateway`
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Valid Redirect URIs: `http://localhost:8080/login/oauth2/code/keycloak`
   - Web Origins: `http://localhost:8080`
3. **Get Client Secret** from Credentials tab
4. **Create Roles:** `CUSTOMER`, `ADMIN`
5. **Create Users:**
   - `john@example.com` → Role: `CUSTOMER`
   - `admin@example.com` → Role: `ADMIN`

---

## API Gateway Configuration

### Project Structure
```
ecommerce-gateway/
├── pom.xml
├── src/main/java/com/ecommerce/gateway/
│   ├── EcommerceGatewayApplication.java
│   └── config/
│       ├── SecurityConfig.java
│       └── RateLimitConfig.java
└── src/main/resources/application.yml
```

### pom.xml
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2023.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### application.yml
```yaml
server:
  port: 8080

spring:
  application:
    name: ecommerce-gateway
    
  security:
    oauth2:
      client:
        provider:
          keycloak:
            issuer-uri: http://localhost:8180/realms/ecommerce-realm
            user-name-attribute: preferred_username
        registration:
          keycloak:
            client-id: ecommerce-gateway
            client-secret: YOUR_CLIENT_SECRET_HERE
            scope: openid,profile,email,roles
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
      
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/ecommerce-realm

  redis:
    host: localhost
    port: 6379

  cloud:
    gateway:
      routes:
        # Product Service - 100 requests/minute per user
        - id: product-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/products/**
          filters:
            - TokenRelay=
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
            
        # Order Service - 50 requests/minute per user
        - id: order-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/orders/**
          filters:
            - TokenRelay=
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 5
                redis-rate-limiter.burstCapacity: 10
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
      
      default-filters:
        - RemoveRequestHeader=Cookie
```

### SecurityConfig.java
```java
package com.ecommerce.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/health").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2Login(oauth2 -> {})
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
```

### RateLimitConfig.java
```java
package com.ecommerce.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> exchange.getPrincipal()
            .cast(JwtAuthenticationToken.class)
            .map(auth -> auth.getToken().getSubject())
            .defaultIfEmpty("anonymous");
    }
}
```

### EcommerceGatewayApplication.java
```java
package com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceGatewayApplication.class, args);
    }
}
```

---

## Backend Services Configuration

### Product Service (Port 8081)

#### pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

#### application.yml
```yaml
server:
  port: 8081

spring:
  application:
    name: product-service
    
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/ecommerce-realm
```

#### SecurityConfig.java
```java
package com.ecommerce.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
```

#### ProductController.java
```java
package com.ecommerce.product.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
}
```

### Order Service (Port 8082)

Same configuration as Product Service, change port to 8082.

---

## JWT Extraction & MDC

### JwtMdcFilter.java
```java
package com.ecommerce.product.filter;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtMdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                MDC.put("userId", jwt.getSubject());
                MDC.put("username", jwt.getClaimAsString("preferred_username"));
                MDC.put("email", jwt.getClaimAsString("email"));
                
                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    List<String> roles = (List<String>) realmAccess.get("roles");
                    MDC.put("roles", String.join(",", roles));
                }
            }
            
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
```

### JwtService.java
```java
package com.ecommerce.product.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    public Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        return null;
    }

    public String getUserId() {
        Jwt jwt = getCurrentJwt();
        return jwt != null ? jwt.getSubject() : null;
    }

    public String getUsername() {
        Jwt jwt = getCurrentJwt();
        return jwt != null ? jwt.getClaimAsString("preferred_username") : null;
    }

    public String getEmail() {
        Jwt jwt = getCurrentJwt();
        return jwt != null ? jwt.getClaimAsString("email") : null;
    }

    public List<String> getRoles() {
        Jwt jwt = getCurrentJwt();
        if (jwt == null) return List.of();
        
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (List<String>) realmAccess.get("roles");
        }
        return List.of();
    }

    public boolean hasRole(String role) {
        return getRoles().contains(role);
    }
}
```

### Usage in Controller
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final JwtService jwtService;

    @GetMapping
    public List<Product> getAllProducts() {
        String userId = jwtService.getUserId();
        List<String> roles = jwtService.getRoles();
        
        log.info("Fetching products"); // [userId=123] [username=john] in logs
        
        return productService.findAll();
    }
}
```

### logback-spring.xml
```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} [userId=%X{userId}] [username=%X{username}] - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

---

## Centralized Security Library

### Create Spring Boot Starter

#### Project Structure
```
ecommerce-security-starter/
├── pom.xml
└── src/main/
    ├── java/com/ecommerce/security/
    │   ├── JwtSecurityAutoConfiguration.java
    │   ├── JwtService.java
    │   ├── JwtRoleConverter.java
    │   └── JwtMdcFilter.java
    └── resources/META-INF/spring/
        └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

#### pom.xml
```xml
<groupId>com.ecommerce</groupId>
<artifactId>ecommerce-security-starter</artifactId>
<version>1.0.0</version>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
</dependencies>
```

#### JwtRoleConverter.java
```java
package com.ecommerce.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.*;
import java.util.stream.Collectors;

public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return List.of();
        
        List<String> roles = (List<String>) realmAccess.get("roles");
        return roles == null ? List.of() : roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }
}
```

#### JwtService.java
```java
package com.ecommerce.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class JwtService {
    
    public Jwt getJwt() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.getPrincipal() instanceof Jwt) ? (Jwt) auth.getPrincipal() : null;
    }
    
    public String getUserId() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getSubject() : null;
    }
    
    public String getUsername() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString("preferred_username") : null;
    }
    
    public List<String> getRoles() {
        Jwt jwt = getJwt();
        if (jwt == null) return List.of();
        
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        return (realmAccess != null && realmAccess.containsKey("roles")) 
            ? (List<String>) realmAccess.get("roles") 
            : List.of();
    }
    
    public boolean hasRole(String role) {
        return getRoles().contains(role);
    }
}
```

#### JwtMdcFilter.java
```java
package com.ecommerce.security;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import java.io.IOException;
import java.util.*;

public class JwtMdcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                MDC.put("userId", jwt.getSubject());
                MDC.put("username", jwt.getClaimAsString("preferred_username"));
                
                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    MDC.put("roles", String.join(",", (List<String>) realmAccess.get("roles")));
                }
            }
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
```

#### JwtSecurityAutoConfiguration.java
```java
package com.ecommerce.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration
@EnableMethodSecurity
@ConditionalOnProperty(prefix = "ecommerce.security", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwtSecurityAutoConfiguration {

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public JwtMdcFilter jwtMdcFilter() {
        return new JwtMdcFilter();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());
        return converter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .addFilterAfter(jwtMdcFilter(), UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
```

#### org.springframework.boot.autoconfigure.AutoConfiguration.imports
```
com.ecommerce.security.JwtSecurityAutoConfiguration
```

### Deploy Starter
```bash
mvn clean install
```

### Use in Services
```xml
<dependency>
    <groupId>com.ecommerce</groupId>
    <artifactId>ecommerce-security-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## Rate Limiting & Throttling

### Redis Rate Limiter (Recommended)

#### Start Redis
```bash
docker run -d -p 6379:6379 redis:latest
```

#### Configuration (Already in Gateway application.yml)
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    
  cloud:
    gateway:
      routes:
        - id: product-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/products/**
          filters:
            - TokenRelay=
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                key-resolver: "#{@userKeyResolver}"
```

### Custom Throttling Filter

#### ThrottleGatewayFilterFactory.java
```java
package com.ecommerce.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ThrottleGatewayFilterFactory extends AbstractGatewayFilterFactory<ThrottleGatewayFilterFactory.Config> {

    private final Map<String, ThrottleInfo> throttleMap = new ConcurrentHashMap<>();

    public ThrottleGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String userId = exchange.getPrincipal()
                .cast(org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken.class)
                .map(auth -> auth.getToken().getSubject())
                .blockOptional()
                .orElse("anonymous");

            ThrottleInfo throttle = throttleMap.computeIfAbsent(userId, 
                k -> new ThrottleInfo(config.getMaxRequests(), config.getWindowSeconds()));

            if (throttle.allowRequest()) {
                return chain.filter(exchange);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config {
        private int maxRequests = 100;
        private int windowSeconds = 60;

        public int getMaxRequests() { return maxRequests; }
        public void setMaxRequests(int maxRequests) { this.maxRequests = maxRequests; }
        public int getWindowSeconds() { return windowSeconds; }
        public void setWindowSeconds(int windowSeconds) { this.windowSeconds = windowSeconds; }
    }

    private static class ThrottleInfo {
        private final int maxRequests;
        private final long windowMillis;
        private long windowStart;
        private int requestCount;

        public ThrottleInfo(int maxRequests, int windowSeconds) {
            this.maxRequests = maxRequests;
            this.windowMillis = windowSeconds * 1000L;
            this.windowStart = System.currentTimeMillis();
            this.requestCount = 0;
        }

        public synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();
            
            if (now - windowStart >= windowMillis) {
                windowStart = now;
                requestCount = 0;
            }

            if (requestCount < maxRequests) {
                requestCount++;
                return true;
            }
            return false;
        }
    }
}
```

### Where Requests Are Stored

#### In-Memory (ConcurrentHashMap)
```
throttleMap = {
    "user-id-123": ThrottleInfo(windowStart=1705320000000, requestCount=45),
    "user-id-456": ThrottleInfo(windowStart=1705320000000, requestCount=12)
}
```
**Location:** Gateway JVM Memory

#### Redis Storage
```
throttle:user-id-123 = "45"  (expires in 60 seconds)
request_rate_limiter.{user-id-123}.tokens = "7"
```

**View in Redis:**
```bash
docker exec -it <redis-container> redis-cli

# View all keys
KEYS throttle:*
KEYS request_rate_limiter.*

# Get user's count
GET throttle:user-id-123

# Monitor real-time
MONITOR
```

---

## Testing

### Start All Services
```bash
# Terminal 1: Keycloak
docker run -d -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev

# Terminal 2: Redis
docker run -d -p 6379:6379 redis:latest

# Terminal 3: Product Service
cd product-service
mvn spring-boot:run

# Terminal 4: Order Service
cd order-service
mvn spring-boot:run

# Terminal 5: Gateway
cd ecommerce-gateway
mvn spring-boot:run
```

### Get Access Token
```bash
TOKEN=$(curl -s -X POST http://localhost:8180/realms/ecommerce-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=ecommerce-gateway" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=john@example.com" \
  -d "password=password" \
  -d "grant_type=password" | jq -r '.access_token')
```

### Test Endpoints
```bash
# View products (CUSTOMER can access)
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/products

# Place order (CUSTOMER can access)
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}' \
  http://localhost:8080/api/orders

# Create product (CUSTOMER cannot - 403)
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Laptop", "price": 999.99}' \
  http://localhost:8080/api/products
```

### Test Rate Limiting
```bash
# Send 15 requests (limit is 10/sec)
for i in {1..15}; do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
    -H "Authorization: Bearer $TOKEN" \
    http://localhost:8080/api/products)
  echo "Request $i: $STATUS"
done

# Expected:
# Request 1-10: 200
# Request 11-15: 429
```

---

## Summary

✅ **Single Sign-On** - One login for all services  
✅ **Centralized Auth** - Gateway handles OAuth2  
✅ **JWT Extraction** - Automatic user context in logs  
✅ **Role-Based Access** - Fine-grained permissions  
✅ **Rate Limiting** - Per-user request throttling  
✅ **Distributed** - Redis for multi-instance support  
✅ **Reusable** - Shared security library

**Production Ready!**
