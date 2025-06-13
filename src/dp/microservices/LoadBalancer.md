Kubernetes automatically load balances requests to Services of type ClusterIP, NodePort, or LoadBalancer.  

### 1. Internal Load Balancing with ClusterIP (Between Pods):
Kubernetes automatically does internal load balancing using its Service abstraction.  
* When you create a Service, Kubernetes creates a virtual IP.
* Incoming traffic to this IP is load-balanced across all matching pods.
* Load balancing uses iptables/ipvs (round robin by default).
* 
```
apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:
  selector:
    app: my-app
  ports:
    - protocol: TCP
      port: 80        # Exposed inside the cluster
      targetPort: 8080  # Pod container port
  type: ClusterIP
```
* Requests sent to my-service are automatically load-balanced across all matching pods.

* Load balancing is done via kube-proxy using iptables or IPVS (round-robin).

### 2. NodePort (Basic External Access)

    Opens a port on every node in the cluster.

    Traffic is routed to Pods via the Node IP and port.
```
apiVersion: v1
kind: Service
metadata:
  name: my-nodeport-service
spec:
  selector:
    app: my-app
  ports:
    - port: 80
      targetPort: 8080
      nodePort: 30080
  type: NodePort
```
Use when: you want basic external access without a cloud provider's load balancer.  

### 3. External Load Balancing (To expose apps)
#### 3a. Service Type: LoadBalancer (Common in Cloud)

Use Service type: LoadBalancer to expose apps outside the cluster via a cloud provider's load balancer (like AWS ELB, Azure LB, GCP LB).  

```
User Request (from internet)
        |
        v
[ Cloud LoadBalancer (from Service type=LoadBalancer) ]
        |
        v
[ Ingress Controller or API Gateway Pod ]
        |
        v
[ user-svc (via K8s Service) ]
```

```
apiVersion: v1
kind: Service
metadata:
  name: my-app-service
spec:
  selector:
    app: my-app
  ports:
    - port: 80
      targetPort: 8080
  type: LoadBalancer
```


Prompt chatGPT: example of configure spring Cloud Gateway with load Balancing, kubernetes and spring boot application.  
Client  
  |  
Ingress (optional)  
  |  
Spring Cloud Gateway (Pod)  
  |  
Kubernetes DNS-based Service Discovery  
  |  
Spring Boot Microservices (UserService, PaymentService)  

Use Spring Boot with Spring Cloud Gateway and Kubernetes integration.  

```
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-kubernetes-fabric8</artifactId>
    </dependency>
</dependencies>
```

application.yml for Kubernetes Discovery  
```
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/user/**
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/order/**
    kubernetes:
      discovery:
        enabled: true
        all-namespaces: false
```
lb://service-name means it will be load-balanced using Spring Cloud LoadBalancer and Kubernetes DNS.  


#### Other Services (Example: user-service, order-service)  

Each of these services should:

* Be registered in Kubernetes as a Service

* Have matching Spring Boot application.name

* Be discoverable via DNS: e.g., http://user-service

#### Load Balancing
Spring Cloud Gateway will:

* Use Spring Cloud LoadBalancer + Kubernetes service discovery

* Load balance across all pods behind user-service, order-service, etc.

* Kubernetes manages pod-level round-robin behind each Service

#### How LB Works (Conceptually)  
When you use:  
```
uri: lb://user-service
```
#### Spring Cloud Gateway (via Spring Cloud LoadBalancer) will:**

* Ask Kubernetes (via Spring Cloud Kubernetes) for the list of available endpoints (pods) of user-service.

* Randomly or round-robin pick one pod to forward the request to.

* You do not need a third-party discovery system like Eureka here — Kubernetes provides discovery through DNS + API.

#### Ingress + Ingress Controller (HTTP/S Load Balancing)

    Ingress exposes HTTP/S routes using rules.

    Requires an Ingress Controller like NGINX, Traefik, Istio, etc.

```
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
spec:
  rules:
    - host: example.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: my-lb-service
                port:
                  number: 80
```

#### External Load Balancer AWS NLB (Layer 4) in Kubernetes (Pod to Pod)  
```
apiVersion: v1
kind: Service
metadata:
  name: payment-service
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-type: "nlb"
    service.beta.kubernetes.io/aws-load-balancer-internal: "true"
spec:
  selector:
    app: payment-service
  ports:
    - protocol: TCP
      port: 443
      targetPort: 8443
  type: LoadBalancer
```
**Notes:**  
nlb = AWS Network Load Balancer (L4).  
internal = true makes it not publicly accessible.  
Useful for internal microservices, gRPC, TLS passthrough, etc.  

#### External Load Balancer AWS ALB (Layer 7) via AWS Load Balancer Controller (Pod to Pod)  
```
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: web-ingress
  namespace: default
  annotations:
    kubernetes.io/ingress.class: alb
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/target-type: ip
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTP": 80}]'
    alb.ingress.kubernetes.io/group.name: my-app
spec:
  rules:
    - host: myapp.example.com
      http:
        paths:
          - path: /web
            pathType: Prefix
            backend:
              service:
                name: web-service
                port:
                  number: 80
```

| Feature          | NLB (Layer 4)                         | ALB (Layer 7)                            |
| ---------------- | ------------------------------------- | ---------------------------------------- |
| **K8s Resource** | `Service` with `type: LoadBalancer`   | `Ingress` + AWS Load Balancer Controller |
| **Routing Type** | TCP, UDP                              | HTTP, HTTPS (host/path-based)            |
| **Access**       | Internal or external                  | Public or private                        |
| **Annotations**  | `aws-load-balancer-type: "nlb"`       | `kubernetes.io/ingress.class: alb`, etc. |
| **Use Case**     | Internal services, payment, gRPC, TLS | Public APIs, websites, web apps          |

#### ELB Overview

Elastic Load Balancing (ELB) is the general term for AWS’s load balancing service. It offers three main types:  
| Type    | Full Name                      | Layer       | Protocol               |
| ------- | ------------------------------ | ----------- | ---------------------- |
| **CLB** | Classic Load Balancer (legacy) | Layer 4 & 7 | HTTP, HTTPS, TCP       |
| **ALB** | Application Load Balancer      | Layer 7     | HTTP, HTTPS, WebSocket |
| **NLB** | Network Load Balancer          | Layer 4     | TCP, UDP, TLS          |



