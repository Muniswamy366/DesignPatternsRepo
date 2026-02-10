public class UpiPayment extends PaymentProcessor {

    @Override
    protected void validate() {
        System.out.println("Validating UPI ID");
    }

    @Override
    protected void debit(double amount) {
        System.out.println("Debiting ₹" + amount + " via UPI");
    }
}
