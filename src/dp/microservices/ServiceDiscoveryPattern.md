Service Discovery is a mechanism by which services can find and communicate with each other without hardcoding network addresses. Instead of static IPs or URLs, services use a discovery mechanism to get the current location (IP, port, etc.) of another service.  
There are two main types of service discovery:  
1. Client-Side Discovery

   The client is responsible for querying the service registry and choosing an instance to call.

    Example: Netflix Eureka + Ribbon.

🔷 Flow:

    Service A (provider) registers itself with Eureka.

    Service B (consumer) queries Eureka.

    Service B gets the list of instances and selects one (e.g., via load balancing).

Pros:

    Less load on server components.

    Flexible client-side logic.

Cons:

    Client must know about the registry.

    Logic duplicated in every client.

2. Server-Side Discovery

    A load balancer or gateway queries the registry and routes the request on behalf of the client.

    Example: Kubernetes Services with kube-proxy, AWS ELB, or Spring Cloud Gateway.

🔷 Flow:

    Service A registers with registry.

    Client sends request to Load Balancer (e.g., API Gateway).

    Load Balancer queries registry and forwards request to Service A.

Pros:

    Simple clients.

    Centralized routing logic.

Cons:

    Load balancer can become a bottleneck or SPOF.

    Extra network hop.

### Benefits of Service Discovery

    Dynamic service location.

    Load balancing support.

    Failover and health checks.

    Automatic scaling.  


