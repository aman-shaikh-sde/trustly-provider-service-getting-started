package com.hulkhiretech.payments.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.pojo.DepositRequest;
import com.hulkhiretech.payments.pojo.DepositResponse;
import com.hulkhiretech.payments.service.DepositServiceHelper;
import com.hulkhiretech.payments.service.interfaces.DepositService;
import com.hulkhiretech.payments.trustly.req.DepositRequestWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
	
	private final HttpServiceEngine httpServiceEngine;
	
	private final Gson gson;
	
	
	private final DepositServiceHelper depositServiceHelper;
	

	@Override
	public DepositResponse createDeposit(DepositRequest depositRequest) {
		log.info("Creating a new deposit| depositRequest:{}", depositRequest);
		
		HttpRequest request = depositServiceHelper.prepareDepositRequest(
				depositRequest);

		ResponseEntity<String> httpResponse = 
				httpServiceEngine.makeHttpCall(request);
		
		DepositResponse responseObj = depositServiceHelper.processResponse(
				httpResponse);
		log.info("Processed DepositResponse: {}", responseObj);
		
		return responseObj;
	}


	

}
