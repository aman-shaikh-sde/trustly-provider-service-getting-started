package com.hulkhiretech.payments.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.constant.Endpoint;
import com.hulkhiretech.payments.pojo.DepositRequest;
import com.hulkhiretech.payments.pojo.DepositResponse;
import com.hulkhiretech.payments.service.interfaces.DepositService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoint.V1_TRUSTLY_DEPOSITS)
@Slf4j
@RequiredArgsConstructor
public class DepositController {
	
	private final DepositService depositService;

	@PostMapping
	public ResponseEntity<DepositResponse> createDeposit(@RequestBody DepositRequest depositRequest) {
		log.info("Creating a new deposit| depositRequest:{}", depositRequest);
		// Logic to create a deposit would go here
		
		DepositResponse response = depositService.createDeposit(depositRequest);
		log.info("Deposit creation response: {}", response);
		
		// create responseEntity with Created status & body as response object
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(response);
	}
}
