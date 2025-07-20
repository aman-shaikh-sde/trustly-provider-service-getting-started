package com.hulkhiretech.payments.service;

import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.constant.ErrorEnum;
import com.hulkhiretech.payments.exception.TrustlyProviderException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.pojo.DepositRequest;
import com.hulkhiretech.payments.pojo.DepositResponse;
import com.hulkhiretech.payments.trustly.req.Attributes;
import com.hulkhiretech.payments.trustly.req.DataPayload;
import com.hulkhiretech.payments.trustly.req.DepositRequestWrapper;
import com.hulkhiretech.payments.trustly.req.Params;
import com.hulkhiretech.payments.trustly.res.TrustlyDepositResponse;
import com.hulkhiretech.payments.trustly.res.error.TrustlyErrorResponse;
import com.hulkhiretech.payments.trustly.security.RS256SignerVerifier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositServiceHelper {
	
	private final Gson gson;
	
	@Value("${merchant.username}")
	private String merchantUsername;
	
	@Value("${merchant.password}")
	private String merchantPassword;
	
	@Value("${notification.url}")
	private String notificationUrl;
	
	@Value("${trustly.deposit.url}")
	String trustlyDepositUrl;
	

	public DepositResponse processResponse(ResponseEntity<String> httpResponse) {
		log.info("Processing HTTP response: {}", httpResponse);
		
		if(httpResponse.getStatusCode().is2xxSuccessful()) {// Success
			
			String body = httpResponse.getBody();
			log.info("HTTP response body: {}", body);
			
	        TrustlyDepositResponse depositResponse = gson.fromJson(
	        		body, TrustlyDepositResponse.class);
	        log.info("Parsed TrustlyDepositResponse: {}", depositResponse);

			if (depositResponse != null
					&& depositResponse.getResult() != null
					&& depositResponse.getResult().getData() != null
					&& depositResponse.getResult().getData().getUrl() != null) {
				log.error("Deposit response or its data is null");

				DepositResponse response = new DepositResponse();
				response.setOrderid(depositResponse.getResult().getData().getOrderid());
				response.setUrl(depositResponse.getResult().getData().getUrl());
				log.info("DepositResponse created: {}", response);
				
				return response;
			}
	        
			log.error("Deposit response is invalid");// TODO let your code handle
		}
		
		// Else failure
		
		// Valid Error Response
		if(httpResponse.getStatusCode().is4xxClientError() || 
                httpResponse.getStatusCode().is5xxServerError()) {
			log.error("HTTP error occurred: {}", httpResponse.getBody());
			
	        TrustlyErrorResponse errorResponse = gson.fromJson(
	        		httpResponse.getBody(), TrustlyErrorResponse.class);

			log.error("Parsed TrustlyErrorResponse: {}", errorResponse);
			
			if (errorResponse != null 
					&& errorResponse.getError() != null) {
				log.error("Error response or its error field is null");

				String trustlyErrorMessage = errorResponse.getError().getCode() 
						+ " - " + errorResponse.getError().getMessage();
				
				throw new TrustlyProviderException(
						ErrorEnum.TRUSTLY_ERROR.getErrorCode(),
						trustlyErrorMessage, 
						HttpStatus.valueOf(httpResponse.getStatusCode().value()));
			}
			
			log.error("Error response is invalid: {}", errorResponse);
		}
		
		
		log.error("Unexpected response from Trustly: {}", httpResponse.getBody());
		
		throw new TrustlyProviderException(
				ErrorEnum.ERROR_PROCESSING_TRUSTLY_RESPONSE.getErrorCode(), 
				ErrorEnum.ERROR_PROCESSING_TRUSTLY_RESPONSE.getErrorMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		
		// what HttpStatus Code - 500
		// 
	}


	public HttpRequest prepareDepositRequest(DepositRequest depositRequest) {
		log.info("Preparing deposit request for depositRequest: {}", depositRequest);
		Attributes attributes = Attributes.builder()
                .country(depositRequest.getCountry())
                .locale(depositRequest.getLocale())
                .currency(depositRequest.getCurrency())
                .amount(depositRequest.getAmount())
                .firstName(depositRequest.getFirstName())
                .lastName(depositRequest.getLastName())
                .email(depositRequest.getEmail())
                
                .successUrl(depositRequest.getSuccessUrl())
                .failUrl(depositRequest.getFailUrl())
                .build();

        
		DataPayload data = DataPayload.builder()
                .username(merchantUsername)
                .password(merchantPassword)
                .notificationUrl(notificationUrl)
                .endUserId(depositRequest.getEndUserId())
                .messageId(depositRequest.getTxnReference())
                .attributes(attributes)
                .build();

		//sign(String method, String uuid, JsonElement data, 
		String signature = RS256SignerVerifier.sign(
				Constant.DEPOSIT, 
				depositRequest.getTxnReference(), 
				gson.toJson(data));
		
		log.info("Generated signature: {}", signature);
		
		if(signature == null || signature.isEmpty()) {
			log.error("Failed to generate signature for deposit request");
			throw new TrustlyProviderException(
					ErrorEnum.ERROR_GENERATING_SIGNATURE.getErrorCode(),
					ErrorEnum.ERROR_GENERATING_SIGNATURE.getErrorMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
        Params params = Params.builder()
                .signature(signature)
                .uuid(depositRequest.getTxnReference())
                .data(data)
                .build();

        DepositRequestWrapper request = DepositRequestWrapper.builder()
                .method(Constant.DEPOSIT)
                .params(params)
                .version(Constant.VERSION_1_1)
                .build();
        log.info("Prepared deposit request: {}", request);
        
        String jsonReqData = gson.toJson(request);
        log.info("JSON request data: {}", jsonReqData);
		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setBody(jsonReqData);
		httpRequest.setHttpMethod(HttpMethod.POST);
		
		httpRequest.setUrl(trustlyDepositUrl);
		
		log.info("Prepared HttpRequest: {}", httpRequest);
		return httpRequest;
	}
}
