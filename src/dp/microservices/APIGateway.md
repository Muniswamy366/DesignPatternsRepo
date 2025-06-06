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
