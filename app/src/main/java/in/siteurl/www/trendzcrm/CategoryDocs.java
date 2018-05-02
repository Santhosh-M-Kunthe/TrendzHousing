package in.siteurl.www.trendzcrm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CategoryDocs extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Dialog alertDialog;
    String responseToBeFiltered;int[] k=new int[500];int j=0;

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
                Intent intent = new Intent(CategoryDocs.this, Home.class);
               /* SharedPreferences preferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                intent.putExtra("response",preferences.getString("responseatoz",null));
               */
                startActivity(intent);
                return true;
            case R.id.chngpswd:
                Intent intent2 = new Intent(CategoryDocs.this, ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(CategoryDocs.this, Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_docs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.doc_group_item, android.R.id.gropuname, docGroupStringList);

        responseToBeFiltered = readFromFile(CategoryDocs.this, "intentToCatDocs");//getIntent().getExtras().getString("responseFromHome");
        alertDialog = new Dialog(this);
        checkConnection();

        ((FloatingActionButton) findViewById(R.id.alldocsfromcategory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // send all response to MyDocuments class
                Intent goToDocs = new Intent(CategoryDocs.this, MyDocuments.class);
                //goToDocs.putExtra("responseFromHome",responseToBeFiltered);
                writeToFile(responseToBeFiltered, CategoryDocs.this, "intentToMyDocs");
                goToDocs.putExtra("flatposition", 351);
                //Intent goToDocs=new Intent(Home.this,TestActivity.class);
                startActivity(goToDocs);
            }
        });
        FloatingActionButton techsup1 = findViewById(R.id.techsupporta);
        techsup1.setOnClickListener(new View.OnClickListener() {

            //go to tech suport classes
            @Override
            public void onClick(View view) {
                // Snackbar.make(view,"Kindly leave your message. . . Our tech support team will get in touch with you soon. . .",Snackbar.LENGTH_LONG).show();
                Intent goToTechSuppo = new Intent(getApplicationContext(), TechSupport.class);
                startActivity(goToTechSuppo);

            }
        });

        // Toast.makeText(CategoryDocs.this, responseToBeFiltered, Toast.LENGTH_SHORT).show();

        ListView listOfDocGrpups = (ListView) findViewById(R.id.listOfDocGroupsl);

        JSONArray listOfDocGroups = null;
        try {
            // parse json to get documents group list
            JSONObject forGroupList = new JSONObject(responseToBeFiltered);
            setTitle(forGroupList.getJSONArray("List of units").getJSONObject(0).getString("unit_name"));
            //Log.d("sw6",responseToBeFiltered);
            listOfDocGroups = forGroupList.getJSONArray("Document_group_list");
            String[] docGroupStringList = new String[listOfDocGroups.length()];
            int countt=0;
            for (int i = 0; i < listOfDocGroups.length(); i++) {
                if (filterForGroupID(Integer.parseInt(listOfDocGroups.getJSONObject(i).getString("document_group_id")),"My DOcuments",false))
                {
                    Log.d("sw1",String.valueOf(countt)+"----"+docGroupStringList[countt]);
                    k[countt]=i+1;
                    docGroupStringList[countt++] = listOfDocGroups.getJSONObject(i).getString("document_name") + "  ➤";
                    Log.d("sw1",String.valueOf(countt-1)+"----"+docGroupStringList[countt-1]);

                }
            }
            String[] forAdapter=new String[countt];
            for (int i=0;i<countt;i++){
                forAdapter[i]=docGroupStringList[i];
                Log.d("sw3",forAdapter[i]+"----"+docGroupStringList[i]);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryDocs.this, R.layout.doc_group_item, R.id.gropuname, forAdapter);
            listOfDocGrpups.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONArray finalListOfDocGroups = listOfDocGroups;
        listOfDocGrpups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(CategoryDocs.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                try {
                    filterForGroupID(k[i], finalListOfDocGroups.getJSONObject(k[i]-1).getString("document_name"),true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //method to filter new response based on ID
    private boolean filterForGroupID(int groupIDtoBeRemoved, String document_name,boolean shoulshow) {
        boolean length = false;
        JSONObject filteredjsonObject = new JSONObject();
        try {
            JSONObject rawjsonObject = new JSONObject(responseToBeFiltered);
            JSONArray temp = new JSONArray();
            JSONArray unit = new JSONArray();
            JSONArray block = new JSONArray();
            JSONArray proj = new JSONArray();

            // add filtered things to new jsonObject
            filteredjsonObject.accumulate("Error", rawjsonObject.getString("Error"));
            filteredjsonObject.accumulate("Message", rawjsonObject.getString("Message"));
            filteredjsonObject.accumulate("Role", rawjsonObject.getString("Role"));
            filteredjsonObject.accumulate("customer_name", rawjsonObject.getString("customer_name"));
            filteredjsonObject.accumulate("sid", rawjsonObject.getString("sid"));
            filteredjsonObject.put("List of units", (Object) rawjsonObject.getJSONArray("List of units"));

            JSONArray allDocuments = rawjsonObject.getJSONArray("Units Documents");
            for (int q = 0; q < allDocuments.length(); q++) {
                JSONObject oneUnitObject = allDocuments.getJSONObject(q);

                if (!oneUnitObject.get("unit_document").toString().contains("No recor")) {
                    JSONArray unitDocArray = oneUnitObject.getJSONArray("unit_document");
                    for (int w = 0; w < unitDocArray.length(); w++) {
                        JSONObject oneDocOfUnit = unitDocArray.getJSONObject(w);
                        if (oneDocOfUnit.getString("doc_group_id").contentEquals(String.valueOf(groupIDtoBeRemoved))) {
                            block.put(oneDocOfUnit);
                        }
                    }
                }

                if (!oneUnitObject.get("block_document").toString().contains("No recor")) {
                    JSONArray blockDocArray = oneUnitObject.getJSONArray("block_document");
                    for (int w = 0; w < blockDocArray.length(); w++) {
                        JSONObject oneDocOfUnit = blockDocArray.getJSONObject(w);
                        if (oneDocOfUnit.getString("doc_group_id").contentEquals(String.valueOf(groupIDtoBeRemoved))) {
                            proj.put(oneDocOfUnit);
                        }
                    }


                }

                if (!oneUnitObject.get("project_document").toString().contains("No recor")) {
                    JSONArray projDocArray = oneUnitObject.getJSONArray("project_document");
                    for (int w = 0; w < projDocArray.length(); w++) {
                        JSONObject oneDocOfUnit = projDocArray.getJSONObject(w);
                        if (oneDocOfUnit.getString("doc_group_id").contentEquals(String.valueOf(groupIDtoBeRemoved))) {
                            unit.put(oneDocOfUnit);
                        }
                    }
                }
                JSONObject nth = new JSONObject();
                nth.put("unit_document", unit);
                nth.put("block_document", block);
                nth.put("project_document", proj);
                temp.put(nth);
            }
            filteredjsonObject.put("Units Documents", (Object) temp);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent goToDocs = new Intent(CategoryDocs.this, MyDocuments.class);
        try {
            // sned filtered response to mydociments class
            writeToFile(filteredjsonObject.toString(9), CategoryDocs.this, "intentToMyDocs");

            goToDocs.putExtra("flatposition", 351);
            goToDocs.putExtra("naam",document_name);
            //Intent goToDocs=new Intent(Home.this,TestActivity.class);
            Log.d("sw4", String.valueOf(filteredjsonObject.getJSONArray("Units Documents").toString().length()));
            if (filteredjsonObject.getJSONArray("Units Documents").toString().length()>72)
            {
                if (shoulshow)startActivity(goToDocs);
                    length=true;
            }
            else {
                length=false;
                //show alert dialog
              if (shoulshow){
                  AlertDialog alertDialoga = new AlertDialog.Builder(CategoryDocs.this).create();
                  alertDialoga.setMessage("No documents in this category. . .");
                  alertDialoga.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                          new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
                              }
                          });
                  alertDialoga.show();

              }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //goToDocs.putExtra("responseFromHome",filteredjsonObject.toString(9));
        return length;
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
            registerReceiver(mNetworkReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        VendorApplication.getInstance().setConnectivityListener(this);

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

    //method to read from file
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
