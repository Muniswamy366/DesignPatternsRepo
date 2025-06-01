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

![image](https://github.com/user-attachments/assets/79175a09-f193-4e56-8175-37be4bce29f2)


![image](https://github.com/user-attachments/assets/bf4ab0e4-a29c-49cb-a2d7-07d386e7fa73)   

![image](https://github.com/user-attachments/assets/991ce177-8111-4f8e-a118-9842b8b38163)


