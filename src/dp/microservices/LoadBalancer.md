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


