What is the CAP Theorem?
The CAP theorem states that a distributed data system can only guarantee two out of the following three properties at any given time:  

### 1. Consistency (C)

Every read receives the most recent write (or an error).

    💡 Like in a traditional RDBMS — when you write something,
    any read from any node should immediately return the updated value.

Example:

    Write: Set balance to ₹5000

    Read from any replica: Must return ₹5000

### 2. Availability (A)

Every request (read or write) gets a non-error response, even if it’s not the latest data.

    💡 The system is always responsive — it won’t deny your request, even if it's using stale data.

Example:

    One node goes down, but the system still returns a (possibly older) value instead of an error.

### 3. Partition Tolerance (P)

The system continues to operate even if network failures occur between nodes.

    💡 In real distributed systems, partitions will happen, so partition tolerance is non-negotiable.

Example:

    A data center in Mumbai can't communicate with one in Delhi, but both should still continue functioning.

| Property                | Description                                                                                          |  
| ----------------------- | ---------------------------------------------------------------------------------------------------- |  
| Consistency (C)         | Every read receives the most recent write or an error. All nodes see the same data at the same time. |  
| Availability (A)        | Every request (read or write) receives a response-even if some nodes are down.                       |  
| Partition Tolerance (P) | The system continues to function despite communication breakdowns between nodes.                     |  

### The Core Trade-off
In a partitioned network (which is inevitable in distributed systems), you have to choose between Consistency and Availability:  
- If you prioritize Consistency, the system may reject requests (becoming unavailable) during a partition.
- If you prioritize Availability, the system may return outdated or inconsistent data to keep running.
This results in three system archetypes:

| System Type                          | Properties Guaranteed                                                        | Real-World Examples                                |  
| ------------------------------------ | ---------------------------------------------------------------------------- | -------------------------------------------------- |  
| CP (Consistent + Partition Tolerant) | Sacrifices Availability                                                      | HBase, MongoDB (in some configs)                   |  
| AP (Available + Partition Tolerant)  | Sacrifices Consistency                                                       | Couchbase, Cassandra                               |  
| CA (Consistent + Available)          | Only works if no partition occurs (theoretical in large distributed systems) | Often infeasible in real-world distributed systems |  

### Why It Matters
In real-world distributed environments (like cloud systems), network partitions are bound to happen. That’s why designers must choose what’s more critical:
- Banking systems might favor Consistency (no incorrect balances!)
- Social media platforms might favor Availability (feed loads, even if slightly out of date)  

![image](https://github.com/user-attachments/assets/9c79c7e7-e698-4caf-bae8-6b3d5d564557)


https://www.youtube.com/watch?v=BHqjEjzAicA
https://www.youtube.com/watch?v=VdrEq0cODu4  



