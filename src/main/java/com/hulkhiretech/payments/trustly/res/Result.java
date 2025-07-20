package com.hulkhiretech.payments.trustly.res;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Result {
    private String signature;
    private String uuid;
    private String method;
    private DataPayload data;
}