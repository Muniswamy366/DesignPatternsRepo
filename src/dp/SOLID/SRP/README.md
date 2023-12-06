### Single Responsibility Principle (SRP)

There shouldn't be more than one reason to change change a class.

#### Begin
1. Getting the request and converting to pojo
2. Validating the request (If user validation changes)
3. Storing the data into map. (If storing data changes or db changes to nosql)

There are multiple responsibilities are handled in single UserController class.

#### End
1. Seperate validation and storing data into map into different classes.