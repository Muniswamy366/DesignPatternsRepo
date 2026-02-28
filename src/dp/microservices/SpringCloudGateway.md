# Spring Cloud Gateway Q Chat

**User:** spring boot application i need to implement spring cloud gateway with oauth2, keycloak, single authentication for my back end service 1 and service 2, explain me in details with code examples, any code changes required in gateway?

**Answer:**

### Architecture Overview
```
Client → Gateway (OAuth2 + Keycloak) → Service 1 (Traffic Light)
                                      → Service 2 (Another Service)
```

### Keycloak Setup

Start Keycloak:
```bash
docker run -d -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev
```

Configure Keycloak (http://localhost:8180):
1. Create Realm: `traffic-system`
2. Create Client: `gateway-client`
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Valid Redirect URIs: `http://localhost:8080/login/oauth2/code/keycloak`
   - Web Origins: `http://localhost:8080`
3. Get Client Secret from Credentials tab
4. Create Users and assign roles

### Gateway Setup

**pom.xml:**
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
</dependencies>
```

**application.yml:**
```yaml
server:
  port: 8080

spring:
  security:
    oauth2:
      client:
        provider:
          keycloak:
            issuer-uri: http://localhost:8180/realms/traffic-system
        registration:
          keycloak:
            client-id: gateway-client
            client-secret: YOUR_CLIENT_SECRET
            scope: openid,profile,email
            authorization-grant-type: authorization_code
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/traffic-system

  cloud:
    gateway:
      routes:
        - id: traffic-light-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/intersections/**
          filters:
            - TokenRelay=
        - id: service-2
          uri: http://localhost:8082
          predicates:
            - Path=/api/service2/**
          filters:
            - TokenRelay=
```

**SecurityConfig.java:**
```java
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2Login(oauth2 -> {})
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
```

### Backend Service Changes

**pom.xml (Both Services):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

**application.yml (Service 1 - Port 8081):**
```yaml
server:
  port: 8081

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/traffic-system
```

**SecurityConfig.java (Both Services):**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
```

## Question 3: Extract JWT Token and Put in MDC

**User:** how to extract jwt token in product service and put in MDC

**Answer:**

### JwtMdcFilter.java
```java
@Component
public class JwtMdcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
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

### Register Filter in SecurityConfig
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtMdcFilter jwtMdcFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
            .addFilterAfter(jwtMdcFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
```

---

## Question 4: Extract JWT and Get Roles

**User:** how to extract jwt token and get roles?

**Answer:**

### Method 1: In Controller
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping
    public List<Product> getAllProducts(@AuthenticationPrincipal Jwt jwt) {
        List<String> roles = extractRoles(jwt);
        System.out.println("User: " + jwt.getSubject());
        System.out.println("Roles: " + roles);
        return productService.findAll();
    }
    
    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (List<String>) realmAccess.get("roles");
        }
        return List.of();
    }
}
```

### Method 2: Custom JWT Converter (Recommended)
```java
public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return List.of();
        }
        
        List<String> roles = (List<String>) realmAccess.get("roles");
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }
}
```

### SecurityConfig with Converter
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        )
        .csrf(csrf -> csrf.disable());
    
    return http.build();
}

@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());
    return converter;
}
```

### 5. Alternative: Interceptor Approach

```java
package com.ecommerce.product.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtMdcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            MDC.put("userId", jwt.getSubject());
            MDC.put("username", jwt.getClaimAsString("preferred_username"));
            MDC.put("email", jwt.getClaimAsString("email"));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.clear();
    }
}

```
### Register Interceptor:
```java
package com.ecommerce.product.config;

import com.ecommerce.product.interceptor.JwtMdcInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtMdcInterceptor jwtMdcInterceptor;

    public WebConfig(JwtMdcInterceptor jwtMdcInterceptor) {
        this.jwtMdcInterceptor = jwtMdcInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtMdcInterceptor);
    }
}

```
---

## Question 7: Rate Limiting in Gateway

**User:** how to implement rate limit in current api gateway

Rate limit can be done per user, ip, 

**Answer:**

### Redis Rate Limiter (Recommended)

**Add Dependency:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
```

**Start Redis:**
```bash
docker run -d -p 6379:6379 redis:latest
```

**application.yml:**
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

**RateLimitConfig.java:**
```java
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

---

## Question 8: User-Based Rate Limit

**User:** user based rate limit

**Answer:**

### User Key Resolver
```java
@Configuration
public class RateLimitConfig {
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> exchange.getPrincipal()
            .cast(JwtAuthenticationToken.class)
            .map(auth -> auth.getToken().getSubject()) // Extract userId from JWT
            .defaultIfEmpty("anonymous");
    }
}


package com.ecommerce.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    // Rate limit by user ID
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> exchange.getPrincipal()
            .cast(JwtAuthenticationToken.class)
            .map(auth -> auth.getToken().getSubject())
            .defaultIfEmpty("anonymous");
    }
    
    // Alternative: Rate limit by IP
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress()
        );
    }
    
    // Alternative: Rate limit by API key
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.justOrEmpty(
            exchange.getRequest()
                .getHeaders()
                .getFirst("X-API-Key")
        ).defaultIfEmpty("no-api-key");
    }
}

```

### Testing
```bash
# Get token for user1
TOKEN1=$(curl -s -X POST http://localhost:8180/realms/ecommerce-realm/protocol/openid-connect/token \
  -d "client_id=ecommerce-gateway" \
  -d "client_secret=YOUR_SECRET" \
  -d "username=user1@example.com" \
  -d "password=password" \
  -d "grant_type=password" | jq -r '.access_token')

# Test user1 - should allow 10 requests/second
for i in {1..15}; do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
    -H "Authorization: Bearer $TOKEN1" \
    http://localhost:8080/api/products)
  echo "Request $i: $STATUS"
done
```

**Expected Output:**
```
Request 1-10: 200
Request 11-15: 429 (Rate limit exceeded)
```

---

## Question 9: Throttling Example

**User:** give me throttling example in gateway

**Answer:**

### Custom Throttling Filter
```java
package com.ecommerce.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class RedisThrottleGatewayFilterFactory extends AbstractGatewayFilterFactory<RedisThrottleGatewayFilterFactory.Config> {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisThrottleGatewayFilterFactory(ReactiveRedisTemplate<String, String> redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getPrincipal()
            .cast(org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken.class)
            .map(auth -> auth.getToken().getSubject())
            .defaultIfEmpty("anonymous")
            .flatMap(userId -> {
                String key = "throttle:" + userId;
                
                return redisTemplate.opsForValue()
                    .increment(key)
                    .flatMap(count -> {
                        if (count == 1) {
                            return redisTemplate.expire(key, Duration.ofSeconds(config.getWindowSeconds()))
                                .thenReturn(count);
                        }
                        return Mono.just(count);
                    })
                    .flatMap(count -> {
                        long remaining = Math.max(0, config.getMaxRequests() - count);
                        
                        exchange.getResponse().getHeaders().add("X-RateLimit-Limit", String.valueOf(config.getMaxRequests()));
                        exchange.getResponse().getHeaders().add("X-RateLimit-Remaining", String.valueOf(remaining));
                        
                        if (count <= config.getMaxRequests()) {
                            return chain.filter(exchange);
                        } else {
                            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                            exchange.getResponse().getHeaders().add("Retry-After", String.valueOf(config.getWindowSeconds()));
                            return exchange.getResponse().setComplete();
                        }
                    });
            });
    }

    public static class Config {
        private int maxRequests = 100;
        private int windowSeconds = 60;

        public int getMaxRequests() { return maxRequests; }
        public void setMaxRequests(int maxRequests) { this.maxRequests = maxRequests; }
        public int getWindowSeconds() { return windowSeconds; }
        public void setWindowSeconds(int windowSeconds) { this.windowSeconds = windowSeconds; }
    }
}
```

### Usage in application.yml
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: product-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/products/**
          filters:
            - name: Throttle
              args:
                maxRequests: 100
                windowSeconds: 60
```

---

## Redis stores: throttle:user-id-123 = "45" (expires in 60 seconds)
