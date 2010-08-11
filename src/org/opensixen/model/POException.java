package org.opensixen.model;

public class POException extends Exception {

	/**
	 * @param message
	 * @param cause
	 */
	public POException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public POException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public POException(Throwable cause) {
		super(cause);
	}

	
}
