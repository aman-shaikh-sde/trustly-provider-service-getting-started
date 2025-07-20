package com.hulkhiretech.payments.trustly.res.error;

import lombok.Data;

@Data
public class TrustlyErrorResponse {
    private ErrorDetail error;
    private String version;
}
