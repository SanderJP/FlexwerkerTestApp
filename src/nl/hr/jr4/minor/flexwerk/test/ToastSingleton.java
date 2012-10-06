package nl.hr.jr4.minor.flexwerk.test;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public enum ToastSingleton {
    INSTANCE;    // only element in this enum
 
    public static void makeToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
        //                               toast.getYOffset() / 2);
        toast.show();
    }
}