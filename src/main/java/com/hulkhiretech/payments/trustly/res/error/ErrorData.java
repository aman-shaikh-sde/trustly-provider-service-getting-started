package com.hulkhiretech.payments.trustly.res.error;

import lombok.Data;

@Data
public class ErrorData {
    private String code;
    private String message;
}