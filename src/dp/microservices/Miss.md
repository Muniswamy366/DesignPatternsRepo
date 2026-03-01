

Interview Answer:
Question : "What problem does ETag solve?"

Answer :

"ETag solves the Lost Update Problem , also known as a Write-Write Conflict . This occurs when two users read the same data, make changes, and save - the last save overwrites the first without anyone knowing. 
ETag implements Optimistic Locking by using version numbers to detect concurrent modifications and prevent data loss."
The problem ETag solves has multiple names in computer science:

Official Names:
1. Lost Update Problem ⭐ (Most Common)
When one user's update overwrites another user's update without anyone knowing.

2. Write-Write Conflict
Two concurrent writes to the same data, last one wins.

3. Concurrent Modification Problem
Multiple users modifying the same data simultaneously.

4. Race Condition (in database context)
Two operations racing to update the same record.

5. Dirty Write
Writing over uncommitted or unread changes from another transaction.


Related Database Concepts:
Optimistic Locking (The Solution)
Assume conflicts are rare

Don't lock data while reading

Check version before writing

ETag implements Optimistic Locking

Pessimistic Locking (Alternative Solution)
Assume conflicts are common

Lock data while reading/editing

Others must wait

Slower but prevents conflicts upfront


In Academic Terms:
ACID Violation - Isolation Problem
Specifically violates the "I" in ACID :

A tomicity

C onsistency

I solation ← This one!

D urability

Without ETag, transactions are not properly isolated from each other.



2️⃣ What Problem Does It Solve?

DynamoDB normally:

Supports single-item atomic writes

Does NOT guarantee atomicity across multiple items

Problem:

If you update multiple items across:

Multiple tables

Multiple keys

And one fails → partial update happens.

This causes:

Inconsistent state

Financial errors

Broken invariants

TransactWriteItemsEnhancedRequest solves this by providing:

Atomic, all-or-nothing writes across multiple items (up to 100).

A tomicity ← This one!

C onsistency

I solation 

D urability
