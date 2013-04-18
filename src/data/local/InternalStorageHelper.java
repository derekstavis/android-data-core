package data.local;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Context;
import android.util.Log;
import ContextedApplication;
import data.Resource;

public class InternalStorageHelper {
	private static final String TAG = "InternalStorageHelper";
	private Context context = ContextedApplication.getRootContext();
	
	public InternalStorageHelper() {}

	public static InternalStorageHelper getInstance() {
		return new InternalStorageHelper();
	}
	
	public List<? extends Resource> getStore(String filename, Class<? extends Resource[]> klass) {
		
		byte[] bytes;
		FileInputStream fis;
		ObjectMapper mapper;
		Resource[] objects = null;
		
		try {
			fis = context.openFileInput(filename.replace(File.separator, "_"));
            Log.d(TAG, "Got the Store FileInput " + fis);
			int length = fis.available();
			
			if (length > 0) {
				bytes = new byte[length];
				fis.read(bytes);
				fis.close();
				
				
				mapper = new ObjectMapper();
				mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
				
				objects = mapper.readValue(bytes, klass);
			} else {
                return new ArrayList<Resource>();
			}
			
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
            return new ArrayList<Resource>();
		} catch (IOException e) {
			//e.printStackTrace();
            return new ArrayList<Resource>();
		}
		
		if (objects != null) {
			return new ArrayList<Resource>(Arrays.asList(objects));
		}
		
		return null;
	}
	
	public void saveRawStore(String filename, byte[] bytes) throws IOException {

        String file = filename.replace(File.separator, "_");
        FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);
        fos.write(bytes);
        fos.close();

	}
	
	public void saveStore(List<? extends Resource> list, String filename) throws FileNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
		
		try {
			byte[] bytes = mapper.writeValueAsBytes(list);
			
			saveRawStore(filename, bytes);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public Date getStoreLastModification(String resourcePath) {
		File file;
		
		try {
			file = new File(context.getFilesDir().toString() + "/" + resourcePath);
			return new Date(file.lastModified());
		} catch (Exception e) {
			
		}
		
		return null;
	}
	
}
