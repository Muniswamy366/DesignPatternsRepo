What is the CAP Theorem?
The CAP theorem states that a distributed data system can only guarantee two out of the following three properties at any given time:  

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





