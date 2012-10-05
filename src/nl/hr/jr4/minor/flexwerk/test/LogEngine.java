package nl.hr.jr4.minor.flexwerk.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;

public class LogEngine {
	
	private String logFile = "log1.txt";
	private File file;
	
	public LogEngine() {
		file = new File(Environment.getExternalStorageDirectory(), logFile);
	}
	
	public void log(String tag, String msg) {
		FileOutputStream fos;
		Date date = new Date();
		String logRow = new String((date.getTime()/1000) + "," + tag + "," + msg + "\n");
		byte[] data = logRow.getBytes();
		try {
		    fos = new FileOutputStream(file);
		    fos.write(data);
		    fos.flush();
		    fos.close();
		} catch (FileNotFoundException e) {
		    // handle exception
		} catch (IOException e) {
		    // handle exception
		}
	}
}
