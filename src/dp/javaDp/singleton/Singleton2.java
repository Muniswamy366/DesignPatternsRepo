package dp.singleton;

import java.io.ObjectStreamException;

public class Singleton2 {

	/*
	 * Without volatile modifier it's possible for another thread in Java to see
	 * half initialized state of _instance variable, but with volatile variable
	 * guaranteeing happens-before relationship, all the write will happen on
	 * volatile _instance before any read of _instance variable.
	 */
	private volatile static Singleton2 INSTANCE;

	private Singleton2() {
		// Check if we already have an instance
		// to prevent reflection.
		if (INSTANCE != null) {
			throw new IllegalStateException("Singleton" + " instance already created.");
		}
	}

	public static final Singleton2 getInstance() {
		if (INSTANCE == null) {
			synchronized (Singleton2.class) {
				//double checked locking - because second check of Singleton instance with lock
				if (INSTANCE == null) {
					INSTANCE = new Singleton2();
				}
			}
		}
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
