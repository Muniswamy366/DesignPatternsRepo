### Microservices Desgin Patterns

Monolithic is low in Cohesion and Microservices are high in Cohesion.


* ### Circuit Breaker

The circuit breaker works by switching between three states: closed, open, and half-open

#### Closed
In the closed state, the circuit breaker allows requests to flow through and execute the operation as normal.

#### Open
In the open state, the circuit breaker returns a pre-configured fallback value instead of executing the operation.

#### Half-Open
In the half-open state, the circuit breaker allows a limited number of requests to pass through to test if the operation is functioning correctly. If these requests succeed, the circuit breaker returns to the closed state. If they fail, the circuit breaker returns to the open state.

Read retry and fallback: https://reflectoring.io/retry-with-springboot-resilience4j/

* ### Command vs Events
Message is super class for command and events.
Commands are imperative (do something) verbs directed from one source (senders) to one destination, receivers (one-to-one).

Events are nouns, facts about the past (something happened), directed from one source (publishers) to zero or more destinations (subscribers).

Watch: https://hdfcbank.udemy.com/course/microservices-interview-questions-passsing-guarranteed/learn/lecture/28754358#overview

* ### Two Phase commit vs SAGA

https://medium.com/javarevisited/difference-between-saga-pattern-and-2-phase-commit-in-microservices-e1d814e12a5a

SAGA is asynchronous, 2PC is synchronous.

* ### SAGA Pattern – Managing Distributed Transactions
Since ACID transactions don’t scale across services and databases, SAGA is used for managing long-running distributed transactions.

## SAGA Approaches:
# a. Choreography (Event-Based)
- Each service listens for events and reacts accordingly.

- No central controller.

- Lightweight but harder to manage for complex flows.

Example:

* Order Service → emits "Order Created"
↓
* Payment Service → emits "Payment Completed"
↓
* Inventory Service → emits "Inventory Reserved"
↓
* Shipping Service → ships the product
  
# b. Orchestration (Central Coordinator)
- A central orchestrator controls the flow.

- More control, easier to trace, but can become a bottleneck.

Example:

*Orchestrator → call OrderService
             → call PaymentService
             → call InventoryService
             → call ShippingService

* ### Difference between Monolith vs SOA vs Microservices
refer pic

Service Discovery
Side Car Pattern
CQRS

* ### Choose Appropriate Sharding Keys
Sharding is the process of splitting data across multiple nodes (shards) so that each node only holds a portion of the data.

✅ Good Sharding Key Characteristics:
Uniform distribution of data across nodes.

Predictable access patterns (avoid hotspots).

Stable over time (doesn't frequently change).

⚠️ Bad Example:
Using country as a sharding key in a global app – some countries (e.g., USA, India) may get more data → hotspot problem.

✅ Good Example:
Use a user ID or hash of email as the sharding key – more likely to evenly distribute.
