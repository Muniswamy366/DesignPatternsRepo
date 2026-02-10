public class OrderFacade {

    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final NotificationService notificationService;

    public OrderFacade() {
        this.inventoryService = new InventoryService();
        this.paymentService = new PaymentService();
        this.shippingService = new ShippingService();
        this.notificationService = new NotificationService();
    }

    public void placeOrder(String productId, double amount) {

        if (!inventoryService.isInStock(productId)) {
            System.out.println("Product out of stock");
            return;
        }

        if (!paymentService.makePayment(amount)) {
            System.out.println("Payment failed");
            return;
        }

        shippingService.shipProduct(productId);
        notificationService.sendOrderConfirmation();

        System.out.println("Order placed successfully 🎉");
    }
}
