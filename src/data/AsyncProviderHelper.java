package data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.ReflectionUtils;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncProviderHelper extends AsyncTask<Integer, Void, Object> {
	private final static int GET = 1;
	private final static int PUT = 2;
	private final static int POST = 3;
	private final static int DELETE = 4;
	
	private final static String TAG = "AsyncHandler";
	
	private Set<OnDataReadyListener> 		listeners = new HashSet<OnDataReadyListener>();
	
	private ResourceProvider		 		provider;
	
	private int 							operation;
	private int[]							parameters = new int[0];
	private Resource						resource;
	
	public static AsyncProviderHelper instanceFor(ResourceProvider<? extends Resource> provider, OnDataReadyListener listener) {
		return new AsyncProviderHelper(provider, listener);
	}
	
	public AsyncProviderHelper(ResourceProvider<? extends Resource> provider, OnDataReadyListener listener) {
		this.provider = provider;
		listeners.add(listener);
	}
	
	@Override
	protected Object doInBackground(Integer... params) {
		// for sanity, if someone pass us a
		// Integer array or direct parameters...
		if (params.length > 0) {
			parameters = new int[params.length]; int i=0;
		    for (Integer integer : params) {
		        parameters[i] = integer.intValue(); i++;
		    }
		}

		//Log.d(TAG, "resourcePath = " + provider.getResourcePath(parameters));
		Log.d(TAG, "class = " + provider.getClass().getSimpleName());
		Log.d(TAG, "operation = " + operation);
		Log.d(TAG, "parameters = " + Arrays.toString(parameters));
		 
		switch (operation) {
		
		case GET:
			return provider.get(parameters);
			
		case PUT:
			return provider.put(resource);
			
		case POST:
			return provider.post(resource);
			
		case DELETE:
			if (parameters.length > 0) {
				return provider.delete(parameters);
			} else if (resource != null ){
				return provider.delete(resource);
			}
			break;
			
		}
		
		return null;
	}
	
	public void doGet(int... params) {
		operation = GET;
		this.parameters = params;
		execute();
	}
	
	public void doPost(Resource resource) {
		operation = POST;
		this.resource= resource;
		execute();
	}
	
	public void doPut(Resource resource) {
		operation = PUT;
		this.resource= resource; 
		execute();
	}
	
	public void doDelete(int... params) {
		operation = DELETE;
		this.parameters = params;
		execute();
	}
	
	public void doDelete(Resource resource) {
		operation = DELETE;
		this.resource= resource;
		execute();
	}
	
	@Override
	protected void onPostExecute(Object result) {
		
		for (OnDataReadyListener target : listeners) {
			if (target != null) {
				target.onDataReady(result);
			}
			
		}
	}	

}
