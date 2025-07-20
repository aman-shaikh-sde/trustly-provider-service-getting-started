package com.hulkhiretech.payments.trustly.res;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DataPayload {
    private String orderid;
    private String url;
}