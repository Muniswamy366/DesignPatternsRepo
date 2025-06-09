**What it does:**
A single entry point for all clients to interact with backend microservices.

**Why use it:**

Decouples clients from services.

Centralized routing, authentication, rate limiting.

**Tools:**
Kong, Spring Cloud Gateway, Zuul, NGINX, Ambassador.  

**An API Gateway is a server that:**

* Acts as a reverse proxy to route requests to multiple microservices

* Handles authentication, authorization, rate limiting, load balancing, caching, and request/response transformation

#### Why Use an API Gateway?
In a microservices architecture, each service is independently deployable and might expose its own API. Without a gateway:  

* Clients must call multiple services directly.  
* Each service must handle cross-cutting concerns like authentication, rate limiting, etc.  

**Reverse Proxy:**  
A reverse proxy is a server that sits between clients and backend servers (like web or application servers). It forwards client requests to the appropriate backend server, then returns the server’s response back to the client.  

### Reverse Proxy vs Forward Proxy

| Feature           | Reverse Proxy                      | Forward Proxy                         |  
| ----------------- | ---------------------------------- | ------------------------------------- |  
| Acts on behalf of | Server                             | Client                                |  
| Used by           | Server-side (e.g., load balancing) | Client-side (e.g., content filtering) |  
| Hides             | Backend servers                    | Clients                               |  

#### How They Work
🔹 Forward Proxy (Client-Side Proxy)  
Client → Forward Proxy → Internet  

Example: Your office network blocks YouTube; you configure a proxy to access it.  

🔹 Reverse Proxy (Server-Side Proxy)  
Client → Reverse Proxy → Web Servers  

Example: A website uses NGINX as a reverse proxy to route traffic to various microservices.  

#### Drawbacks
Single Point of Failure: If not highly available.  

Latency: Adds an extra network hop.  

Complexity: Requires careful design and management.  

#### Key Responsibilities:  

* Routing: Forward requests to the right microservice.  
* Authentication & Authorization: Verify user identity and access.  
* Rate Limiting & Throttling  
* Request/Response Transformation  
* Caching  
* Monitoring / Logging  
* Service Discovery (optional)

#### Difference Between Rate Limiting and Throttling  
| Feature          | **Rate Limiting**                                    | **Throttling**                                    |  
| ---------------- | ---------------------------------------------------- | ------------------------------------------------- |  
| **Purpose**      | Enforce a maximum number of requests per time window | Control traffic spikes and smooth out usage       |  
| **Behavior**     | Rejects requests that exceed the limit               | Delays or queues excess requests                  |  
| **When Applied** | Before requests hit the backend                      | During or after traffic exceeds a threshold       |  
| **Error Code**   | Usually returns `429 Too Many Requests`              | May delay or return `429`, or retry automatically |  
| **Example**      | Max 1000 requests/user/day                           | Allow burst of 100 requests/sec, then slow down   |  


#### Performance Benchmark Summary
| Gateway                  | Requests per Second (RPS) | P99 Latency (ms) | Notes                                                                        |                        |
| ------------------------ | ------------------------- | ---------------- | ---------------------------------------------------------------------------- | ---------------------- |
| **Kong Gateway 3.10.x**  | \~127,000 (no plugins)    | 7.1              | High throughput with low latency; performance decreases with added plugins.  |                        |
| **Spring Cloud Gateway** | \~32,000                  | \~6.6            | Moderate performance; suitable for Spring-based applications.                |                        |
| **NGINX**                | \~30,000                  | <30              | Consistent low latency; performance may vary with complex configurations.    |  |  

Observations: Kong maintains high throughput and low latency, even with added plugins, making it suitable for high-performance applications.  
Observations: While SCG offers decent performance, it may not match the throughput of Kong or NGINX, especially under high load.  
Observations: NGINX provides stable and low-latency performance, making it a reliable choice for scenarios where consistent response times are critical.  

#### Final Tip  
In real-world systems, teams often combine:

* Spring Cloud Gateway for internal, secure traffic within Spring ecosystem

* Kong or NGINX as the external, hardened API edge gateway

| Feature                      | **Spring Cloud Gateway**     | **Kong Gateway**                        | **NGINX** (open-source/pro)         |
| ---------------------------- | ---------------------------- | --------------------------------------- | ----------------------------------- |
| **Language**                 | Java (Spring Boot)           | C / Lua (native)                        | C                                   |
| **Deployment**               | Embedded in Spring Boot apps | Standalone / Docker / K8s               | Standalone / Docker / K8s           |
| **Best For**                 | Java/Spring microservices    | Language-agnostic, high-scale APIs      | Static reverse proxy/load balancer  |
| **Performance**              | Moderate (JVM overhead)      | High (native binary, optimized)         | Very High (lightweight)             |
| **Plugin Ecosystem**         | Basic via Spring filters     | Very rich (JWT, OAuth, ACL, gRPC, etc.) | Limited in OSS; rich in NGINX Plus  |
| **Authentication Support**   | Spring Security integration  | Built-in JWT/OAuth2 plugins             | Custom or NGINX Plus                |
| **Rate Limiting**            | Redis or custom filters      | Native plugin                           | Requires custom config or Plus      |
| **gRPC/WebSocket Support**   | Partial                      | Full                                    | Limited (better in NGINX Plus)      |
| **Admin Dashboard**          | ❌ Manual or custom           | ✅ Kong Manager (UI) in Enterprise       | ❌ (NGINX Plus has dashboard)        |
| **Service Discovery**        | ✅ (Eureka, Consul)           | ✅ DNS or via Kong Mesh                  | ❌ (manual or 3rd party like Consul) |
| **Cloud/Kubernetes Support** | Spring Cloud K8s             | ✅ Kong Ingress Controller for K8s       | ✅ NGINX Ingress (very popular)      |
| **Open Source**              | ✅ Fully open source          | ✅ + Enterprise edition                  | ✅ OSS + NGINX Plus (commercial)     |
| **Custom Extensions**        | Write filters in Java        | Write plugins in Lua or JS (EE)         | Write custom modules in C (harder)  |


**Prompt:** provide me example of kubernetes with service discovery and load balancer with multiple regions   

### Core Features of API Gateway
1. Request Routing  
Routes incoming requests to appropriate backend services based on paths, headers, or other criteria.

```
spring:
  cloud:
    gateway:
      routes:
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/api/products/**
```

2. Load Balancing  
Distributes traffic across multiple instances of backend services.

```
# Using the lb:// prefix enables client-side load balancing
spring:
  cloud:
    gateway:
      routes:
        - id: order-service
          uri: lb://order-service  # Load balances across instances
          predicates:
            - Path=/api/orders/**
```

3. Authentication & Authorization  
Centralizes security concerns by integrating with OAuth2/OIDC providers.

```
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .oauth2Login(Customizer.withDefaults())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/public/**").permitAll()
                .anyExchange().authenticated())
            .build();
    }
}
```

```
server:
  port: 8081

spring:
  application:
    name: gateway-service

  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8082
          predicates:
            - Path=/users/**
          filters:
            - RemoveRequestHeader=Cookie
            - TokenRelay

  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: gateway-client
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          keycloak:
            issuer-uri: http://localhost:8080/realms/demo

      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/demo
```

4. Request/Response Transformation  
Modifies requests or responses as they pass through the gateway.

```
filters:
  - name: AddRequestHeader
    args:
      name: X-Gateway-Header
      value: gateway
```

```
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - AddRequestHeader=X-Request-Source, api-gateway
            - RemoveRequestHeader=X-Internal-Header
            - AddResponseHeader=X-Response-Time, ${now}
```

5. Rate Limiting  
Protects backend services from being overwhelmed by too many requests.

```
spring:
  cloud:
    gateway:
      routes:
        - id: payment-service
          uri: lb://payment-service
          predicates:
            - Path=/api/payments/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
```

6. Circuit Breaking  
Prevents cascading failures by detecting and isolating failing services.

```
spring:
  cloud:
    gateway:
      routes:
        - id: inventory-service
          uri: lb://inventory-service
          predicates:
            - Path=/api/inventory/**
          filters:
            - name: CircuitBreaker
              args:
                name: inventoryCircuitBreaker
                fallbackUri: forward:/fallback/inventory
```

7. Request Logging & Monitoring  
Provides visibility into API traffic and performance.

```
@Configuration
public class GatewayConfig {
    @Bean
    public GlobalFilter loggingFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("Path: {}, Method: {}", request.getPath(), request.getMethod());
            
            return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    log.info("Status: {}", response.getStatusCode());
                }));
        };
    }
}
```

8. Token Relay  
Forwards authentication tokens to downstream services.

```
spring:
  cloud:
    gateway:
      routes:
        - id: account-service
          uri: lb://account-service
          predicates:
            - Path=/api/accounts/**
          filters:
            - TokenRelay=
```

9. Request Validation  
Validates incoming requests before they reach backend services.

```
@Component
public class RequestValidator extends AbstractGatewayFilterFactory<RequestValidator.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Validate request parameters
            if (!request.getQueryParams().containsKey("apiKey")) {
                return Mono.error(new InvalidRequestException("Missing API key"));
            }
            
            return chain.filter(exchange);
        };
    }
    
    public static class Config {}
}
```

10. Cross-Origin Resource Sharing (CORS)  
Handles CORS headers for web applications.

```
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "https://example.com"
            allowedMethods:
              - GET
              - POST
            allowedHeaders: "*"
            maxAge: 3600
```

11. SSL Termination  
Handles HTTPS connections and forwards requests as HTTP to internal services.

```
server:
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
    key-alias: api-gateway
  port: 8443
```

12. Request Aggregation  
Combines results from multiple backend services into a single response.

```
@RestController
public class AggregationController {
    private final WebClient webClient;
    
    @GetMapping("/api/dashboard")
    public Mono<Map<String, Object>> getDashboard() {
        Mono<UserData> userData = webClient.get()
            .uri("lb://user-service/api/users/current")
            .retrieve()
            .bodyToMono(UserData.class);
            
        Mono<List<Order>> orders = webClient.get()
            .uri("lb://order-service/api/orders/recent")
            .retrieve()
            .bodyToFlux(Order.class)
            .collectList();
            
        return Mono.zip(userData, orders)
            .map(tuple -> {
                Map<String, Object> result = new HashMap<>();
                result.put("user", tuple.getT1());
                result.put("orders", tuple.getT2());
                return result;
            });
    }
}
```

13. Caching  
Caches responses to improve performance and reduce backend load.

```
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("apiResponses");
    }
}

@Component
public class CachingFilter extends AbstractGatewayFilterFactory<CachingFilter.Config> {
    @Autowired
    private CacheManager cacheManager;
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String cacheKey = exchange.getRequest().getPath().toString();
            Cache cache = cacheManager.getCache("apiResponses");
            
            Object cachedResponse = cache.get(cacheKey);
            if (cachedResponse != null) {
                return Mono.just(cachedResponse);
            }
            
            return chain.filter(exchange)
                .doOnSuccess(response -> cache.put(cacheKey, response));
        };
    }
    
    public static class Config {}
}
```

14. API Versioning  
Handles different versions of APIs through routing.

```
spring:
  cloud:
    gateway:
      routes:
        - id: product-service-v1
          uri: lb://product-service-v1
          predicates:
            - Path=/api/v1/products/**
        - id: product-service-v2
          uri: lb://product-service-v2
          predicates:
            - Path=/api/v2/products/**
```

15. Retry Mechanism  
Automatically retries failed requests to improve resilience.

```
spring:
  cloud:
    gateway:
      routes:
        - id: notification-service
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**
          filters:
            - name: Retry
              args:
                retries: 3
                statuses: BAD_GATEWAY
                methods: GET,POST
                backoff:
                  firstBackoff: 10ms
                  maxBackoff: 50ms
                  factor: 2
                  basedOnPreviousValue: false
```

![image](https://github.com/user-attachments/assets/152037d0-313a-4424-a8b6-f95750625d47)  

![image](https://github.com/user-attachments/assets/d74aaf4d-bb71-46f2-9a52-443e7979b75f)  

#### Is API gateway is single point of failure?
Yes, API Gateway can be a single point of failure (SPOF) — but only if it’s not properly designed and deployed.  

#### Why API Gateway Can Be a SPOF
If you deploy your API Gateway:
* As a single instance
* On a single VM/pod
* Without load balancing or failover
Then, if that instance crashes, all traffic is blocked, because clients rely on it to reach the backend services.

#### How to Avoid SPOF in API Gateway
1. High Availability (HA) Deployment
* Run multiple replicas of the gateway (e.g., Spring Cloud Gateway or Kong) in a Kubernetes cluster or load-balanced environment.  
* Example in Kubernetes:
```
replicas: 3
```

2. Load Balancer in Front
Use a cloud load balancer (like AWS ALB/NLB, GCP Load Balancer) or Kubernetes service with type LoadBalancer or Ingress to distribute traffic across gateway replicas.  

3. Health Checks
Configure liveness/readiness probes so unhealthy gateway pods are not used.  

4. Auto-scaling
Use Horizontal Pod Autoscaler (HPA) in Kubernetes to scale gateway replicas based on CPU or latency.  

5. Distributed Deployments (Multi-region)
For global systems, deploy API gateways in multiple regions behind a geo-aware load balancer (e.g., AWS Route 53 with health checks).  




