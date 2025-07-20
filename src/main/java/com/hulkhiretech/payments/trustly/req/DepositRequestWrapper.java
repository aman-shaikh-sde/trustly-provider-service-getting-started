package com.hulkhiretech.payments.trustly.req;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositRequestWrapper {
    @SerializedName("method")
    private String method;

    @SerializedName("params")
    private Params params;

    @SerializedName("version")
    private String version;
}
