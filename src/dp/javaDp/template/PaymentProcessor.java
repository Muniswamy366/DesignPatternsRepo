public abstract class PaymentProcessor {

    // 👉 Template Method (final = cannot be overridden)
    public final void processPayment(double amount) {
        validate();
        debit(amount);
        sendReceipt();
    }

    protected abstract void validate();
    protected abstract void debit(double amount);

    // common step (default implementation)
    protected void sendReceipt() {
        System.out.println("Sending payment receipt");
    }
}
