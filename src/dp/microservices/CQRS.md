### CQRS
CQRS stands for Command Query Responsibility Segregation. It’s a pattern that separates:  
Commands (write/update operations)  
Queries (read operations)

https://www.youtube.com/watch?v=fzGZPf0FMao  
https://github.com/Java-Techie-jt/cqrs-design-pattern  

🎯 Goal:
- To optimize ``performance, scalability``, and security by handling reads and writes separately.

#### When to Use  

1. 🧠 Separation of Concerns
Writes (Commands): Focus on business logic and validations.

Reads (Queries): Focus on displaying data efficiently (e.g., joined DTOs).

This makes your code cleaner, simpler, and easier to maintain.

2. ⚡ Performance Optimization
Read models can be denormalized and optimized for fast queries.

Write models can focus on consistency and validation.

You can use different databases (e.g., SQL for writes, NoSQL for reads).

3. 📈 Independent Scaling
Read traffic is usually much higher than write traffic.

With CQRS, you can scale read and write services separately.  

✅ Use CQRS when:

- High read/write load

- Complex business logic on commands

- Need different scaling for read and write

- Event sourcing or distributed systems involved

🚫 Avoid CQRS when:

- Simple CRUD system

- Low scalability needs

- Team isn’t experienced in handling eventual consistency
