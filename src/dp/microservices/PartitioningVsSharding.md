Many people use **Partitioning** and **Sharding** interchangeably, but there is a subtle difference.

# Short Answer

**Sharding is a type of partitioning.**

```text
Partitioning
    |
    +---- Vertical Partitioning
    |
    +---- Horizontal Partitioning (Sharding)
```

---

# Partitioning

Partitioning means dividing a large dataset into smaller parts.

This can happen:

### Within the same database server

Example:

```text
Users Table

Partition 1: UserID 1-100000
Partition 2: UserID 100001-200000
Partition 3: UserID 200001-300000
```

All partitions may still reside on the **same database machine**.

```text
Database Server
 ├── Partition1
 ├── Partition2
 └── Partition3
```

Purpose:

* Better query performance
* Easier maintenance
* Faster indexing

---

# Sharding

Sharding is **horizontal partitioning across multiple servers**.

Example:

```text
Shard1 (Server A)
Users 1-100000

Shard2 (Server B)
Users 100001-200000

Shard3 (Server C)
Users 200001-300000
```

```text
Server A      Server B      Server C
---------     ---------     ---------
Users         Users         Users
1-100000      100001-200000 200001-300000
```

Purpose:

* Horizontal scaling
* Increased storage
* Increased throughput

---

# Visual Comparison

## Partitioning

```text
Single Database Server

+-------------------+
| Database          |
|                   |
| Partition 1       |
| Partition 2       |
| Partition 3       |
+-------------------+
```

---

## Sharding

```text
+-----------+
| Shard 1   |
| Server A  |
+-----------+

+-----------+
| Shard 2   |
| Server B  |
+-----------+

+-----------+
| Shard 3   |
| Server C  |
+-----------+
```

---

# Types of Partitioning

## Vertical Partitioning

Split by columns.

Example:

User table:

```text
userId
name
email
photo
address
```

Split into:

### Table A

```text
userId
name
email
```

### Table B

```text
userId
photo
address
```

Useful when some columns are accessed frequently.

---

## Horizontal Partitioning

Split by rows.

```text
UserID 1-100000
UserID 100001-200000
```

This is the basis of sharding.

---

# Real Example

### PostgreSQL Partitioning

PostgreSQL supports native table partitioning:

```sql
PARTITION BY RANGE(user_id)
```

All partitions may still be on the same server.

This is partitioning.

---

### Cassandra Sharding

Apache Cassandra uses consistent hashing.

```text
hash(userId)
```

Data is distributed across multiple nodes.

This is sharding.

---

# Interview Table

| Feature       | Partitioning               | Sharding                  |
| ------------- | -------------------------- | ------------------------- |
| Definition    | Split data into partitions | Split data across servers |
| Server Count  | Usually one server         | Multiple servers          |
| Scaling       | Limited                    | Horizontal                |
| Complexity    | Lower                      | Higher                    |
| Network Calls | No                         | Yes                       |
| Rebalancing   | Easier                     | Harder                    |
| Example       | PostgreSQL partitions      | Cassandra shards          |

---

# Interview Answer (30 Seconds)

> Partitioning is the general technique of dividing data into smaller logical pieces. Sharding is a specific form of horizontal partitioning where those partitions are distributed across multiple database servers. Every sharding solution is partitioning, but not every partitioning solution is sharding. Partitioning improves manageability and query performance, while sharding primarily enables horizontal scalability and higher throughput.
