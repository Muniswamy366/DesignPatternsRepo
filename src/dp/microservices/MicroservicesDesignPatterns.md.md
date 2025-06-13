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


## Another Prompt result: 
✅ 1. API Gateway Pattern

    Purpose: Entry point for all clients. Routes requests to appropriate microservices.

    Benefits: Aggregates responses, handles cross-cutting concerns (auth, rate limiting, logging).

    Use case: Mobile/web clients requiring different response formats.

✅ 2. Circuit Breaker Pattern

    Purpose: Prevent cascading failures by detecting and stopping calls to failed services.

    Library: Resilience4j, Netflix Hystrix (deprecated).

    Use case: Service A depends on flaky Service B; fail fast to maintain stability.

✅ 3. Service Discovery Pattern

    Purpose: Dynamically locate other services in the network.

    Types:

        Client-side discovery (e.g., Netflix Eureka + Ribbon)

        Server-side discovery (e.g., AWS ALB, Kubernetes)

    Use case: Scaling services dynamically in Kubernetes or cloud.

✅ 4. Database per Service Pattern

    Purpose: Each microservice has its own database schema.

    Benefits: Loose coupling, service autonomy.

    Challenges: Cross-service queries require event-driven communication.

✅ 5. Saga Pattern (for distributed transactions)

    Purpose: Manage data consistency across multiple microservices using a series of local transactions.

    Types:

        Choreography (event-based)

        Orchestration (central coordinator)

    Use case: E-commerce checkout process involving payment, inventory, and shipping services.

✅ 6. CQRS (Command Query Responsibility Segregation)

    Purpose: Separate read and write models to optimize performance and scalability.

    Use case: High-read systems like dashboards or reports.

✅ 7. Event Sourcing Pattern

    Purpose: Persist state changes as a sequence of events rather than current state.

    Use case: Audit trails, undo functionality, and time-travel in apps.

✅ 8. Strangler Fig Pattern

    Purpose: Gradually replace a legacy system by routing traffic to a new system module-by-module.

    Use case: Migrating a monolith to microservices without downtime.

✅ 9. Bulkhead Pattern

    Purpose: Isolate resources (threads, DB pools) so failure in one doesn’t impact others.

    Use case: Avoid full system outage if one service fails under load.

✅ 10. Retry Pattern

    Purpose: Automatically retry failed operations with a backoff strategy.

    Use case: Network hiccups, temporary service unavailability.

✅ 11. Sidecar Pattern

    Purpose: Deploy supporting components like logging, proxy, or service mesh alongside a microservice.

    Use case: Service Mesh (e.g., Istio with Envoy Proxy).

✅ 12. Backend for Frontend (BFF) Pattern

    Purpose: Create a custom backend tailored for each frontend client (web, mobile).

    Use case: Mobile apps needing optimized data to reduce network usage.
