package data;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.os.Bundle;
import android.util.Log;
import data.remote.MessageConverters;


public abstract class ResourceProvider<T extends Resource> {
	protected static final String TAG = "ResourceProvider";
	
	private String baseURL = "";
	
	public ResourceProvider(String baseURL) {
		this.baseURL = baseURL;
	}
	
	public String getBaseURL() {
		return baseURL;
	}
	
	public abstract Object get(int... args);
	
	public abstract Bundle put(T res);
	
	public abstract Bundle post(T res);
	
	public abstract Bundle delete(T res);
	
	public abstract Bundle delete(int... args);
	
	public abstract String getResourcePath(int... params);
	
	protected Bundle getStatusBundle(DataProvider.Method method, int... code) {
		Bundle status = new Bundle();
		Class<?> klass = (Class<?>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
		status.putSerializable(DataProvider.RESPONSE_ENTITY, klass);
		status.putSerializable(DataProvider.RESPONSE_METHOD, method);
		if (code.length > 0) {
			status.putInt(DataProvider.RESPONSE_STATUS_CODE, code[0]);
		}
		return status;
	}
}
