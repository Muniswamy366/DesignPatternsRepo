Depend on abstractions, not on concrete implementations.
 High-level modules should not depend on low-level modules. Both should depend on abstractions.

❌ Violation:

class MySQLDatabase {
    public void save(String data) { /* ... */ }
}

class UserService {
    private MySQLDatabase db = new MySQLDatabase();
}

✅ Solution:

interface Database {
    void save(String data);
}

class MySQLDatabase implements Database {
    public void save(String data) { /* ... */ }
}

class UserService {
    private final Database db;

    public UserService(Database db) {
        this.db = db;
    }
}
