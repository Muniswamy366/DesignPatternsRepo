
### 1. What is ACID?

ACID defines the four properties of a reliable database transaction:

A → Atomicity

C → Consistency

I → Isolation

D → Durability

A transaction must satisfy all four.

### 2. Optimistic vs Pesimistic lock

When multiple users modify same data at same time:

```plain text
User A → updates balance
User B → updates balance
```

Without locking:

Data corruption

Lost updates

Inconsistent state

Locking prevents concurrency problems.

