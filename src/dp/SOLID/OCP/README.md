### Open Closed Principle (OCP)

Software entities (modules, classes and methods) should be open for extension and closed for modification.


base class -> sub class

base class and methods open for extension and closed for modification.

Read:
https://www.baeldung.com/java-open-closed-principle#:~:text=As%20the%20name%20suggests%2C%20this,be%20extended%2C%20but%20not%20modified.


Open/Closed Principle (OCP) Software entities should be open for extension but closed for modification.

```
// Bad Example
class PaymentProcessor {
    void processPayment(String type) {
        if (type.equals("CREDIT_CARD")) {
            // process credit card
        } else if (type.equals("PAYPAL")) {
            // process PayPal
        }
    }
}

// Good Example
interface PaymentMethod {
    void processPayment();
}

class CreditCardPayment implements PaymentMethod {
    @Override
    public void processPayment() {
        // process credit card payment
    }
}

class PayPalPayment implements PaymentMethod {
    @Override
    public void processPayment() {
        // process PayPal payment
    }
}
```
