### Interface Segregation Principle (ISP)

Clients should not be forced to depend on interfaces they do not use.  

In simple words:  

👉 Don’t create “fat interfaces”  
👉 Split large interfaces into smaller, specific ones  

Instead of one huge interface:

Java separates:  

List  

Set  

Queue  

Deque  

Each has specific responsibilities.  

The Interface Segregation Principle states that clients should not be forced to implement interfaces they don't use. ISP splits interfaces that are very large into smaller and more specific ones so that clients will only have to know about the methods that are of interest to them.

| ISP                       | LSP                               |
| ------------------------- | --------------------------------- |
| About interface size      | About behavioral substitutability |
| Prevents fat interfaces   | Prevents broken inheritance       |
| Compile-time design issue | Runtime behavior issue            |



LSP segrigating related classes under one umbrilla, ISP is used to segrigate method into specific interface.

Code example:
https://www.javaguides.net/2018/02/interface-segregation-principle.html#:~:text=The%20Interface%20Segregation%20Principle%20states,are%20of%20interest%20to%20them.

![image](https://github.com/user-attachments/assets/e35bbfc8-a14e-4afd-9a4b-1905ab7c6c39)


❌ Violation:
```
interface Machine {
    void print();
    void scan();
    void fax();
}

class OldPrinter implements Machine {
    public void print() { /* yes */ }
    public void scan() { throw new UnsupportedOperationException(); }
    public void fax() { throw new UnsupportedOperationException(); }
}
```

✅ Solution:
Split interfaces:

```
interface Printer {
    void print();
}

interface Scanner {
    void scan();
}

class OldPrinter implements Printer {
    public void print() { /* print only */ }
}
```
