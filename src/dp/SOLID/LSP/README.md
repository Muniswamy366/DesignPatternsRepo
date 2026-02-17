### Liskov Substitution Principle (LSP)

objects of a superclass should be replaceable with objects of its subclasses without breaking the application

Explanation:
https://www.youtube.com/watch?v=129QkkXUHeQ

Code example:
https://www.javaguides.net/2018/02/liskov-substitution-principle.html

1. Subtypes must be substitutable for their base types without breaking behavior.

❌ Violation:
```
class Bird {
    public void fly() {
        System.out.println("Flying");
    }
}

class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins can't fly");
    }
}

Bird bird = new Penguin();
bird.fly();  // runtime error

```

✅ Solution:
Redesign the hierarchy:

```
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
```

2. ❌ LSP Violation Example
Let's say you have a Vehicle class that assumes all vehicles can be refueled with petrol:

```
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
```

🚫 Problem:
ElectricCar inherits refuel() but cannot implement it meaningfully.

If a method expects a Vehicle and calls refuel(), it may break when passed an ElectricCar.

✅ LSP-Compliant Design
Instead of one generic Vehicle class with refuel(), we separate concerns using interfaces:

```
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
```

✅ Benefits:
Now, PetrolCar and ElectricCar are used through the right interfaces.

You never get into a situation where a method expects a Vehicle and incorrectly assumes it has refuel().
