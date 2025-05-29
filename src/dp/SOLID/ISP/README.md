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





❌ LSP Violation Example
Let's say you have a Vehicle class that assumes all vehicles can be refueled with petrol:

class Vehicle {
    public void refuel() {
        System.out.println("Refueling with petrol...");
    }
}

class PetrolCar extends Vehicle {
    @Override
    public void refuel() {
        System.out.println("Refueling with petrol...");
    }
}

class ElectricCar extends Vehicle {
    @Override
    public void refuel() {
        throw new UnsupportedOperationException("Electric cars cannot be refueled with petrol!");
    }
}

🚫 Problem:
ElectricCar inherits refuel() but cannot implement it meaningfully.

If a method expects a Vehicle and calls refuel(), it may break when passed an ElectricCar.

✅ LSP-Compliant Design
Instead of one generic Vehicle class with refuel(), we separate concerns using interfaces:

interface Vehicle {
    void drive();
}

interface Refuelable {
    void refuel();
}

interface Rechargeable {
    void recharge();
}

class PetrolCar implements Vehicle, Refuelable {
    @Override
    public void drive() {
        System.out.println("Driving petrol car...");
    }

    @Override
    public void refuel() {
        System.out.println("Refueling petrol car...");
    }
}

class ElectricCar implements Vehicle, Rechargeable {
    @Override
    public void drive() {
        System.out.println("Driving electric car...");
    }

    @Override
    public void recharge() {
        System.out.println("Recharging electric car...");
    }
}
✅ Benefits:
Now, PetrolCar and ElectricCar are used through the right interfaces.

You never get into a situation where a method expects a Vehicle and incorrectly assumes it has refuel().
