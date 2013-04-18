package data.local;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import data.ResourceProvider;
import model.entity.Person;

public class LocalPersonResourceProviderImpl extends LocalResourceProvider<Person> {
	

	public LocalPersonResourceProviderImpl(String baseURL) {
		super(baseURL);
	}

	public static LocalPersonResourceProviderImpl getInstance() {
		LocalPersonResourceProviderImpl impl = new LocalPersonResourceProviderImpl("/");
		impl.setStorageHelper(InternalStorageHelper.getInstance());
		
		return impl;
	}
	
	public Object get(int... args) {
		List<Person> objects;
		
		if (args.length > 0) {
			objects = (List<Person>) get();
			
			if (objects != null) {
				for (Person element : objects) {
					if (element.id() == args[0]) {
						return element;
					}
				}
			}
			
		} else {
			objects = (List<Person>) sh.getStore(getResourcePath(), Person[].class);
			
			if (objects == null) {
				objects = new ArrayList<Person>();
			}
			
			return objects;
		}
		
		return null;
	}
	
	public String getResourcePath(int... params) {
		
		return "Persons";
	}

}
