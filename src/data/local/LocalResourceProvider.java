package data.local;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import data.DataProvider;
import data.Resource;
import data.ResourceProvider;

public abstract class LocalResourceProvider<T extends Resource> extends ResourceProvider<T> {
	protected InternalStorageHelper sh;
	
	public LocalResourceProvider(String baseURL) {
		super(baseURL);
	}

	public void setStorageHelper(InternalStorageHelper helper) {
		sh = helper;
	}
	
	protected InternalStorageHelper getStorageHelper() {
		return sh;
	}

	public Bundle put(T res) {
		List<T> entities = (List<T>) get();
		T local = null;
		for (T entity : entities) {
			if (entity.id() == res.id()) {
				local = entity;
			}
		}
		
		if (local != null) {
			local = res;
		} else {
			return getStatusBundle(DataProvider.Method.PUT, 404);
		}

        try {
            sh.saveStore(entities, getResourcePath(res.id()));
        } catch (FileNotFoundException e) {
            return getStatusBundle(DataProvider.Method.PUT, 404);
        }
        return getStatusBundle(DataProvider.Method.PUT, 200);
	}
	
	public Bundle post(T res) {
        Log.d(TAG, "post(" + res.getClass().getSimpleName() + ")");
        Object object = get();

        Log.d(TAG, "object from get() = " + object);

        if (object instanceof List<?>) {
            ArrayList<T> entities = (ArrayList<T>) object;

            for (T entity : entities) {
                if (entity.id() == res.id()) {
                    return getStatusBundle(DataProvider.Method.PUT, 406);
                }
            }

            entities.add(res);

            try {
                sh.saveStore(entities, getResourcePath(res.id()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return getStatusBundle(DataProvider.Method.PUT, 404);
            }

            return getStatusBundle(DataProvider.Method.PUT, 200);

        }

        return (Bundle) object;

	}

	public Bundle delete(T res) {
		
		return delete(res.id());
		
	}

	public Bundle delete(int... args) {
		if (args.length > 0) {
			List<T> entities = (List<T>) get();
			
			for (T entity : entities) {
				if (entity.id() == args[0]) {
					entities.remove(entity);
					return getStatusBundle(DataProvider.Method.PUT, 200);
				}
			}
		}
		
		return getStatusBundle(DataProvider.Method.PUT, 400);
	}
	
}
