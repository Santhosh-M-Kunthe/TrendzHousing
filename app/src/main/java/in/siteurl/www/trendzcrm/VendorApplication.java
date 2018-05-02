package in.siteurl.www.trendzcrm;

import android.app.Application;
import android.content.Context;
import android.os.Build;

/**
 * Created by siteurl on 9/1/18.
 */

public class VendorApplication extends Application {
    private static VendorApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static synchronized VendorApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}
