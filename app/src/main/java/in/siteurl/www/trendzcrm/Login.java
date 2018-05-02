package in.siteurl.www.trendzcrm;

import android.app.AlertDialog;
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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    EditText loginED, passwordED;
    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    String storedEmail;
    ConnectivityReceiver mNetworkReceiver = new ConnectivityReceiver();

    String storedPassword;
    SharedPreferences prefs;
    boolean fromSplashOrNot = false;
    AlertDialog dialog, fpdialog;
    Dialog alertDialog;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        dialog = new SpotsDialog(Login.this, "Signing you in");
        fpdialog = new SpotsDialog(Login.this, "Please wait");

        alertDialog = new Dialog(this);
        checkConnection();
        loginED = findViewById(R.id.edloginemail);
        passwordED = findViewById(R.id.edloginpassword);
        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        storedEmail = prefs.getString("emaillogin", null);
        storedPassword = prefs.getString("passwordlogin", null);
        // if (storedEmail!=null && storedPassword!=null)
        if (checkConnection())
            gotoToSigninAPI();


        FloatingActionButton info = findViewById(R.id.loginquesmark);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // COMMENTED FOR FUTURE USE
                /*Snackbar snack=Snackbar.make(view,"Enter Email and Password sent by Admin",Snackbar.LENGTH_LONG);
                CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                snack.show();*/

                //To show snackbar
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordsumney);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter Email and Password sent by Admin", Snackbar.LENGTH_LONG);
                View viewa = snackbar.getView();

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) viewa.getLayoutParams();
                params.gravity = Gravity.TOP;
                viewa.setLayoutParams(params);
                snackbar.show();

            }
        });
        CheckBox showPassword = findViewById(R.id.checkboxtoshowpassword);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //show or hide password based on on checked
                if (!b)
                    passwordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    passwordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });
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
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        VendorApplication.getInstance().setConnectivityListener(this);

    }

    /// call forgot password API
    public void useForgotPasswordAPIMethod(View view) {
        if (!checkConnection())
            return;
        if (loginED.getText().toString() == null || loginED.getText().toString().length() < 3)
            loginED.setError("Enter an email to get further instructions");

        else if (!(loginED.getText().toString().contains("@") && loginED.getText().toString().contains(".")))
            loginED.setError("Enter valid email for further instructions");
        else {
            fpdialog.show();
            StringRequest changePswd = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/forgot",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                fpdialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setTitle("Login");
                                builder.setMessage((new JSONObject(response)).getString("Message").toString());
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();
                                //passwordED.setError((new JSONObject(response)).getString("Message").toString());
                                //loginED.setText("");
                                // Toast.makeText(Login.this, (new JSONObject(response)).getString("Message").toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    fpdialog.dismiss();
                    if (error.networkResponse != null) {
                        parseVolleyError(error);
                    }
                    if (error instanceof ServerError) {
                        Toast.makeText(Login.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                        Log.d("Error", String.valueOf(error instanceof ServerError));
                        error.printStackTrace();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(Login.this, "Authentication Error", Toast.LENGTH_LONG).show();
                        Log.d("Error", "Authentication Error");
                        error.printStackTrace();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(Login.this, "Parse Error", Toast.LENGTH_LONG).show();
                        Log.d("Error", "Parse Error");
                        error.printStackTrace();
                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(Login.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                        Log.d("Error", "No Connection Error");
                        error.printStackTrace();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(Login.this, "Please check your connection.", Toast.LENGTH_LONG).show();
                        Log.d("Error", "Network Error");
                        error.printStackTrace();
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(Login.this, "Timeout Error", Toast.LENGTH_LONG).show();
                        Log.d("Error", "Timeout Error");
                        error.printStackTrace();
                    } else {
                        Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", loginED.getText().toString());
                    params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                    return params;
                }
            };

            changePswd.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            SingleTon.getInnstance(Login.this).addREquest(changePswd);

        }
    }

    //Handling Volley Error
    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message = data.getString("Message");
            //Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
            android.app.AlertDialog.Builder loginErrorBuilder = new android.app.AlertDialog.Builder(Login.this);
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


    public void useSignInAPIMethod(View view) {

        //dialog.show();
        fromSplashOrNot = true;
        //  Toast.makeText(Login.this, prefs.getString("emaillogin",null), Toast.LENGTH_LONG).show();
        //Toast.makeText(Login.this, prefs.getString("passwordlogin",null), Toast.LENGTH_LONG).show();


        if (!loginED.getText().toString().matches(EMAIL_PATTERN)) {
            loginED.setError("Enter Valid Email");
            return;
        } else if (passwordED.getText().length() < 3) {
            passwordED.setError("Enter Valid Password");
            return;
        } else {
            dialog.show();
            if (checkConnection())
                gotoToSigninAPI();
        }
    }

    // call sign in API
    private void gotoToSigninAPI() {


        //call API
        StringRequest loginAPI = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/customerlogin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                        parseJSON(response);
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                if (error.networkResponse != null) {
                    parseVolleyError(error);
                }
                if (error instanceof ServerError) {
                    Toast.makeText(Login.this, "Check connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", String.valueOf(error instanceof ServerError));
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Login.this, "Authentication Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Authentication Error");
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Login.this, "Parse Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Parse Error");
                    error.printStackTrace();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Login.this, "No connection try again.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "No Connection Error");
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Login.this, "Please check your connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Network Error");
                    error.printStackTrace();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Login.this, "Timeout Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Timeout Error");
                    error.printStackTrace();
                } else {
                    Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //if (storedEmail==null && storedPassword==null)
                {
                    params.put("email", loginED.getText().toString());
                    params.put("password", passwordED.getText().toString());
                    params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                }
              /*  else
              {
                    params.put("email",prefs.getString("emaillogin",null));
                    params.put("password",prefs.getString("passwordlogin",null));
                    params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");

                }*/
                return params;
            }
        };
        loginAPI.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingleTon.getInnstance(Login.this).addREquest(loginAPI);


        /*
        Intent intent=new Intent(Login.this,Home.class);
        startActivity(intent);
        */

    }

    // parse the response of Login API
    private void parseJSON(String response) {
        try {
            JSONObject responseWhole = new JSONObject(response);
            String sucessORfailure = responseWhole.getString("Error");
            if (sucessORfailure.contains("false")) {
                editor.putString("emaillogin", responseWhole.getString("sid"));
                editor.putString("passwordlogin", responseWhole.getString("customer_id"));
                editor.putString("customer_name", responseWhole.getString("customer_name"));
                editor.commit();

                // COMMENTED FOR FUTURE USE
                // Got to home screen if login credentials are correct
               // Intent intent = new Intent(Login.this, Home.class);
                //intent.putExtra("response", response);
                //writeToFile(response, Login.this, "response");
                getUserDetails();
                //startActivity(intent);
            } else if (sucessORfailure.contains("true")) {
                if (fromSplashOrNot) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("Login");
                    builder.setMessage(responseWhole.getString("Message").toString());
                    // builder.setIcon(R.drawable.header);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getUserDetails() {

        //call API
        StringRequest loginAPI = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/userdetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                        Log.d("de1",response);
                        try {
                            JSONObject toAdd=new JSONObject(response);
                            toAdd.put("Role","Customer");
                            toAdd.put("sid",prefs.getString("emaillogin", null));
                            toAdd.put("customer_name",prefs.getString("customer_name", null));
                            writeToFile(toAdd.toString(9),Login.this,"response");
                            startActivity(new Intent(Login.this,Home.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                    params.put("sid", prefs.getString("emaillogin", null));
                    params.put("customer_id", prefs.getString("passwordlogin", null));
                    params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");

                return params;
            }
        };

        loginAPI.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingleTon.getInnstance(Login.this).addREquest(loginAPI);

        /*
        Intent intent=new Intent(Login.this,Home.class);
        startActivity(intent);
        */

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
