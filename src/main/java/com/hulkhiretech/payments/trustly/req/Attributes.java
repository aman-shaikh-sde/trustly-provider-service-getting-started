package com.hulkhiretech.payments.trustly.req;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Attributes {
    @SerializedName("Country")
    private String country;

    @SerializedName("Locale")
    private String locale;

    @SerializedName("Currency")
    private String currency;

    @SerializedName("Amount")
    private Double amount;

    @SerializedName("Firstname")
    private String firstName;

    @SerializedName("Lastname")
    private String lastName;

    @SerializedName("Email")
    private String email;

    @SerializedName("SuccessURL")
    private String successUrl;

    @SerializedName("FailURL")
    private String failUrl;
}
