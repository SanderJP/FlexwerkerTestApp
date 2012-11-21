package nl.hr.jr4.minor.flexwerk.test;

import java.util.Iterator;
import java.util.Set;

import com.google.android.maps.GeoPoint;
import com.qubulus.qps.Intents;
import com.qubulus.qps.PositioningManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Test2 extends Activity implements SensorEventListener, OnClickListener {
	
	IntentFilter intentFilter = new IntentFilter();
	private LogEngine _le;
	private String _logFile = "test2.txt";
	private PositioningManager manager;
	private Button _btn;
	private Button _nextBtn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        
        manager = PositioningManager.get(this);
        //manager.requestPosition();
        
        _btn = (Button) findViewById(R.id.requestPositionBtn);
        _btn.setOnClickListener(this);
        _nextBtn = (Button) findViewById(R.id.nextPositionBtn);
        _nextBtn.setOnClickListener(this);
        
        intentFilter.addAction(Intents.Position.ACTION);
        intentFilter.addAction(Intents.UnknwownPosition.ACTION);
        this.registerReceiver(mQpsReceiver, intentFilter);
    }
    
    private BroadcastReceiver mQpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	// Implement your logic here (show position on the map for example)
        	
        	Bundle extras = intent.getExtras();
        	Double geoLat =  extras.getDouble("LATITUDE");
        	Double geoLng =  extras.getDouble("LONGITUDE");
        	Log.w("QPS", geoLat + ", " + geoLng);
        	Log.w("Incoming", "Broadcasted");
        	
			
			// Get LogEngine
			_le = LogEngine.getInstance();
			
			// Log Qubulus geocoords
			_le.log("QubuGeo", geoLat + ", " + geoLng, _logFile);
        	
        	/*
        	Bundle extras2 = intent.getExtras();
        	Set<String> ks = extras2.keySet();
        	Iterator<String> iterator = ks.iterator();
        	while (iterator.hasNext()) {
        	    Log.d("KEY", iterator.next());
        	}
        	//*/
        }
      };

	@Override
	protected void onPause() {
		
		stopQps();
		
		super.onPause();
	}

	@Override
	protected void onStop() {
		
		//stopQps();
		
		
		super.onStop();
	}
      
     
	private void stopQps(){

		PositioningManager manager = PositioningManager.get(this);
		manager.stopService();
		this.unregisterReceiver(mQpsReceiver);
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextPositionBtn :
			_le.log("QubuGeo", " ", _logFile);
			Toast.makeText(getBaseContext(), "Nieuwe regel", 150).show();
			break;
		case R.id.requestPositionBtn :
			Toast.makeText(getBaseContext(), "clicked!", 150).show();
			manager.requestPosition();
			break;
		}
	}

   
}