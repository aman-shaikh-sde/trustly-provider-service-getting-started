package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.pojo.DepositRequest;
import com.hulkhiretech.payments.pojo.DepositResponse;

public interface DepositService {
	
	public DepositResponse createDeposit(DepositRequest depositRequest);

}
