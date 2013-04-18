package data.remote;

import java.util.Arrays;

import org.springframework.web.client.RestTemplate;

import android.util.Log;
import data.ResourceProvider;
import model.entity.Person;

public class RemotePersonResourceProviderImpl 
extends RemoteResourceProvider<Person> {
	
	private static final String resourcePath = "Persons";

	public static RemotePersonResourceProviderImpl getInstance(String baseURL) {
		return new RemotePersonResourceProviderImpl(baseURL);
	}
	
	public RemotePersonResourceProviderImpl(String baseURL) {
		super(baseURL);
	}

	public Object get(int... args) {
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(
				MessageConverters.getDeserializationConverter());

		if (args.length > 0) {
			Person Person = restTemplate.getForObject(getResourcePath(args[0]) + ".json",
					Person.class);

			return Person;
		} else {
			Person[] Persons = restTemplate.getForObject(getResourcePath() + ".json",
					Person[].class);

			return Arrays.asList(Persons);
		}
	}
	
	public String getResourcePath(int... params) {
		String path;
		if (params.length > 0) {
			path = String.format("%s/%s/%d", getBaseURL(), resourcePath, params[0]);
		} else {
			path = String.format("%s/%s", getBaseURL(), resourcePath);
		}
		
		Log.d(this.getClass().getName(), "Resource on path: " + path);
		
		return path;
	}

}
