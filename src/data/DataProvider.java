package data;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ContextedApplication;
import data.local.LocalInstrutorResourceProviderImpl;
import data.local.LocalPersonResourceProviderImpl;
import data.remote.RemoteInstrutorResourceProviderImpl;
import data.remote.RemoteLessonRatingResourceProviderImpl;
import data.remote.RemoteLessonResourceProviderImpl;
import data.remote.RemotePersonResourceProviderImpl;


public class DataProvider {
	public static enum Method {
		GET,
		POST,
		PUT,
		DELETE
	}

	private static final String baseURL = "http://127.0.0.1:3000";
	private final ConnectivityManager cm = (ConnectivityManager) Application.getRootContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	public static final String RESPONSE_METHOD = "KEY_METHOD";
	public static final String RESPONSE_STATUS_CODE = "KEY_ERROR_CODE";
	public static final String RESPONSE_ENTITY = "KEY_ENTITY";

	public static DataProvider getInstance() {
		return new DataProvider();
	}
	
	public DataProvider() {}
	
	public AsyncProviderHelper personFor(OnDataReadyListener target) {
		ResourceProvider provider;
		
		if (isConnectivityOk()) {
			provider = new RemotePersonResourceProviderImpl(baseURL);
		} else {
			provider = LocalPersonResourceProviderImpl.getInstance();
		}
		
		return AsyncProviderHelper.instanceFor(provider, target);
	}
	
	public boolean isConnectivityOk() {
		NetworkInfo network = cm.getActiveNetworkInfo();
		return (network != null && network.isConnected());
	}
	
	public String getBaseURL() {
		return baseURL;
	}
	
}