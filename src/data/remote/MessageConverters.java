package data.remote;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MessageConverters {
	
	public static MappingJackson2HttpMessageConverter getConverter(boolean withAnnotations) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		
		converter.getObjectMapper()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(MapperFeature.USE_ANNOTATIONS, withAnnotations);
		
		return converter;
	}
	
	public static MappingJackson2HttpMessageConverter getSerializationConverter() {
				
		return getConverter(true);
	}
	
	public static MappingJackson2HttpMessageConverter getDeserializationConverter() {
				
		return getConverter(false);
	}
}
