package com.hulkhiretech.payments.pojo;

import lombok.Data;

@Data
public class DepositRequest {

	private String txnReference;
	private String endUserId;
	
	private Double amount;
	private String currency;

	private String firstName;
	private String lastName;
	private String email;
	
	private String country;
	private String locale;
	
	private String successUrl;
	private String failUrl;
	
}
