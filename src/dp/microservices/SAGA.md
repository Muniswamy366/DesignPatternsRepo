* ### SAGA Pattern – Managing Distributed Transactions
Since ACID transactions don’t scale across services and databases, SAGA is used for managing long-running distributed transactions.

### SAGA Approaches:
#### a. Choreography (Event-Based)
- Each service listens for events and reacts accordingly.

- No central controller.

- Lightweight but harder to manage for complex flows.

Example:

Order Service → emits "Order Created"  
↓  
Payment Service → emits "Payment Completed"  
↓  
Inventory Service → emits "Inventory Reserved"  
↓  
Shipping Service → ships the product

#### If failure occurs:
- Services emit failure events (PaymentFailed, InventoryFailed)

- Other services listen and compensate (e.g., cancel order, refund)

✅ Pros:
- Simple, no central orchestrator

- Loosely coupled

❌ Cons:
- Hard to monitor and debug

- Complex logic spread across services

https://www.youtube.com/watch?v=WGI_ciUa3FE  
https://github.com/JavaaTechSolutions/distributed-transaction/tree/main  
  
#### b. Orchestration (Central Coordinator)
- A central orchestrator controls the flow.

- More control, easier to trace, but can become a bottleneck.

Example:  

Orchestrator → call OrderService  
             → call PaymentService  
             → call InventoryService  
             → call ShippingService  

```
public class OrderOrchestrator {

    public void processOrder(Order order) {
        try {
            paymentService.pay(order);
            inventoryService.reserve(order);
            shippingService.ship(order);
        } catch (Exception e) {
            // Compensation in reverse order
            shippingService.cancel(order);
            inventoryService.release(order);
            paymentService.refund(order);
        }
    }
}
```

