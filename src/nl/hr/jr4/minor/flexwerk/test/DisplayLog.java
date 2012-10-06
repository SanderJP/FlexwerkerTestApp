package nl.hr.jr4.minor.flexwerk.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayLog extends Activity {
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_log);
        
        String logFile = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            logFile = extras.getString("logFile");
        }
        
        if (!logFile.equals("")) {
	        // get listview
	        ListView logListView = (ListView) findViewById(R.id.listView_log);
	        // get logfile
	        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LogEngine.logFilePath + "/" + logFile);
	        // convert to ArrayList
			ArrayList<String> logs = new ArrayList<String>();
	        if(file.exists()) {
	        	String readLine = null;
	            try {
	            	FileInputStream fIn = new FileInputStream(file);
	            	BufferedReader br = new BufferedReader(new InputStreamReader(fIn));
	                while ((readLine = br.readLine()) != null) {
	                	logs.add(readLine);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	        	Log.w(LogEngine.TAG, "Cannot find file at: " + LogEngine.logFilePath + "/" + logFile);
	        }
	        // attach ArrayAdapter to the listview
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logs);
			logListView.setAdapter(adapter);
        } else {
        	ContextHolder ch = ContextHolder.getInstance();
        	Toast.makeText(ch.getContext(), "No log file specified", Toast.LENGTH_LONG);
        }
    }
}
