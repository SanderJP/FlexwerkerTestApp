package nl.hr.jr4.minor.flexwerk.test;

import java.util.Iterator;
import java.util.Set;

import com.qubulus.qps.Intents;
import com.qubulus.qps.PositioningManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class Test2 extends Activity {
	
	IntentFilter intentFilter = new IntentFilter();
	
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
        	
        	//Bundle extras = intent.getExtras();
        	//Double geoLong =  extras.getDouble("Intents.Position.Extras.LATITUDE");
        	//Log.w("QPS", ""+extras.keySet().toString()); 
        	
        	Bundle extras = getIntent().getExtras();
        	Set<String> ks = extras.keySet();
        	Iterator<String> iterator = ks.iterator();
        	while (iterator.hasNext()) {
        	    Log.d("KEY", iterator.next());
        	}
        }
      };

	@Override
	protected void onPause() {
		
		stopQps();
		
		super.onPause();
	}

	@Override
	protected void onStop() {
		
		stopQps();
		
		super.onStop();
	}
      
     
	private void stopQps(){
		this.unregisterReceiver(mQpsReceiver);

	     PositioningManager manager = PositioningManager.get(this);
	     manager.stopService();
	}

   
}