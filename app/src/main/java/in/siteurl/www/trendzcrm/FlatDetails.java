package in.siteurl.www.trendzcrm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FlatDetails extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Dialog alertDialog;

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
                Intent intent = new Intent(FlatDetails.this, Home.class);
                /*SharedPreferences preferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                intent.putExtra("response",preferences.getString("responseatoz",null));
                */
                startActivity(intent);
                return true;
            case R.id.chngpswd:
                Intent intent2 = new Intent(FlatDetails.this, ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(FlatDetails.this, Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        alertDialog = new Dialog(this);
        checkConnection();

        // set details of all textview by initializing arrays
        String[] flatlistdetailsarray = (String[]) getIntent().getSerializableExtra("flatDetails");
        ((TextView) findViewById(R.id.unitnName)).setText(flatlistdetailsarray[0]);
        //((TextView)findViewById(R.id.value)).setText(flatlistdetailsarray[1]);
        ((TextView) findViewById(R.id.size)).setText(flatlistdetailsarray[2]);
        ((TextView) findViewById(R.id.salval)).setText(flatlistdetailsarray[3]);
        ((TextView) findViewById(R.id.balamnt)).setText(flatlistdetailsarray[5]);
        ((TextView) findViewById(R.id.recamnt)).setText(flatlistdetailsarray[4]);
        ((TextView) findViewById(R.id.floor)).setText(flatlistdetailsarray[6]);
        ((TextView) findViewById(R.id.block)).setText(flatlistdetailsarray[7]);
        ((TextView) findViewById(R.id.project)).setText(flatlistdetailsarray[8]);
        ((TextView) findViewById(R.id.status)).setText(flatlistdetailsarray[9]);

        // go to adding ticket
        Button goTOAddTicket = findViewById(R.id.flattoat);
        goTOAddTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAT = new Intent(FlatDetails.this, AddTicket.class);
                toAT.putExtra("unitid", getIntent().getExtras().getString("unitid"));
                startActivity(toAT);
            }
        });


    }

    public void finish(View view) {
        finish();
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

    ConnectivityReceiver mNetworkReceiver = new ConnectivityReceiver();

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        VendorApplication.getInstance().setConnectivityListener(this);
    }

    //filter responsebased on unit ID
    public void passFlatDocsToMyDocs(View view) {

        Intent goToDocs = new Intent(FlatDetails.this, CategoryDocs.class);
        SharedPreferences preferences = FlatDetails.this.getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        JSONObject filteresJSONObject = new JSONObject();

        String filteredResponse = readFromFile(FlatDetails.this, "response");//preferences.getString("responseatoz",null);
        try {
            JSONObject orignalJSONobject = new JSONObject(filteredResponse);
            filteresJSONObject.put("Error", orignalJSONobject.getString("Error"));
            filteresJSONObject.put("Message", orignalJSONobject.getString("Message"));
            filteresJSONObject.put("Role", orignalJSONobject.getString("Role"));
            filteresJSONObject.put("customer_name", orignalJSONobject.getString("customer_name"));
            filteresJSONObject.put("sid", orignalJSONobject.getString("sid"));
            //filteresJSONObject.put("Document_group_list",orignalJSONobject.getString("Document_group_list"));

            JSONArray temp1 = new JSONArray();
            //temp1.put();
            filteresJSONObject.put("Document_group_list", orignalJSONobject.getJSONArray("Document_group_list"));


            JSONArray temp2 = new JSONArray();
            temp2.put(orignalJSONobject.getJSONArray("List of units").getJSONObject(getIntent().getIntExtra("flatposition", 0)));
            filteresJSONObject.put("List of units", temp2);

            JSONArray temp3 = new JSONArray();
            temp3.put(orignalJSONobject.getJSONArray("Units Documents").getJSONObject(getIntent().getIntExtra("flatposition", 0)));
            filteresJSONObject.put("Units Documents", temp3);

            Log.d("frl", filteresJSONObject.toString(9));
            //goToDocs.putExtra("responseFromHome",filteresJSONObject.toString(9));
            writeToFile(filteresJSONObject.toString(9), FlatDetails.this, "intentToCatDocs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // goToDocs.putExtra("responseFromHome",wholeResponcepassedToHome);
        goToDocs.putExtra("flatposition", 351);
        //Intent goToDocs=new Intent(Home.this,TestActivity.class);
        startActivity(goToDocs);

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