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

3. Data Management Patterns

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

