package nl.hr.jr4.minor.flexwerk.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FlexwerkerTestActivity extends Activity implements OnClickListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn1 = (Button) findViewById(R.id.btn_test1);
        btn1.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btn_test2);
        btn2.setOnClickListener(this);

        // Set the current context (this) in the 'singleton' contextHolder.
        ContextHolder ch = ContextHolder.getInstance();
        ch.setContext(this);
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