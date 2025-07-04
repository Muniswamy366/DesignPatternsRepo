What is the CAP Theorem?
The CAP theorem states that a distributed data system can only guarantee two out of the following three properties at any given time:  

| Property                | Description                                                                                          |  
| ----------------------- | ---------------------------------------------------------------------------------------------------- |  
| Consistency (C)         | Every read receives the most recent write or an error. All nodes see the same data at the same time. |  
| Availability (A)        | Every request (read or write) receives a response—even if some nodes are down.                       |  
| Partition Tolerance (P) | The system continues to function despite communication breakdowns between nodes.                     |  

| Feature           | Reverse Proxy                      | Forward Proxy                         |  
| ----------------- | ---------------------------------- | ------------------------------------- |  
| Acts on behalf of | Server                             | Client                                |  
| Used by           | Server-side (e.g., load balancing) | Client-side (e.g., content filtering) |  
| Hides             | Backend servers                    | Clients                               |  




