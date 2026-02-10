public class CreditCardPayment extends PaymentProcessor {

    @Override
    protected void validate() {
        System.out.println("Validating credit card details");
    }

    @Override
    protected void debit(double amount) {
        System.out.println("Debiting ₹" + amount + " from credit card");
    }
}
