### CQRS
CQRS stands for Command Query Responsibility Segregation. It’s a pattern that separates:  
Commands (write/update operations)  
Queries (read operations)

🎯 Goal:
- To optimize performance, scalability, and security by handling reads and writes separately.

#### When to Use  
✅ Use CQRS when:

- High read/write load

- Complex business logic on commands

- Need different scaling for read and write

- Event sourcing or distributed systems involved

🚫 Avoid CQRS when:

- Simple CRUD system

- Low scalability needs

- Team isn’t experienced in handling eventual consistency
