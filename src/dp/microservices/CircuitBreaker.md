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

### When to Use Circuit Breaker?

✅ Use when:

    Downstream service is slow or unreliable

    You want to avoid repeated timeouts

    You need a fallback during temporary failures

❌ Avoid when:

    Failure is quick and harmless

    Internal retry mechanisms are better suited

Detailed Explanation of Each Property
🔹 registerHealthIndicator: true

    Purpose: Integrates the circuit breaker status with Spring Boot Actuator.

    Effect: You can check the health of the productService circuit breaker at:

    /actuator/health

    It shows if the circuit is OPEN, CLOSED, or HALF-OPEN.

🔹 failureRateThreshold: 50

    Purpose: Defines the percentage of failed calls (out of total recorded) to trigger the breaker to open.

    Example: If 50% (or more) of the calls fail within the sliding window, the circuit opens.

🔹 minimumNumberOfCalls: 5

    Purpose: Specifies the minimum number of calls that must be recorded before the failure rate is calculated.

    Example: If fewer than 5 calls were made, the circuit won’t evaluate the failure rate.

🔹 waitDurationInOpenState: 30s

    Purpose: Time the circuit breaker remains OPEN before transitioning to HALF-OPEN.

    Effect: During these 30 seconds, all incoming calls are immediately rejected (fail-fast).

    After 30 seconds, it enters HALF-OPEN state and allows trial requests.

🔹 permittedNumberOfCallsInHalfOpenState: 3

    Purpose: Number of trial calls allowed when the circuit is in HALF-OPEN state.

    Effect:

        If these 3 calls succeed → circuit goes to CLOSED (healthy).

        If any fail → circuit goes back to OPEN (still broken).

🔹 slidingWindowSize: 10

    Purpose: Defines the number of recent calls to track when calculating failure rate.

    Example: Out of last 10 calls, if 5 or more fail → failure rate = 50% → triggers circuit to OPEN.

    The window works like a rolling log: only the most recent 10 calls are evaluated.

🔁 Example Scenario

- 10 API calls made
- 6 of them fail → failure rate = 60%
- Since 60% > 50% (failureRateThreshold), circuit opens
- Next 30 seconds → all calls fail immediately (open state)
- After 30s → allows 3 trial calls
  - If all 3 succeed → closes the circuit
  - If even 1 fails → opens the circuit again
