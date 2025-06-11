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

