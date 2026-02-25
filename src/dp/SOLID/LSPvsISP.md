

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
