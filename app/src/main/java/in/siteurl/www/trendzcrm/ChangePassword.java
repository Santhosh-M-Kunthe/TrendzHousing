package in.siteurl.www.trendzcrm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toolbar;

import dmax.dialog.SpotsDialog;

public class ChangePassword extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Dialog alertDialog;

    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    AlertDialog progressA;
    TextInputLayout cati, npti1, npti2;
    TextInputEditText np1, np2, cp;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.non_chng_pswd_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.homemenu:
                Intent intent = new Intent(ChangePassword.this, Home.class);
               /* SharedPreferences preferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                intent.putExtra("response",preferences.getString("responseatoz",null));
               */
                startActivity(intent);
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(ChangePassword.this, Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressA = new SpotsDialog(ChangePassword.this, "Changing your password. . .");
        alertDialog = new Dialog(this);
        checkConnection();


    /*    ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
*/

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
/*
        Toast.makeText(Logout.this, prefs.getString("sid","you got nothing"), Toast.LENGTH_LONG).show();
        Toast.makeText(Logout.this, prefs.getString("customer_id","you got nothing"), Toast.LENGTH_LONG).show();
*/

        cp = findViewById(R.id.curpswd);
        np1 = findViewById(R.id.newPswd1);
        np2 = findViewById(R.id.newPswd2);

        cati = findViewById(R.id.ti_oldpassword);
        npti2 = findViewById(R.id.ti_conformpassword);
        npti1 = findViewById(R.id.ti_newpassword);
        FloatingActionButton techsup1 = findViewById(R.id.techsupporta);
        techsup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to tech support
                // Snackbar.make(view,"Kindly leave your message. . . Our tech support team will get in touch with you soon. . .",Snackbar.LENGTH_LONG).show();
                Intent goToTechSuppo = new Intent(getApplicationContext(), TechSupport.class);
                startActivity(goToTechSuppo);

            }
        });
    }


    //check connection
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
        showSnack(isConnected);
    }

    ConnectivityReceiver mNetworkReceiver = new ConnectivityReceiver();


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        VendorApplication.getInstance().setConnectivityListener(this);

    }


    // change password API
    private void changePswd() {
        StringRequest changePswdReqst = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/changepassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressA.dismiss();
                        try {
                            JSONObject chngpswdrspns = new JSONObject(response);
                            if (response.toString().contains("ged succ")) {
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                                    builder.setMessage((new JSONObject(response)).getString("Message").toString());

                                    // inflate view to alert dialog
                                    View vie = LayoutInflater.from(ChangePassword.this).inflate(R.layout.alertokay, null);
                                    ImageView wokay = vie.findViewById(R.id.wokay);
                                    wokay.setBackgroundResource(R.drawable.wokaytrendz);
                                    AnimationDrawable frameAnimation = (AnimationDrawable) wokay.getBackground();
                                    frameAnimation.start();//-=-=====----=-=-=_+_+-+-+-=_+-=_=_+-=_+-=_=-=_=_+-+_=_=_+-+-+-=_=_+_+-=_=-+_=_=_+-=_
                                    builder.setView(vie);


                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                           /* cati.setError(null);
                                            npti1.setError(null);
                                            npti2.setError(null);
                                          */
                                            startActivity((new Intent(ChangePassword.this, Logout.class)));
                                        }
                                    });
                                    builder.show();
                                    //passwordED.setError((new JSONObject(response)).getString("Message").toString());
                                    //loginED.setText("");
                                    // Toast.makeText(Login.this, (new JSONObject(response)).getString("Message").toString(), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (response.toString().contentEquals("Login to change your password")) {
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                                    builder.setMessage((new JSONObject(response)).getString("Message").toString());

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startActivity((new Intent(ChangePassword.this, Logout.class)));

                                        }
                                    });
                                    builder.setCancelable(false);
                                    builder.show();
                                    //passwordED.setError((new JSONObject(response)).getString("Message").toString());
                                    //loginED.setText("");
                                    // Toast.makeText(Login.this, (new JSONObject(response)).getString("Message").toString(), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                cati.setError(chngpswdrspns.getString("Message").toString());
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                                builder.setMessage(chngpswdrspns.getString("Message").toString());
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                    }
                                });
                                builder.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // handle volley errors
                progressA.dismiss();
                if (error.networkResponse != null) {
                    parseVolleyError(error);
                }
                if (error instanceof ServerError) {
                    Toast.makeText(ChangePassword.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                    Log.d("Error", String.valueOf(error instanceof ServerError));
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(ChangePassword.this, "Authentication Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Authentication Error");
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    Toast.makeText(ChangePassword.this, "Parse Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Parse Error");
                    error.printStackTrace();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ChangePassword.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "No Connection Error");
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ChangePassword.this, "Please check your connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Network Error");
                    error.printStackTrace();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ChangePassword.this, "Timeout Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Timeout Error");
                    error.printStackTrace();
                } else {
                    Toast.makeText(ChangePassword.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                HashMap<String, String> params = new HashMap<>();
                params.put("customer_id", prefs.getString("customer_id", "you got nothing"));
                params.put("sid", prefs.getString("sid", "you got nothing"));
                params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                params.put("currentpassword", cp.getText().toString());
                if (np1.getText().toString().contains(np2.getText().toString()))
                    params.put("password", np1.getText().toString());
                Log.d("q23", params.toString());
                return params;
            }
        };
        changePswdReqst.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingleTon.getInnstance(ChangePassword.this).addREquest(changePswdReqst);

    }

    //check and change password
    public void changePswdq(View view) {
        npti1.setError(null);
        npti2.setError(null);
        cati.setError(null);
        if (np1.getText().toString().contentEquals(np2.getText().toString()) && (np1.getText().length() > 3) && (cp.getText().length() > 3)) {
            progressA.show();
            if (checkConnection())
                changePswd();
        } else if ((cp.getText().length() < 3)) {
            cati.setError("Enter minimum 3 characters");
        } else if (!(np1.getText().toString().contentEquals(np2.getText().toString()))) {
            np1.setText("");
            np2.setText("");
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
            builder.setMessage("New password and confirm password didn't match");
            builder.setTitle("Password");
            builder.setIcon(R.drawable.header);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            builder.show();
        } else if (np1.getText().length() < 3)
            npti1.setError("Enter minimum 3 characters");
        else if (np2.getText().length() < 3)
            npti2.setError("Enter minimum 3 characters");
        else {
            np1.setText("");
            np2.setText("");
            npti1.setError("Enter valid detail above");
            npti2.setError("Enter valid detail above");
        }
        //changePswd();
    }

    //Handling Volley Error
    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message = data.getString("Message");
            //Toast.makeText(ChangePassword.this, message, Toast.LENGTH_LONG).show();
            android.app.AlertDialog.Builder loginErrorBuilder = new android.app.AlertDialog.Builder(ChangePassword.this);
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

}
