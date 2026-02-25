

```
public interface PaymentService {
    void pay(double amount);
    void refund(double amount);
}

public class CreditCardPaymentService implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Paid via credit card");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refund to credit card");
    }
}

public class CashPaymentService implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Paid via cash");
    }

    @Override
    public void refund(double amount) {
        throw new UnsupportedOperationException("Cash payment cannot be refunded");
    }
}
```
### ISP Violation (Interface Problem)
Question:  

Does CashPayment really need refund()?  

No.  

But it is forced to implement it because:  

PaymentService  

is too big.  

That’s ISP violation.  

Clients are forced to depend on methods they do not use.  

CashPayment depends on refund() even though it doesn't support it.  

**This is a design-level issue.**  


### LSP Violation (Behavior Problem)

```
PaymentService service = new CashPaymentService();
processRefund(service);
```
💥 Runtime exception.  

Why?  

Because:  

The base contract says:  
Any PaymentService supports refund()  
But subclass broke that promise.  
This is LSP violation.  
Subtype cannot replace base type safely.  
This is a behavioral/runtime issue.  

### Why They Look Same

Because:  
ISP violation (fat interface)  
Causes  
LSP violation (runtime break)  
So ISP violation often leads to LSP violation.  
But conceptually:  

| Principle | Focus                  |
| --------- | ---------------------- |
| ISP       | Interface design       |
| LSP       | Behavioral correctness |

### Example: ISP Violation but NOT LSP
```
interface PaymentService {
    void pay();
    void refund();
}

class CashPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Cash paid");
    }

    @Override
    public void refund() {
        // empty implementation
    }
}
```
No exception.  
No crash.  
Program runs fine.  

So:  
❌ ISP violated (fat interface)  
✅ LSP NOT violated (no behavioral break)  

### Example: LSP Violation but NOT ISP

```
interface PaymentService {
    void pay(double amount);
}

class NormalPaymentService implements PaymentService {
    public void pay(double amount) {
        System.out.println("Paid: " + amount);
    }
}

class SecurePaymentService extends NormalPaymentService {

    @Override
    public void pay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        super.pay(amount);
    }
}

PaymentService service = new SecurePaymentService();
service.pay(-10); // exception
```

Base allowed negative.  
Subclass restricted it.  
This is LSP violation.  
But ISP is perfectly fine.  

### Proper Design Fix (For Both)

```
interface Payment {
    void pay(double amount);
}

interface Refundable {
    void refund(double amount);
}

class CreditCardPayment implements Payment, Refundable { }
class CashPayment implements Payment { }
```

Now:

No fat interface (ISP satisfied)  
No runtime surprise (LSP satisfied)  

🎯 ### Final Simple Mental Model

If you ask:

👉 "Is this interface too big?" → ISP  
👉 "Will subclass break client code?" → LSP  

🔥 ### Interview-Ready Final Statement

ISP is about splitting interfaces so clients aren’t forced to depend on unused methods. LSP is about ensuring subclasses honor the behavioral contract of the base type. ISP violations often lead to LSP violations, but they address different design concerns.
