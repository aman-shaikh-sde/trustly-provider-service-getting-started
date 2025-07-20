package com.hulkhiretech.payments.trustly.req;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataPayload {
    @SerializedName("Username")
    private String username;

    @SerializedName("Password")
    private String password;

    @SerializedName("NotificationURL")
    private String notificationUrl;

    @SerializedName("EndUserID")
    private String endUserId;

    @SerializedName("MessageID")
    private String messageId;

    @SerializedName("Attributes")
    private Attributes attributes;
}
