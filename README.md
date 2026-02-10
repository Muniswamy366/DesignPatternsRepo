# Design Patterns Repository

A comprehensive Java implementation repository showcasing various design patterns, SOLID principles, and microservices architecture concepts.

## 📖 Overview

This repository contains implementations of popular design patterns in Java, along with detailed explanations and practical examples. It's structured to help developers understand and apply these patterns in real-world scenarios.

## 📁 Repository Structure

```
src/dp/
├── javaDp/                           # Java Design Pattern Implementations
│   ├── abstractFactory1/             # Abstract Factory Pattern
│   ├── adapter/                      # Adapter Pattern
│   ├── builder/                      # Builder Pattern
│   ├── facade/                       # Facade Pattern
│   │   └── example2/                 # Real-world Facade example (Order system)
│   ├── factory1/                     # Simple Factory Pattern
│   ├── factroy2/                     # Factory Pattern with Loan types
│   ├── singleton/                    # Singleton Pattern
│   └── template/                     # Template Method Pattern
├── microservices/                    # Microservices Architecture Documentation
│   ├── APIGateway.md
│   ├── Architecture.md
│   ├── CAPTheorem.md
│   ├── CircuitBreaker.md
│   ├── CQRS.md
│   ├── DDD.md
│   ├── gRPC.md
│   ├── LoadBalancer.md
│   ├── OAuth2.md
│   ├── SAGA.md
│   ├── ServiceDiscoveryPattern.md
│   ├── ServiceMesh.md
│   └── TLS.md
└── SOLID/                            # SOLID Principles
    ├── DIP.md                        # Dependency Inversion Principle
    ├── ISP/                          # Interface Segregation Principle
    ├── LSP/                          # Liskov Substitution Principle
    ├── OCP/                          # Open/Closed Principle
    ├── SRP/                          # Single Responsibility Principle
    │   ├── begin/                    # Before applying SRP
    │   └── end/                      # After applying SRP
```

## 🎯 Design Patterns Included

### Creational Patterns
- **Singleton Pattern** - Ensures a class has only one instance and provides a global point of access
- **Builder Pattern** - Constructs complex objects step by step
- **Factory Pattern** - Creates objects without specifying their exact classes
- **Abstract Factory Pattern** - Creates families of related objects without specifying concrete classes

### Structural Patterns
- **Adapter Pattern** - Converts interface of one class into another clients expect
- **Facade Pattern** - Provides unified, simplified interface to complex subsystems

### Behavioral Patterns
- **Template Method Pattern** - Defines skeleton of algorithm, letting subclasses override steps

## 🏛️ SOLID Principles

Learn how to write maintainable, scalable code following SOLID principles:

1. **Single Responsibility Principle (SRP)**
   - A class should have only one reason to change
   - Example: User validation, persistence, and controller logic separated

2. **Open/Closed Principle (OCP)**
   - Open for extension, closed for modification
   - Use inheritance and polymorphism

3. **Liskov Substitution Principle (LSP)**
   - Derived classes must be substitutable for base classes

4. **Interface Segregation Principle (ISP)**
   - Clients shouldn't depend on interfaces they don't use

5. **Dependency Inversion Principle (DIP)**
   - Depend on abstractions, not concretions

## 🚀 Microservices Architecture

Comprehensive documentation on microservices patterns and concepts:

- **API Gateway** - Single entry point for microservice requests
- **Service Discovery** - Services dynamically locate each other
- **Circuit Breaker** - Prevent cascading failures
- **SAGA Pattern** - Manage distributed transactions
- **CQRS** - Separate read and write models
- **Domain-Driven Design (DDD)** - Align code with business domains
- **Service Mesh** - Communication layer for microservices
- **Load Balancer** - Distribute traffic across services
- **OAuth2 & TLS** - Security implementation

## 🛠️ Technologies

- **Language**: Java
- **Build System**: Maven/Gradle ready
- **Java Version**: Java 8+

## 📚 Examples

### Singleton Pattern (Eager Initialization)
```java
public class Singleton1 {
    private static final Singleton1 INSTANCE = new Singleton1();
    
    private Singleton1() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Instance already created");
        }
    }
    
    public static Singleton1 getInstance() {
        return INSTANCE;
    }
}
```

### Builder Pattern
```java
Employee employee = new Employee.EmployeeBuilder("John", "ABC Company")
    .hasCar(true)
    .hasBike(false)
    .build();
```

### Facade Pattern (Order System)
```java
OrderFacade orderFacade = new OrderFacade();
orderFacade.placeOrder("P1001", 2499.00);
// Internally handles: inventory check, payment, shipping, notification
```

## 📖 How to Use

### Running the Code

1. Navigate to the pattern directory:
```bash
cd src/dp/javaDp/singleton/
```

2. Compile the Java files:
```bash
javac *.java
```

3. Run the demo:
```bash
java Singleton1Demo
```

### Reading the Documentation

Explore SOLID principles and microservices patterns in their respective directories:
```bash
cat src/dp/SOLID/SRP/README.md
cat src/dp/microservices/CircuitBreaker.md
```

## 📝 Key Concepts

- **Cohesion** - Elements within a module should be closely related
- **Coupling** - Minimize dependencies between modules
- **DRY** - Don't Repeat Yourself
- **KISS** - Keep It Simple, Stupid
- **YAGNI** - You Aren't Gonna Need It

## 🎓 Learning Path

1. Start with SOLID Principles (Foundation)
2. Learn Creational Patterns (Singleton, Builder, Factory)
3. Study Structural Patterns (Adapter, Facade)
4. Explore Behavioral Patterns (Template Method)
5. Understand Microservices Architecture

## 📌 Notes

- Each pattern includes both explanation and code examples
- Real-world examples are provided (e.g., Order management with Facade pattern)
- The repository demonstrates best practices for enterprise Java development

## 🤝 Contributing

Feel free to add more design patterns, improve documentation, or provide feedback!

## 📄 License

This repository is open source and available for educational purposes.

---

**Last Updated**: February 2026
