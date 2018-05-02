package in.siteurl.www.trendzcrm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Dialog alertDialog;

    String loginuserid, sessionid, uid;
    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    String storedEmail;
    String storedPassword;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // startActivity(new Intent(Splash.this,UnitAndProjPics.class));


        alertDialog = new Dialog(this);
        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        storedEmail = prefs.getString("emaillogin", null);
        storedPassword = prefs.getString("passwordlogin", null);
        if (storedEmail != null && storedPassword != null && checkConnection())
            gotoToSigninAPI();


        //getting login details in preferences
        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        sessionid = loginpref.getString("sessionid", null);
        uid = loginpref.getString("User-id", null);

        checkConnection();
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (loginpref.contains("User-id")) {
                        loginuserid = loginpref.getString("User-id", null);
                        if (loginuserid.equals("") || loginuserid.equals(null)) {
                            startActivity(new Intent(Splash.this, Home.class));
                        } else {
                            //startActivity(new Intent(Splash.this, MainActivity.class));
                        }
                    } else {
                        if (storedEmail == null && storedPassword == null)
                            startActivity(new Intent(Splash.this, Login.class));
                    }
                }
            }
        };
        timer.start();

    }


    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;

    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        if (isConnected) {
            //Method to signin
            //Signinwithmail(email, password);
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        } else {
            shownointernetdialog();
        }
    }

    //To show no internet dialog
    private void shownointernetdialog() {
        //alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.nointernet);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        Button retry = alertDialog.findViewById(R.id.exit_btn);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //checkConnection();
                System.exit(0);
            }
        });
        alertDialog.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        recreate();
        showSnack(isConnected);

    }

    ConnectivityReceiver mNetworkReceiver = new ConnectivityReceiver();

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        VendorApplication.getInstance().setConnectivityListener(this);
    }


    /*  public void useSignInAPIMethod(View view) {

          //Toast.makeText(Splash.this, prefs.getString("emaillogin",null), Toast.LENGTH_LONG).show();
          //Toast.makeText(Splash.this, prefs.getString("passwordlogin",null), Toast.LENGTH_LONG).show();
          gotoToSigninAPI();
      }
  */
    private void gotoToSigninAPI() {


        //call API
        StringRequest loginAPI = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/userdetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // COMMENTED FOR FUTURE USE
        /*   if (error.networkResponse != null) {
                    parseVolleyError(error);
                }
                if (error instanceof ServerError) {
                    Toast.makeText(Splash.this, "Check your connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", String.valueOf(error instanceof ServerError));
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Splash.this, "Authentication Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Authentication Error");
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Splash.this, "Parse Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Parse Error");
                    error.printStackTrace();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Splash.this, "No internet connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "No Connection Error");
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Splash.this, "Please check your connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Network Error");
                    error.printStackTrace();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Splash.this, "Timeout Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Timeout Error");
                    startActivity(new Intent(Splash.this, Login.class));
                    error.printStackTrace();
                } else {
                    Toast.makeText(Splash.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        */    }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                if (storedEmail == null && storedPassword == null) {
                    startActivity(new Intent(Splash.this, Login.class));
                } else {
                    params.put("sid", prefs.getString("emaillogin", null));
                    params.put("customer_id", prefs.getString("passwordlogin", null));
                    params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");

                }
                return params;
            }
        };

        loginAPI.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingleTon.getInnstance(Splash.this).addREquest(loginAPI);

        /*
        Intent intent=new Intent(Login.this,Home.class);
        startActivity(intent);
        */

    }

    //Handling Volley Error
    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message = data.getString("Message");
            // Toast.makeText(Splash.this, message, Toast.LENGTH_LONG).show();
            android.app.AlertDialog.Builder loginErrorBuilder = new android.app.AlertDialog.Builder(Splash.this);
            loginErrorBuilder.setTitle("Error");
            loginErrorBuilder.setMessage(message);
            loginErrorBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            loginErrorBuilder.show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }

    private void parseJSON(String response) {
        try {
            JSONObject responseWhole = new JSONObject(response);
            responseWhole.put("customer_name", prefs.getString("customer_name", null));
            responseWhole.put("sid", prefs.getString("emaillogin", null));
            responseWhole.put("Role", "customer");
            responseWhole.put("customer_id", prefs.getString("passwordlogin", null));
            String sucessORfailure = responseWhole.getString("Error");
            if (sucessORfailure.contains("false")) {

                // Got to home screen if login credentials are correct
                Intent intent = new Intent(Splash.this, Home.class);
                //intent.putExtra("response",responseWhole.toString());
                writeToFile(responseWhole.toString(), Splash.this, "response");
                startActivity(intent);
            } else if (sucessORfailure.contains("true")) {

                startActivity((new Intent(Splash.this, Login.class)));
                //Toast.makeText(Splash.this, responseWhole.getString("Message").toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void writeToFile(String data, Context context, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context, String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
