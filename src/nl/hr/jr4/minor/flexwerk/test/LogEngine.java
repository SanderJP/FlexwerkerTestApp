package nl.hr.jr4.minor.flexwerk.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class LogEngine {

	public static final String TAG = "FlexwerkerTest";
	public static final String GENERAL_LOG = "general.log";
	public static final String logFilePath = "flexwerkerTest";
	private static LogEngine _le;
	private File _logFile;
	private ContextHolder _ch;
	
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
        _logFile = getLogFile(GENERAL_LOG);
        // get context
        _ch = ContextHolder.getInstance();
	}
	
	private File getLogFile(String fileName) {
		// get the path
		File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + logFilePath);
		// make the dirs if they don't exist
        dir.mkdirs();
        // get the file
        File file = new File(dir, fileName);
		return file;
	}
	
	public void log(String tag, String msg, String logFile) {
		File log = (logFile.equals("")) ? getLogFile(GENERAL_LOG) : getLogFile(logFile);
		Log.w(TAG,""+log);
		Toast.makeText(_ch.getContext(), ""+msg, 500).show();
		FileOutputStream fos;
		Date date = new Date();
		String logRow = new String((date.getTime()/100) + "," + tag + "," + msg + "\n");
		//Log.w("FlexwerkTest", "Logged: "+logRow);
		byte[] data = logRow.getBytes();
		try {
		    fos = new FileOutputStream(log, true);
		    fos.write(data);
		    fos.flush();
		    fos.close();
		} catch (FileNotFoundException e) {
		    Log.w(TAG,"File not found! "+e);
		} catch (IOException e) {
		    Log.w(TAG,"No connection to SD card! (probably) "+e);
		}
	}
	
	/* Deletes a logfile, no questions asked ..
	 * !! USE WITH CARE !!
	 * */
	public Boolean deleteLog(String logFile) {
		// if not empty
		if (!logFile.equals("")) {
			File f = getLogFile(logFile);
			// if file exists
			if (f.exists()) {
				ToastSingleton.makeToast(_ch.getContext(), "Logfile "+logFile+" is deleted");
				return f.delete();
			} else {
				ToastSingleton.makeToast(_ch.getContext(), "Logfile "+logFile+" does not exist");
			}
		} else {
			ToastSingleton.makeToast(_ch.getContext(), "No log file specified");
		}
		return false;
	}
	
	public void displayLog(String logFile) {
		File f = getLogFile(logFile);
		if (f.exists()) {
			Intent i = new Intent(_ch.getContext(), DisplayLog.class);
			i.putExtra("logFile", logFile);
			_ch.getContext().startActivity(i);
		}
	}
}
