package in.siteurl.www.trendzcrm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Logout extends AppCompatActivity {

    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
/*
        Toast.makeText(Logout.this, prefs.getString("sid","you got nothing"), Toast.LENGTH_LONG).show();
        Toast.makeText(Logout.this, prefs.getString("customer_id","you got nothing"), Toast.LENGTH_LONG).show();
*/

        editor.putString("emaillogin", null);
        editor.putString("passwordlogin", null);
        editor.commit();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Logout.this, "Logged out succesfully. . .", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Logout.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("customer_id", prefs.getString("sid", "you got nothing"));
                params.put("sid", prefs.getString("customer_id", "you got nothing"));
                params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");

                return params;
            }
        };
        SingleTon.getInnstance(Logout.this).addREquest(stringRequest);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        startActivity(new Intent(Logout.this, Login.class));
    }
}
