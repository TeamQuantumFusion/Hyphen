package dev.quantumfusion.hyphen.thr.exception;

public class NotYetImplementedException extends HyphenException {

	public NotYetImplementedException(String message) {
		super(message);
	}

	private NotYetImplementedException(Throwable cause) {
		super(cause);
	}
}