package com.hulkhiretech.payments.trustly.res.error;

import lombok.Data;

@Data
public class NestedError {
    private String signature;
    private String uuid;
    private String method;
    private ErrorData data;
}

