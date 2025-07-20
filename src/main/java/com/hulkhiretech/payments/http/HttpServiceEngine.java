package com.hulkhiretech.payments.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.hulkhiretech.payments.constant.ErrorEnum;
import com.hulkhiretech.payments.exception.TrustlyProviderException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpServiceEngine {
	
	private final RestClient restClient;

	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
		log.info("Making an HTTP call... httpRequest:{}", httpRequest);
		
		try {
			ResponseEntity<String> response = restClient
					.method(httpRequest.getHttpMethod())
					.uri(httpRequest.getUrl())
					.contentType(MediaType.APPLICATION_JSON)
					.body(httpRequest.getBody())
					.retrieve()
					.toEntity(String.class);
			log.info("HTTP response received response: {}", response);
			return response; // 2xx
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {// 4xx or 5xx
            log.error("Client error occurred while making HTTP call: {}", e.getMessage(), e);
            
            // if gateway timeout, then throw TrustlyProviderException with specific error code and message
			if (e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT 
					|| e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
				log.error("Gateway timeout or service unavailable error occurred: {}", 
						e.getMessage(), e);
				throw new TrustlyProviderException(
						ErrorEnum.UNABLE_TO_CONNECT_TRUSTLY.getErrorCode(), 
						ErrorEnum.UNABLE_TO_CONNECT_TRUSTLY.getErrorMessage(),
						HttpStatus.SERVICE_UNAVAILABLE);
			}
            
            // return ResponseEntity Object, 
            //the error response will have a json response object, 
            //set that as body of ResponseEntity. 
            //And whatever HttpStatus code comes, return that in ResponseEntity.
			return ResponseEntity
					.status(e.getStatusCode())
					.body(e.getResponseBodyAsString());
            
		} catch (Exception e) {
			log.error("Error occurred while making HTTP call: {}", e.getMessage(), e);
			
			throw new TrustlyProviderException(
					ErrorEnum.UNABLE_TO_CONNECT_TRUSTLY.getErrorCode(), 
					ErrorEnum.UNABLE_TO_CONNECT_TRUSTLY.getErrorMessage(),
					HttpStatus.SERVICE_UNAVAILABLE);
			
		}
		
	}
}
