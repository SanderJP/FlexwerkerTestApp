package nl.hr.jr4.minor.flexwerk.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FlexwerkerTestActivity extends Activity implements OnClickListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
			case R.id.btn_test1 :
				i = new Intent(this, Test1.class);
				startActivity(i);
			break;
			case R.id.btn_test2 :
				i = new Intent(this, Test2.class);
				startActivity(i);
			break;
		}
	}
}