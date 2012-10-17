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
	private int _timer2 = 60;
	private int _threshold = 4;
	private int _counter = 10;
	
	private int _currentCounter = 0;
	private boolean _isCountingDown = false;

	private TextView currentStatus;
	private TextView currentAccel;
	
	private TextView currentInterval;
	private TextView secondTimer;
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
	private String _logFile = "test1_17-okt-2012_01.txt";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test1);
        
        ContextHolder ch = ContextHolder.getInstance();
        ch.setContext(this);
        
        //*
        _le = LogEngine.getInstance();
        _le.log("currentTimer", ""+_timer, _logFile);
        _le.log("currentTimer2", ""+_timer2, _logFile);
        _le.log("currentThreshold", ""+_threshold, _logFile);
        _le.log("currentCounter", ""+_counter, _logFile);
        //_le.log("Test1", "Dit is de test!", _logFile);
        //le.deleteLog("flexwerkerTestLog.txt");
        //*/
        
        Button intervalSubmitBtn = (Button) findViewById(R.id.submitInputBtn);
        intervalSubmitBtn.setOnClickListener(this);
        
        Button startWalkingBtn = (Button) findViewById(R.id.startWalkingBtn);
        startWalkingBtn.setOnClickListener(this);
        Button stopWalkingBtn = (Button) findViewById(R.id.stopWalkingBtn);
        stopWalkingBtn.setOnClickListener(this);
        Button doSomethingBtn = (Button) findViewById(R.id.doSomethingBtn);
        doSomethingBtn.setOnClickListener(this);
		
        currentStatus = (TextView) findViewById(R.id.currentStatusTextView);
        currentStatus.setText(getString(R.string.currentStatusString) + " " + statusAvailable);
        currentAccel = (TextView) findViewById(R.id.currentAccelTextView);
        
        currentInterval = (TextView) findViewById(R.id.currentIntervalTextView);
		currentInterval.setText(getString(R.string.currentIntervalString) + " " + _timer);
        secondTimer = (TextView) findViewById(R.id.currentSecondTimerTextView);
		secondTimer.setText(getString(R.string.currentSecondTimerString) + " " + _timer2);
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
			EditText secondTimerInput = (EditText) findViewById(R.id.secondTimerEditText);
			String secondTimerInputString = intervalInput.getText().toString();
			EditText thresholdInput = (EditText) findViewById(R.id.thresholdEditText);
			String thresholdInputString = thresholdInput.getText().toString();
			EditText counterInput = (EditText) findViewById(R.id.counterEditText);
			String counterInputString = counterInput.getText().toString();
			if (!intervalInputString.equals("")) {
				_timer = Integer.parseInt(intervalInputString);
				currentInterval.setText(getString(R.string.currentIntervalString) + _timer);
				toastMsg += "Timer";
				_le.log("value changed", "timer: "+_timer, _logFile);
			}
			if (!secondTimerInputString.equals("")) {
				_timer2 = Integer.parseInt(secondTimerInputString);
				secondTimerInput.setText(getString(R.string.secondTimerString) + _timer2);
				toastMsg += (toastMsg.equals("")) ? "Timer2" : ", timer2";
				_le.log("value changed", "timer2: "+_timer2, _logFile);
			}
			if (!thresholdInputString.equals("")) {
				_threshold = Integer.parseInt(thresholdInputString);
				currentThreshold.setText(getString(R.string.currentThresholdString) + _threshold);
				toastMsg += (toastMsg.equals("")) ? "Threshold" : ", threshold";
				_le.log("value changed", "threshold: "+_threshold, _logFile);
			}
			if (!counterInputString.equals("")) {
				_counter = Integer.parseInt(counterInputString);
				currentCounter.setText(getString(R.string.currentCounterString) + _counter);
				toastMsg += (toastMsg.equals("")) ? "Counter" : ", counter";
				_le.log("value changed", "counter: "+_counter, _logFile);
			}
			if (toastMsg.equals("")) {
				toastMsg = "Niks opgeslagen";
			} else {
				toastMsg += " opgeslagen";
			}
			ToastSingleton.makeToast(this, toastMsg);
			break;
			case R.id.startWalkingBtn :
				_le.log("action", "start walking", _logFile);
				ToastSingleton.makeToast(this, "Start walking");
			break;
			case R.id.stopWalkingBtn :
				_le.log("action", "stop walking", _logFile);
				ToastSingleton.makeToast(this, "Stop walking");
			break;
			case R.id.doSomethingBtn :
				_le.log("action", "do something", _logFile);
				ToastSingleton.makeToast(this, "Do something");
			break;
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
			
	        _le.log("accel", String.valueOf(mAccel), _logFile);
	        
	        // if threshold is passed
			if (mAccel > _threshold) {
				// count
				_currentCounter++;
		        _le.log("currentCounter", ""+_currentCounter, _logFile);
				// start timer if it's not counting down already
				if (!_isCountingDown) {
					startTimer(_timer);
				}
			}
			currentCounter.setText(getString(R.string.currentCounterString) + " " + _counter + ", huidige counter: " + _currentCounter);
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
	private void startTimer(int time) {
        _le.log("timer", "timer started ("+time+")", _logFile);
		_isCountingDown = true;
    	int playtime = time*1000;
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
	        _le.log("status", "unavailable", _logFile);
	        currentStatus.setText(getString(R.string.currentStatusString) + " " + statusUnavailable);
	        startTimer(_timer2);
		} else {
	        _le.log("status", "available", _logFile);
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
