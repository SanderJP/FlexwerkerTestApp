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

public class Test2 extends Activity implements SensorEventListener{
	
	IntentFilter intentFilter = new IntentFilter();
	private LogEngine _le;
	private String _logFile = "test2.txt";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        
        PositioningManager manager = PositioningManager.get(this);
        manager.requestPosition();
        
        intentFilter.addAction(Intents.Position.ACTION);
        intentFilter.addAction(Intents.UnknwownPosition.ACTION);
        this.registerReceiver(mQpsReceiver, intentFilter);
    }
    
    private BroadcastReceiver mQpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	// Implement your logic here (show position on the map for example)
        	
        	Bundle extras = intent.getExtras();
        	Double geoLat =  extras.getDouble("LONGITTUDE");
        	Double geoLng =  extras.getDouble("LATITUDE");
        	Log.w("QPS Coords", geoLat + ", " + geoLng); 
        	Log.w("Incoming", "Broadcasted");
        	
        	/*
        	// Todo:
        	// Compare Qubulus coords with GPS coords
        	
        	// Get the location manager
    		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    		// Define the criteria how to select the locatioin provider -> use
    		// default
    		Criteria criteria = new Criteria();
    		String provider = locationManager.getBestProvider(criteria, false);
    		Location location = locationManager.getLastKnownLocation(provider);
    		
    		Double gpsLng = null;
			Double gpsLat = null;
    		// Initialize the location fields
    		if (location != null) {
    			System.out.println("Provider " + provider + " has been selected.");
    			gpsLat = (Double) (location.getLatitude() * 1000000);
    			gpsLng = (Double) (location.getLongitude() * 1000000);
    			
    			//GeoPoint gp = new GeoPoint(lat, lng));
    		}
    		
			if(gpsLat == geoLat && gpsLng == geoLng){
    			Log.w("Impossible", "You'll never see this in logcat");
    		}
			
			// Get LogEngine
			_le = LogEngine.getInstance();
			
			// Log Qubulus geocoords and GPS geocoords
			_le.log("QubuGeo", geoLat + ", " + geoLng, _logFile);
			_le.log("QubuGeo", gpsLat + ", " + gpsLng, _logFile);
        	*/
        	
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
		this.unregisterReceiver(mQpsReceiver);

	     PositioningManager manager = PositioningManager.get(this);
	     manager.stopService();
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

   
}