package com.hulkhiretech.payments.http;

import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
public class HttpRequest {

	private HttpMethod httpMethod;
	private String url;
	private String body;

}
