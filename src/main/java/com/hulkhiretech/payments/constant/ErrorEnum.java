package com.hulkhiretech.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    GENERIC_ERROR(
    		"30000", "Unable to process your request, please try later"),
    
    UNABLE_TO_CONNECT_TRUSTLY(
    		"30001", "Unable to connect to TrustlyPSP, please try later"),
    
	ERROR_PROCESSING_TRUSTLY_RESPONSE(
			"30002", "Error processing TrustlyPSP response, please try later"),
	
	TRUSTLY_ERROR("30003", "<Dynamic message based on Trustly Error Response>"), 
	ERROR_GENERATING_SIGNATURE("30004", "Error Generating Signature, Try again later");

    private final String errorCode;
    private final String errorMessage;

    ErrorEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

