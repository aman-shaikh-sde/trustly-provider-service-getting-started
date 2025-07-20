package com.hulkhiretech.payments.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TrustlyProviderException extends RuntimeException {
	private static final long serialVersionUID = -6560387861714534572L;

	private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public TrustlyProviderException(
    		String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage); // Optional: to include message in stack trace
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
