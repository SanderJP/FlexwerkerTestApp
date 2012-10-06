package nl.hr.jr4.minor.flexwerk.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class LogEngine {

	public static final String TAG = "FlexwerkerTest";
	public static final String logFileName = "flexwerkerTestLog.txt";
	public static final String logFilePath = "flexwerkerTest";
	private static LogEngine _le;
	private File logFile;
	private ContextHolder ch;
	
	public static LogEngine getInstance(){
		if(_le == null){
			_le = new LogEngine();
		}
		return _le;
	}
	
	public LogEngine() {
		// get the path
		File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + logFilePath);
		// make the dirs if they don't exist
        dir.mkdirs();
        // get the file
        logFile = new File(dir, logFileName);
        // get context
        ch = ContextHolder.getInstance();
	}
	
	public void log(String tag, String msg) {
		FileOutputStream fos;
		Date date = new Date();
		String logRow = new String((date.getTime()/1000) + "," + tag + "," + msg + "\n");
		//Log.w("FlexwerkTest", "Logged: "+logRow);
		byte[] data = logRow.getBytes();
		try {
		    fos = new FileOutputStream(logFile, true);
		    fos.write(data);
		    fos.flush();
		    fos.close();
		} catch (FileNotFoundException e) {
		    Log.w(TAG,"File not found! "+e);
		} catch (IOException e) {
		    Log.w(TAG,"No connection to SD card! (probably) "+e);
		}
	}
	
	/* Simple but effective delete, basically writes "" in the file
	 * !! USE WITH CARE !!
	 * */
	public void deleteLog() {
		FileOutputStream fos;
		String s = new String("");
		byte[] data = s.getBytes();
		try {
		    fos = new FileOutputStream(logFile);
		    fos.write(data);
		    fos.flush();
		    fos.close();
		    Log.w(TAG,"Log deleted");
		} catch (FileNotFoundException e) {
		    Log.w(TAG,"File not found! "+e);
		} catch (IOException e) {
		    Log.w(TAG,"No connection to SD card! (probably) "+e);
		}
	}
	
	public void displayLog() {
		Intent i = new Intent(ch.getContext(), DisplayLog.class);
		ch.getContext().startActivity(i);
	}
}
