
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


Optimistic Locking

Assume conflicts are rare. Allow concurrent updates, but detect conflict before commit.

No lock is placed while reading.

Conflict is detected during update.

🧠 How It Works

Uses a version field.
```sql
id | balance | version
1  | 100     | 1
Transaction 1
```

Reads:

balance=100, version=1

Updates:

UPDATE account
SET balance = 120, version = 2
WHERE id = 1 AND version = 1;
Transaction 2

Also read version=1.

When it tries update:

UPDATE account
SET balance = 150, version = 2
WHERE id = 1 AND version = 1;

But version is now 2.

Update affects 0 rows → conflict detected.

Transaction fails.

🟢 Spring Boot Example (JPA)
@Entity
public class Account {

    @Id
    private Long id;

    private double balance;

    @Version
    private int version;
}

Spring automatically:

Adds version check

Throws OptimisticLockException on conflict

✅ Pros

High concurrency

No DB-level lock

Better performance

Good for read-heavy systems

❌ Cons

Retry logic required

Not good for high-conflict data

May fail frequently under heavy writes

🔴 2️⃣ Pessimistic Locking

Assume conflicts are likely. Lock data immediately.

Other transactions must wait.

🧠 How It Works

Uses:

SELECT * FROM account WHERE id=1 FOR UPDATE;

This locks the row.

Until transaction commits:

Other transactions cannot update it

🔴 Spring Boot Example
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT a FROM Account a WHERE a.id = :id")
Account findByIdForUpdate(Long id);

This applies DB-level lock.

✅ Pros

No conflict failures

Strong consistency

Good for high-contention data

❌ Cons

Lower concurrency

Risk of deadlocks

Blocking

Slower performance

📊 Comparison Table
Feature	Optimistic Lock	Pessimistic Lock
Lock timing	No lock while reading	Lock immediately
Conflict handling	Detect at commit	Prevent upfront
Performance	High	Lower
Deadlock risk	No	Yes
Retry needed	Yes	No
Best for	Read-heavy	Write-heavy / high conflict
🏗 Real-World Examples
🟢 Optimistic Locking Used In

E-commerce product updates

User profile update

CMS editing

Booking system (sometimes)

🔴 Pessimistic Locking Used In

Bank account withdrawal

Stock trading

High-frequency financial updates

Critical inventory management

🧠 When To Use What?
Use Optimistic Locking When:

Reads >> Writes

Low chance of conflict

High scalability needed

Use Pessimistic Locking When:

High contention

Strong consistency required

Conflict probability high

🔥 Interview-Level Example

If interviewer asks:

How would you prevent lost update problem?

You can say:

Use optimistic locking with version field

Or use pessimistic lock with SELECT FOR UPDATE

Choose based on contention level

🏆 Architect-Level Insight

Modern systems prefer:

Optimistic locking

Idempotency

Unique constraints

Avoid distributed locks

Pessimistic locking should be limited to critical sections.

🎯 Interview-Ready Summary

Optimistic locking assumes conflicts are rare and detects them at commit time using a version field, while pessimistic locking assumes conflicts are likely and locks the data upfront to prevent concurrent modifications. Optimistic locking offers better scalability, while pessimistic locking ensures stronger immediate consistency.

