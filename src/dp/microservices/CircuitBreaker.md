The Circuit Breaker is a resilience pattern used in microservices and distributed systems to prevent failure cascades and improve system stability and fault tolerance.  

### What is a Circuit Breaker?

A circuit breaker is like a fuse in an electrical system: it prevents a service from trying to execute operations likely to fail, allowing time to recover or degrade gracefully.  

It monitors the calls to a remote service:  
* If failures reach a threshold, the circuit "opens"
* It stops further calls for a period of time
* Once the system is considered healthy, it “closes” again

### Circuit Breaker States
| State         | Behavior                                                               |
| ------------- | ---------------------------------------------------------------------- |
| **Closed**    | All requests go through. Failures are tracked.                         |
| **Open**      | Requests are automatically failed/skipped without calling the service. |
| **Half-Open** | A few trial requests are allowed to check if the service is back.      |

### Key Concepts
| Concept               | Meaning                                                                             |
| --------------------- | ----------------------------------------------------------------------------------- |
| **Failure threshold** | Number/percentage of failed requests before opening the circuit                     |
| **Timeout**           | How long the breaker stays open before trying again (half-open)                     |
| **Trial requests**    | Limited test requests in half-open state to determine service health                |
| **Fallback**          | Optional logic (e.g., default value, cache, error message) when the circuit is open |


### Real-life Example (E-Commerce)

* Service A calls PaymentService
* PaymentService is down → 5 consecutive failures
* Circuit opens → all further calls to PaymentService are short-circuited for 30 seconds
* After 30s → a trial call is allowed (half-open)
  - If success → breaker closes
  - If failure → breaker remains open

```
resilience4j:
  circuitbreaker:
    instances:
      productService:
        registerHealthIndicator: true
        failureRateThreshold: 50
        minimumNumberOfCalls: 5
        waitDurationInOpenState: 30s
        permittedNumberOfCallsInHalfOpenState: 3
        slidingWindowSize: 10
```

When to Use Circuit Breaker?

✅ Use when:

    Downstream service is slow or unreliable

    You want to avoid repeated timeouts

    You need a fallback during temporary failures

❌ Avoid when:

    Failure is quick and harmless

    Internal retry mechanisms are better suited


