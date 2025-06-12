### 1. Decomposition Patterns
Used to break a monolith into smaller microservices.  
🔹 a. Decompose by Business Capability  

    Each service owns one business area.  
    📌 Example: UserService, OrderService, PaymentService.  

🔹 b. Decompose by Subdomain (from Domain-Driven Design)  

    Based on Bounded Contexts.  
    📌 Example: Customer in CRM ≠ Customer in Billing.  

### 2. Integration Patterns
Used to make microservices talk to each other.  
🔹 a. API Gateway

    Entry point for all clients.
    Handles routing, rate limiting, auth, etc.
    📌 Example: Netflix Zuul, Spring Cloud Gateway, Kong.

🔹 b. Service Registry & Discovery

    Dynamically find service instances.
    📌 Example: Eureka, Consul.

🔹 c. Remote Procedure Invocation

    Services call each other via REST, gRPC, or message queues.  

### 3. Data Management Patterns

Used to manage data across services.  
🔹 a. Database per Service

    Each service has its own DB schema.
    📌 Avoids tight coupling.

🔹 b. Shared Database (anti-pattern, use only if needed)

    Multiple services use the same DB.
    ✅ Quick, but ❌ tightly coupled.

🔹 c. Saga Pattern

    Manages distributed transactions using event-driven approach.
    📌 Useful for maintaining consistency across services.

🔹 d. CQRS (Command Query Responsibility Segregation)

    Separate read/write models.
    📌 Speeds up queries, scales better.

4. Observability Patterns

Used to monitor and debug services.  
🔹 a. Log Aggregation

    Collect logs from all services in one place.

    📌 ELK Stack, Loki + Grafana.

🔹 b. Distributed Tracing

    Trace a request across multiple services.

    📌 Zipkin, Jaeger.

🔹 c. Health Check API

    Each service exposes /health endpoint.

    Used by API Gateway or orchestrators (like Kubernetes).

5. Resilience Patterns

Used to make services fault-tolerant.  
🔹 a. Circuit Breaker

    Stop calling a failing service temporarily.

    📌 Resilience4j, Hystrix.

🔹 b. Retry

    Automatically retry failed calls.

🔹 c. Timeout

    Avoid waiting forever for a response.

🔹 d. Bulkhead

    Limit resources to prevent cascading failures.

6. Security Patterns

Used to secure service communication.  
🔹 a. Access Token (JWT)

    Validate user identity on each request.

🔹 b. OAuth2 + OpenID Connect

    Secure user auth (e.g., using Google login).

7. Deployment Patterns

Used to deploy and manage services.  
🔹 a. Service Mesh

    Transparent service communication control.

    📌 Istio, Linkerd.

🔹 b. Sidecar

    Deploy helper containers (e.g., for logging or proxying) alongside services.

