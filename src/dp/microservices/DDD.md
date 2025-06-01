### Domain Driven Design

#### Netflix:
**Video Streaming:** Core domain responsible for streaming videos.  
**Recommendations:** Subdomain handling personalized recommendations.  
**Billing:** Manages subscriptions and payment details.  

**Entities:** Objects with a distinct, continuous identity (e.g., a User with an Id and Name).  
**Aggregates:** Group of entities or Clusters of domain objects grouped together for transactional consistency.  

**Entities:** Subscribers, video preferences, viewing history.  
**Aggregates:** User profiles (with preferences and history). 

**Entity:** A person – even if they change their name or address, they're still the same individual.  
**Value Object:** A home address – if any detail changes (e.g., street name), it's treated as a new address.  

![image](https://github.com/user-attachments/assets/bf4ab0e4-a29c-49cb-a2d7-07d386e7fa73)   

✅ ** Bounded Context means:** “This part of the system has its own clear meaning for domain concepts (like Order), and doesn’t share them directly with other parts.”  

Imagine a Real-Life Business: Amazon has many different departments (or contexts) like:  

Sales Department  

Shipping Department  

Payments Department  

Each department uses the word "Order", but they mean different things.  

📦 In the Sales Context:  
"Order" = what the customer places when they buy something.  

Contains: list of items, customer info, total price.  

🚚 In the Shipping Context:  
"Order" = what needs to be shipped.  

Contains: delivery address, shipment status, tracking ID.  

💳 In the Payments Context:  
"Order" = what needs to be paid.  

Contains: payment status, transaction ID, refund details.  

If you tried to use one common model for "Order" across all departments, it would be confusing and hard to manage.  

So instead, each department gets its own Bounded Context:  

+----------------------+   +---------------------+   +----------------------+  
|  Sales Bounded Context|   | Shipping Bounded Context|   | Payments Bounded Context |  
|----------------------|   |---------------------|   |----------------------|  
| Order (id, items,...)|   | Order (id, address,...)|   | Order (id, amount,...)|  
+----------------------+   +---------------------+   +----------------------+  

Each context has its own version of "Order" that makes sense only in that department.  


![image](https://github.com/user-attachments/assets/79175a09-f193-4e56-8175-37be4bce29f2)

![image](https://github.com/user-attachments/assets/991ce177-8111-4f8e-a118-9842b8b38163)


