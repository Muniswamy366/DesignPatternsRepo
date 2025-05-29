### Single Responsibility Principle (SRP)

There shouldn't be more than one reason to change change a class.

#### Begin
1. Getting the request and converting to pojo
2. Validating the request (If user validation changes)
3. Storing the data into map. (If storing data changes or db changes to nosql)

There are multiple responsibilities are handled in single UserController class.

#### End
1. Seperate validation and storing data into map into different classes.


A class should have only one reason to change.

❌ Violation:
public class Invoice {
    public void calculateTotal() { /* logic */ }
    public void printInvoice() { /* logic */ }
    public void saveToDB() { /* logic */ }
}

✅ Solution:
Split responsibilities:

public class Invoice {
    public void calculateTotal() { /* logic */ }
}

public class InvoicePrinter {
    public void print(Invoice invoice) { /* logic */ }
}

public class InvoiceRepository {
    public void save(Invoice invoice) { /* logic */ }
}
