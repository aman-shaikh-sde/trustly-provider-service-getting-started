package com.hulkhiretech.payments.trustly.req;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Params {
    @SerializedName("Signature")
    private String signature;

    @SerializedName("UUID")
    private String uuid;

    @SerializedName("Data")
    private DataPayload data;
}
