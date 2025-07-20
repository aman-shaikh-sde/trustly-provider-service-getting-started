package com.hulkhiretech.payments.trustly.res.error;

import lombok.Data;

@Data
public class ErrorDetail {
    private String name;
    private String code;
    private String message;
    private NestedError error;
}