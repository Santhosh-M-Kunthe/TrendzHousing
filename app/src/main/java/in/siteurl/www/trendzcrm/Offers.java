package in.siteurl.www.trendzcrm;

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
import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Offers extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    SharedPreferences prefs;
    Dialog alertDialog;
    AlertDialog progress;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
                Intent intent = new Intent(Offers.this, Home.class);
               /* SharedPreferences preferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                intent.putExtra("response",preferences.getString("responseatoz",null));
               */
                startActivity(intent);
                return true;
            case R.id.chngpswd:
                Intent intent2 = new Intent(Offers.this, ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(Offers.this, Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        progress = new SpotsDialog(Offers.this, "Getting your offer. . .");
        progress.show();
        //  Toast.makeText(Offers.this, "Image number of image you clicked was", Toast.LENGTH_LONG).show();
        //Toast.makeText(Offers.this,String.valueOf( getIntent().getExtras().getInt("itno")), Toast.LENGTH_SHORT).show();

        // get offer list
        alertDialog = new Dialog(this);
        checkConnection();
        StringRequest getOffersInOffers = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/useralloffers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if ((new JSONObject(response)).getString("Message").toString().contains("Login to access")) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Offers.this);
                                builder.setMessage((new JSONObject(response)).getString("Message").toString());

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        startActivity((new Intent(Offers.this, Logout.class)));

                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();


                            } else {
                                JSONObject Wholeoffer = new JSONObject(response);
                                // Toast.makeText(Home.this, Wholeoffer.toString(7), Toast.LENGTH_SHORT).show();
                                JSONArray offerdateArray = Wholeoffer.getJSONArray("data");

                                //for (int o=0;o<offerdateArray.length();o++)
                                {
                                    JSONObject everySingleOffer = offerdateArray.getJSONObject(getIntent().getExtras().getInt("itno"));
                                    ((TextView) findViewById(R.id.offerName)).setText(everySingleOffer.getString("offer_name"));
                                    ((TextView) findViewById(R.id.offerDesc)).setText(everySingleOffer.getString("description"));
                                    String urrl;
                                    urrl = everySingleOffer.getString("offer_image");

                                    Glide.with(Offers.this).load(urrl).thumbnail(0.5f).into(((ImageView) findViewById(R.id.oggerImageview)));
                                    progress.dismiss();

                                    final String[] stringList = {urrl};
                                    final String[] nameList = {everySingleOffer.getString("offer_name")};

                                    ImageView oo = ((ImageView) findViewById(R.id.oggerImageview));
                                    oo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            Intent intent = new Intent(Offers.this, ViewPagerClass.class);
                                            intent.putExtra("FILES_TO_SEND", stringList);
                                            intent.putExtra("FILES_TO_SENDa", nameList);

                                            intent.putExtra("position", 1);
                                            startActivity(intent);
                                        }
                                    });

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    parseVolleyError(error);
                }
                if (error instanceof ServerError) {
                    Toast.makeText(Offers.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                    Log.d("Error", String.valueOf(error instanceof ServerError));
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Offers.this, "Authentication Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Authentication Error");
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Offers.this, "Parse Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Parse Error");
                    error.printStackTrace();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Offers.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "No Connection Error");
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Offers.this, "Please check your connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Network Error");
                    error.printStackTrace();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Offers.this, "Timeout Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Timeout Error");
                    error.printStackTrace();
                } else {
                    Toast.makeText(Offers.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // paramenters to be passed to server
                HashMap<String, String> params = new HashMap<>();
                params.put("sid", prefs.getString("sid", "you got nothing"));
                params.put("customer_id", prefs.getString("customer_id", "you got nothing"));
                params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                return params;
            }
        };
        SingleTon.getInnstance(Offers.this).addREquest(getOffersInOffers);

        // tech support button
        FloatingActionButton techsup1 = findViewById(R.id.techsupporta);
        techsup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view,"Kindly leave your message. . . Our tech support team will get in touch with you soon. . .",Snackbar.LENGTH_LONG).show();
                Intent goToTechSuppo = new Intent(getApplicationContext(), TechSupport.class);
                startActivity(goToTechSuppo);

            }
        });
    }


    // method to check internet connection

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

    // call offer of interest appi
    public void finishh(View view) {
        if (!checkConnection())
            return;
        StringRequest userSelectedOffers = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/offerofinterest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        if (response.toString().contains("Login to access")) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Offers.this);
                            try {
                                builder.setMessage((new JSONObject(response)).getString("Message").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        finishAffinity();
                                    }
                                    startActivity((new Intent(Offers.this, Logout.class)));

                                }
                            });
                            builder.setCancelable(false);
                            builder.show();
                        }
                        if (response.toString().contains("Successful")) {
                            //Toast.makeText(Offers.this, response, Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Offers.this);
                            builder1.setMessage("Thank you. . .\nWe have noted your interest.Concerned person will get in touch with you soon. . .");
                            builder1.setCancelable(true);

                            View vie = LayoutInflater.from(Offers.this).inflate(R.layout.alertokay, null);
                            ImageView wokay = vie.findViewById(R.id.wokay);
                            wokay.setBackgroundResource(R.drawable.wokaytrendz);
                            AnimationDrawable frameAnimation = (AnimationDrawable) wokay.getBackground();
                            frameAnimation.start();//-=-=====----=-=-=_+_+-+-+-=_+-=_=_+-=_+-=_=-=_=_+-+_=_=_+-+-+-=_=_+_+-=_=-+_=_=_+-=_
                            builder1.setView(vie);


                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            finish();
                                        }
                                    });

                            builder1.show();

                           /* AlertDialog alert11 = builder1.create();
                            alert11.show();*/

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                if (error.networkResponse != null) {
                    parseVolleyError(error);
                }
                if (error instanceof ServerError) {
                    Toast.makeText(Offers.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                    Log.d("Error", String.valueOf(error instanceof ServerError));
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Offers.this, "Authentication Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Authentication Error");
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Offers.this, "Parse Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Parse Error");
                    error.printStackTrace();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Offers.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "No Connection Error");
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Offers.this, "Please check your connection.", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Network Error");
                    error.printStackTrace();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Offers.this, "Timeout Error", Toast.LENGTH_LONG).show();
                    Log.d("Error", "Timeout Error");
                    error.printStackTrace();
                } else {
                    Toast.makeText(Offers.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("sid", prefs.getString("sid", "you got nothing"));
                params.put("customer_id", prefs.getString("customer_id", "you got nothing"));
                params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                params.put("offer_id", String.valueOf(getIntent().getExtras().getInt("itno")));
                return params;
            }
        };
        userSelectedOffers.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingleTon.getInnstance(Offers.this).addREquest(userSelectedOffers);

        //finish();
    }

    //Handling Volley Error
    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message = data.getString("Message");
            // Toast.makeText(Offers.this, message, Toast.LENGTH_LONG).show();
            android.app.AlertDialog.Builder loginErrorBuilder = new android.app.AlertDialog.Builder(Offers.this);
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
