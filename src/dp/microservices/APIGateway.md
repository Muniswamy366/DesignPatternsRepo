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


