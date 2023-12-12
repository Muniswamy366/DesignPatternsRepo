### Microservices Desgin Patterns

Monolithic is low in Cohesion and Microservices are high in Cohesion.


### Circuit Breaker

The circuit breaker works by switching between three states: closed, open, and half-open

#### Closed
In the closed state, the circuit breaker allows requests to flow through and execute the operation as normal.

#### Open
In the open state, the circuit breaker returns a pre-configured fallback value instead of executing the operation.

#### Half-Open
In the half-open state, the circuit breaker allows a limited number of requests to pass through to test if the operation is functioning correctly. If these requests succeed, the circuit breaker returns to the closed state. If they fail, the circuit breaker returns to the open state.

Read retry and fallback: https://reflectoring.io/retry-with-springboot-resilience4j/ 