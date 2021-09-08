package net.oskarstrom.hyphen.thr;

public class IllegalClassException extends RuntimeException {
	public IllegalClassException() {
	}

	public IllegalClassException(String message) {
		super(message);
	}

	public IllegalClassException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalClassException(Throwable cause) {
		super(cause);
	}

	public IllegalClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
