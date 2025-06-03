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

Reverse Proxy:  
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


