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

