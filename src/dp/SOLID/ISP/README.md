### Interface Segregation Principle (ISP)

The Interface Segregation Principle states that clients should not be forced to implement interfaces they don't use. ISP splits interfaces that are very large into smaller and more specific ones so that clients will only have to know about the methods that are of interest to them.


LSP segrigating related classes under one umbrilla, ISP is used to segrigate method into specific interface.

Code example:
https://www.javaguides.net/2018/02/interface-segregation-principle.html#:~:text=The%20Interface%20Segregation%20Principle%20states,are%20of%20interest%20to%20them.

1. Subtypes must be substitutable for their base types without breaking behavior.

❌ Violation:

class Bird {
    public void fly() {}
}

class Ostrich extends Bird {
    public void fly() { throw new UnsupportedOperationException(); }
}

✅ Solution:
Redesign the hierarchy:

interface Bird {}

interface FlyingBird extends Bird {
    void fly();
}

class Sparrow implements FlyingBird {
    public void fly() { /* flying logic */ }
}

class Ostrich implements Bird {
    // does not fly
}
