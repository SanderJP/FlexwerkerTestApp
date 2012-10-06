package nl.hr.jr4.minor.flexwerk.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Test1 extends Activity implements OnClickListener {

	private int _timer = 20;
	private int _threshold = 4;
	private int _counter = 10;
	
	private int _currentCounter = 0;
	private boolean _isCountingDown = false;

	private TextView currentStatus;
	private TextView currentAccel;
	
	private TextView currentInterval;
	private TextView currentThreshold;
	private TextView currentCounter;
	
	private CountDownTimer countDown;
	
	private String statusAvailable = "Beschikbaar";
	private String statusUnavailable = "Niet beschikbaar";
	
	private SensorManager mSensorManager;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;
	
	private MediaPlayer mp;
	
	private LogEngine _le;
	private String _logFile = "test3.txt";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test1);
        
        ContextHolder ch = ContextHolder.getInstance();
        ch.setContext(this);
        
        //*
        _le = LogEngine.getInstance();
        //le.log("Test1", "Dit is de test!", "test1.log");
        //le.deleteLog("flexwerkerTestLog.txt");
        //*/
        
        Button intervalSubmitBtn = (Button) findViewById(R.id.submitInputBtn);
        intervalSubmitBtn.setOnClickListener(this);
		
        currentStatus = (TextView) findViewById(R.id.currentStatusTextView);
        currentStatus.setText(getString(R.string.currentStatusString) + " " + statusAvailable);
        currentAccel = (TextView) findViewById(R.id.currentAccelTextView);
        
        currentInterval = (TextView) findViewById(R.id.currentIntervalTextView);
		currentInterval.setText(getString(R.string.currentIntervalString) + " " + _timer);
		currentThreshold = (TextView) findViewById(R.id.currentThresholdTextView);
		currentThreshold.setText(getString(R.string.currentThresholdString) + " " + _threshold);
		currentCounter = (TextView) findViewById(R.id.currentCounterTextView);
		currentCounter.setText(getString(R.string.currentCounterString) + " " + _counter);

		// getting sensor service
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// setting up listener
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		// set default accelerations
		mAccel = 0.0f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
        
    }

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitInputBtn :
			String toastMsg = "";
			EditText intervalInput = (EditText) findViewById(R.id.intervalEditText);
			String intervalInputString = intervalInput.getText().toString();
			EditText thresholdInput = (EditText) findViewById(R.id.thresholdEditText);
			String thresholdInputString = thresholdInput.getText().toString();
			EditText counterInput = (EditText) findViewById(R.id.counterEditText);
			String counterInputString = counterInput.getText().toString();
			if (!intervalInputString.equals("")) {
				_timer = Integer.parseInt(intervalInputString);
				currentInterval.setText(getString(R.string.currentIntervalString) + _timer);
				toastMsg += "Timer";
			}
			if (!thresholdInputString.equals("")) {
				_threshold = Integer.parseInt(thresholdInputString);
				currentThreshold.setText(getString(R.string.currentThresholdString) + _threshold);
				toastMsg += (toastMsg.equals("")) ? "Threshold" : ", threshold";
			}
			if (!counterInputString.equals("")) {
				_counter = Integer.parseInt(counterInputString);
				currentCounter.setText(getString(R.string.currentCounterString) + _counter);
				toastMsg += (toastMsg.equals("")) ? "Counter" : ", counter";
			}
			if (toastMsg.equals("")) {
				toastMsg = "Niks opgeslagen";
			} else {
				toastMsg += " opgeslagen";
			}
			ToastSingleton.makeToast(this, toastMsg);
		}
	}

	// the sensor event listener
	private final SensorEventListener mSensorListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent se) {
			// getting sensor values
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			// saving this acceleration for later
			mAccelLast = mAccelCurrent;
			// square root to get the current acceleration
			mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
			// delta = difference between current and last accelerations
			float delta = mAccelCurrent - mAccelLast;
			// perform low-cut filter
			mAccel = mAccel * 0.9f + delta;
	        
	        // make all numbers positive so we can compare with one value
	        if (mAccel < 0) {
	        	mAccel *= -1;
	        }
	        
	        // update acceleration textview
	        currentAccel.setText(getString(R.string.currentAccelString) + " " + mAccel);
			
	        //_le.log("accel", String.valueOf(mAccel), _logFile);
	        
	        // if threshold is passed
			if (mAccel > _threshold) {
				// count
				_currentCounter++;
				// start timer if it's not counting down already
				if (!_isCountingDown) {
					startTimer();
				}
			}
			currentCounter.setText(getString(R.string.currentCounterString) + " " + _counter + ", huidige counter: " + _currentCounter);
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
	private void startTimer() {
		_isCountingDown = true;
    	int playtime = _timer*1000;
    	countDown = new CountDownTimer(playtime, 1000) {

    	     public void onTick(long millisUntilFinished) {
    	     }

    	     public void onFinish() {
    	    	 _isCountingDown = false;
    	    	 updateStatus();
    	     }
    	     public void cancelTimer() {
    	    	 _isCountingDown = false;
    	    	 this.cancel();
    	     }
    	  }.start();
	}
	
	public void updateStatus() {
		if (_currentCounter >= _counter) {
	        currentStatus.setText(getString(R.string.currentStatusString) + " " + statusUnavailable);
	        startTimer();
		} else {
	        currentStatus.setText(getString(R.string.currentStatusString) + " " + statusAvailable);
		}
		_currentCounter = 0;
		if (mp != null && mp.isPlaying()) {
			mp.stop();
		}
		new Thread(){
            public void run(){
            	ContextHolder ch = ContextHolder.getInstance();
            	Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				mp = MediaPlayer.create(ch.getContext(), notify);   
				mp.start();
            }
       }.start();
	}
	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mSensorListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(mSensorListener);
		super.onStop();
	}
}
