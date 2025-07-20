package com.hulkhiretech.payments.trustly.res;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TrustlyDepositResponse {
    private Result result;
    private String version;
}
