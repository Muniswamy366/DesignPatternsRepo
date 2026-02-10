package dp.singleton;

import java.io.ObjectStreamException;

public class Singleton1 {

	private static final Singleton1 INSTANCE = new Singleton1();

	private Singleton1() {
		// Check if we already have an instance
		// to prevent reflection.
		if (INSTANCE != null) {
			throw new IllegalStateException("Singleton" + " instance already created.");
		}
	}

	public static final Singleton1 getInstance() {
		return INSTANCE;
	}

	// to prevent de-serialization
	private Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}

	private Object writeReplace() throws ObjectStreamException {
		return INSTANCE;
	}

	// to prevent cloning.
	public Object clone() throws CloneNotSupportedException {
		// return INSTANCE
		throw new CloneNotSupportedException();
	}

}
