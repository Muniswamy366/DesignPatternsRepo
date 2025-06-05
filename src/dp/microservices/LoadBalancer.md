Prompt chatGPT: example of configure spring Cloud Gateway with load Balancing, kubernetes and spring boot application
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
#### 1. Spring Cloud Gateway (via Spring Cloud LoadBalancer) will:**

* Ask Kubernetes (via Spring Cloud Kubernetes) for the list of available endpoints (pods) of user-service.

* Randomly or round-robin pick one pod to forward the request to.

* You do not need a third-party discovery system like Eureka here — Kubernetes provides discovery through DNS + API.

#### 2. Basic LoadBalancer Service (for Web App)
This is a standard way to expose a service via cloud provider's default L4/L7 load balancer, without using ALB Controller or AWS-specific annotations.
```
apiVersion: v1
kind: Service
metadata:
  name: internal-service
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-internal: "true"
spec:
  type: LoadBalancer
  selector:
    app: internal
  ports:
    - port: 443
      targetPort: 8443
```

#### 3. Configure AWS NLB (Layer 4) in Kubernetes (Pod to Pod)  
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

#### 4. Configure AWS ALB (Layer 7) via AWS Load Balancer Controller (Pod to Pod)  
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




