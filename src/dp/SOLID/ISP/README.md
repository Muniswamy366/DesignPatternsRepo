### Interface Segregation Principle (ISP)

Don't force a class to implement interfaces it doesn't use.

The Interface Segregation Principle states that clients should not be forced to implement interfaces they don't use. ISP splits interfaces that are very large into smaller and more specific ones so that clients will only have to know about the methods that are of interest to them.


LSP segrigating related classes under one umbrilla, ISP is used to segrigate method into specific interface.

Code example:
https://www.javaguides.net/2018/02/interface-segregation-principle.html#:~:text=The%20Interface%20Segregation%20Principle%20states,are%20of%20interest%20to%20them.

🔁 Summary Comparison
Aspect	Liskov Substitution Principle (LSP)	Interface Segregation Principle (ISP)
🔍 Focus	Behavior compatibility in inheritance	Interface size and usage
🔥 Violation	Subclass breaks base class behavior	Class forced to implement unused methods
✅ Fix	Use appropriate abstraction, avoid incorrect inheritance	Split large interfaces into smaller ones
📌 Goal	Replace base class with subclass safely	Keep interfaces lean and role-specific
