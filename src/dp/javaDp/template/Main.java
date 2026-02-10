public class Main {
    public static void main(String[] args) {

        PaymentProcessor payment1 = new CreditCardPayment();
        payment1.processPayment(1500);

        System.out.println("------");

        PaymentProcessor payment2 = new UpiPayment();
        payment2.processPayment(500);
    }
}

/*Where you see this in real Java frameworks

JdbcTemplate
RestTemplate

JdbcTemplate.execute(sql);
 */

/*Validating credit card details
Debiting ₹1500 from credit card
Sending payment receipt
------
Validating UPI ID
Debiting ₹500 via UPI
Sending payment receipt*/
