package data.remote;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.os.Bundle;
import android.util.Log;
import data.DataProvider;
import data.Resource;
import data.ResourceProvider;

public abstract class RemoteResourceProvider<T extends Resource> extends ResourceProvider<T> {

	public RemoteResourceProvider(String baseURL) {
		super(baseURL);
	}

	@Override
	public abstract Object get(int... args);
	
	public Bundle put(T res) {
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(
				MessageConverters.getDeserializationConverter());
		restTemplate.getMessageConverters().add(
				new StringHttpMessageConverter());
		
		try {
			restTemplate.put(getResourcePath(res.id()), res);
			
		} catch (HttpClientErrorException http) {
			Log.d("HTTP RESPONSE", "" + http.getStatusText());
			return getStatusBundle(DataProvider.Method.PUT, http.getStatusCode().value());
		} catch (RestClientException e) {
			//e.printStackTrace();
		}
		
		return getStatusBundle(DataProvider.Method.PUT, 200);
		
	}
	
	public Bundle post(T res) {
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(
				MessageConverters.getDeserializationConverter());
		
		try {
			Log.d(TAG, "postForObject ");
			Log.d(TAG, "url = " + getResourcePath());
			Log.d(TAG, res.toString());
			
			restTemplate.postForObject(getResourcePath(), res, String.class);
		} catch (HttpClientErrorException http) {
			Log.d("RESPONSE", "" + http.getStatusCode());
			return getStatusBundle(DataProvider.Method.PUT, http.getStatusCode().value());
		} catch (RestClientException e) {
			//e.printStackTrace();
		} 
		
		return getStatusBundle(DataProvider.Method.PUT, 200);
		
	}
	
	public Bundle delete(T res) {
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(
				MessageConverters.getDeserializationConverter());

		// Make the HTTP GET request, marshaling the response from JSON to an
		// array of Persons
		
		try {
			restTemplate.delete(getResourcePath(res.id()));
		} catch (HttpClientErrorException http) {
			Log.d("RESPONSE", "" + http.getStatusCode());
			return getStatusBundle(DataProvider.Method.PUT, http.getStatusCode().value());
		} catch (RestClientException e) {
			//e.printStackTrace();
		} 
		
		return getStatusBundle(DataProvider.Method.PUT, 200);
		
	}
	
	public Bundle delete(int... args) {
		
		try {
			new RestTemplate().delete(getResourcePath(args));
		} catch (HttpClientErrorException http) {
			Log.d("RESPONSE", "" + http.getStatusCode());
			return getStatusBundle(DataProvider.Method.PUT, http.getStatusCode().value());
		} catch (RestClientException e) {
			//e.printStackTrace();
		}
		
		return getStatusBundle(DataProvider.Method.PUT, 200);
		
	}
	
}
