package org.opensixen.exceptions;

import org.adempiere.exceptions.AdempiereException;

public class OpensixenException extends AdempiereException {

	public OpensixenException() {
		super();
	}

	public OpensixenException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpensixenException(String message) {
		super(message);
	}

	public OpensixenException(Throwable cause) {
		super(cause);
	}

}
