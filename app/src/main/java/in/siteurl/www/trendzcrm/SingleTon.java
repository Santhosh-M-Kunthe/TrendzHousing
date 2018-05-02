package in.siteurl.www.trendzcrm;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by SiteURL on 2/9/2018.
 */

public class SingleTon {

    private Context context;
    private RequestQueue requestQueue;
    private static SingleTon singleTonForVendors;

    private SingleTon(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized SingleTon getInnstance(Context context) {
        if (singleTonForVendors == null) {
            singleTonForVendors = new SingleTon(context);
        }
        return singleTonForVendors;
    }

    public void addREquest(Request<String> request) {
        requestQueue.add(request);
    }
}
