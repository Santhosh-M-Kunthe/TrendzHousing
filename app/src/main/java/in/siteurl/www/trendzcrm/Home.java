package in.siteurl.www.trendzcrm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class Home extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    SharedPreferences prefs;

    Dialog alertDialog;

    int noOfOffers;
    ListView ls;
    FlatListAdapter flatListAdapter;
    ArrayList<FlatContents> arrayList = new ArrayList<>();
    String wholeResponcepassedToHome = "";
    RecyclerView hsv;
    ArrayList<DocumentsContent> PicsArrayList = new ArrayList<>();
    ViewPager offerviewpager;
    ArrayList<ViewPageOfferContentds> viewPageOfferContentdsArrayList = new ArrayList<>();
    SharedPreferences loginpref;
    SharedPreferences.Editor editor;

    Boolean run = true;
    String[] offerImageURLlist;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.non_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
          /*  case android.R.id.home:
                finish();
                return true;
          */
            case R.id.chngpswdnh:
                Intent intent2 = new Intent(Home.this, ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logoutnh:
                Intent intent3 = new Intent(Home.this, Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        alertDialog = new Dialog(this);
        //Offer view pager uncommetn these
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);

        // COMMENTED FOR FUTURE USE
/*
        MenuItem item = (MenuItem) findViewById(R.id.homemenu);
        item.setVisible(false);*/
       /* ViewPager offerviewpager=(ViewPager) findViewById(R.id.offerviewpager);
        //viewPageOfferContentdsArrayList=getResponseParseItAndgetArraylist();
        ViewPageOfferAdapter viewPageOfferAdapter=new ViewPageOfferAdapter(Home.this,offerImageURLlist);
        Toast.makeText(Home.this, "232323232\n"+offerImageURLlist.toString(), Toast.LENGTH_SHORT).show();
        offerviewpager.setAdapter(viewPageOfferAdapter);
       */ //================================================Offer view pager
        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        //GET RESPONSE FROM LOGIN
        wholeResponcepassedToHome = readFromFile(Home.this, "response");//getIntent().getExtras().getString("response");

        // COMMENTED FOR FUTURE USE

    //Toast.makeText(Home.this, "▻ Click on My Documents to view your documents◅ ", Toast.LENGTH_SHORT).show();
        // TECHSUPPORT SNACKBAR FLOATING BUTTON'
    /*    FloatingActionButton techsup=findViewById(R.id.techsupport);
        techsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Kindly leave your message. . . Our tech support team will get in touch with you soon. . .",Snackbar.LENGTH_LONG).show();
                Intent goToTechSuppo=new Intent(Home.this,TechSupport.class);
                startActivity(goToTechSuppo);
            }
        });*/
        //FAB BUTTON FOR USER COMPLAINTS
        FloatingActionButton techsup1 = findViewById(R.id.techsupporta);
        techsup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view,"Kindly leave your message. . . Our tech support team will get in touch with you soon. . .",Snackbar.LENGTH_LONG).show();
                Intent goToTechSuppo = new Intent(Home.this, TechSupport.class);
                startActivity(goToTechSuppo);

            }
        });

        // FLATS/UNITS LISTVIEW
        ls = (ListView) findViewById(R.id.myunitslistview);
        arrayList = getArrayList();
        flatListAdapter = new FlatListAdapter(Home.this, R.layout.flat_contents, arrayList);
        ls.setAdapter(flatListAdapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // COMMENTED FOR FUTURE USE
                /*  String flatDetailsString=
                        "Unit name:"+arrayList.get(i).getUnitName()+"\n"+
                        "Value:    "+arrayList.get(i).getValue() +"\n"+
                        "Size:     "+arrayList.get(i).getSize()+"\n"+
                        "Sale value:"+arrayList.get(i).getSaleValue() +"\n"+
                        "Balance amount:"+arrayList.get(i).getBalanceAmaount() +"\n"+
                        "Recieved amount:"+arrayList.get(i).getRecievedAmount() +"\n"+
                        "Floor:          "+arrayList.get(i).getFloor() +"\n"+
                        "Block ID:       "+arrayList.get(i).getBlockID() +"\n"+
                        "Project ID:     "+arrayList.get(i).getProjectId() +"\n"+
                        "Status of unit: "+arrayList.get(i).getUnitStatus() +"\n";*/

                String[] flatDetailsString = {arrayList.get(i).getUnitName(), arrayList.get(i).getValue(),
                        arrayList.get(i).getSize(), arrayList.get(i).getSaleValue(), arrayList.get(i).getBalanceAmaount()
                        , arrayList.get(i).getRecievedAmount(), arrayList.get(i).getFloor(), arrayList.get(i).getProjectId()
                        , arrayList.get(i).getBlockID(), arrayList.get(i).getUnitStatus()};

                // COMMENTED FOR FUTURE USE
                    /*
Intent intent = new Intent(getBaseContext(), SignoutActivity.class);
intent.putExtra("EXTRA_SESSION_ID", sessionId);
startActivity(intent);
Access that intent on next activity

String s = getIntent().getStringExtra("EXTRA_SESSION_ID");*/

                //intent to start Flatdetails
                Intent flatDet = new Intent(Home.this, FlatDetails.class);
                flatDet.putExtra("flatDetails", flatDetailsString);
                flatDet.putExtra("flatposition", i);
                flatDet.putExtra("unitid", arrayList.get(i).getUnitId());
                //flatDet.putExtra("responseFromHome",wholeResponcepassedToHome);
                writeToFile(wholeResponcepassedToHome, Home.this, "hometoflat");
                startActivity(flatDet);
            }
        });

        // DOCUMENTS GRIDVIEW number based on screen orientation
        hsv = (RecyclerView) findViewById(R.id.hsv);
        if (Home.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            hsv.setLayoutManager(new GridLayoutManager(Home.this, 1));
        if (Home.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hsv.setLayoutManager(new GridLayoutManager(Home.this, 1));
            //getSupportActionBar().hide();
        }
        getArrayListBottomHome();
        if (checkConnection())
            getResponseParseItAndgetArraylist();
        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw = new DocumentsAdapterRcyclrvw(Home.this, PicsArrayList);
        hsv.setAdapter(documentsAdapterRcyclrvw);
        checkConnection();



        // COMMENTED FOR FUTURE USE
       /* Button photosAsUnitAndProj=(Button) findViewById(R.id.phptps);
        photosAsUnitAndProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("234www",wholeResponcepassedToHome);
                //start MyDocuments intent
                Intent goToDocs=new Intent(Home.this,MyDocuments.class);
                //Intent goToDocs=new Intent(Home.this,CategoryDocs.class);
                goToDocs.putExtra("responseFromHome",wholeResponcepassedToHome);
                goToDocs.putExtra("flatposition",351);
                goToDocs.putExtra("unitProjPics",true);
                //Intent goToDocs=new Intent(Home.this,TestActivity.class);
                startActivity(goToDocs);
               //startActivity(new Intent(Home.this,UnitAndProjPics.class));
            }
        });*/
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


    // CALL \api AND GET ARRAY LIST FOR FLATS
    //    private ArrayList<ViewPageOfferContentds> getResponseParseItAndgetArraylist() {
    private void getResponseParseItAndgetArraylist() {

        //final ArrayList<ViewPageOfferContentds> viewPageOfferContentdsArrayListtemp=new ArrayList<>();

        // go to al offers dialog
        StringRequest stringRequestforoffer = new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/useralloffers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();

                        try {

                            if (response.toString().contains("Login to access")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                                builder.setMessage((new JSONObject(response)).getString("Message").toString());

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finishAffinity();
                                        startActivity((new Intent(Home.this, Logout.class)));

                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();

                            }
                            JSONObject Wholeoffer = new JSONObject(response);
                            // Toast.makeText(Home.this, Wholeoffer.toString(7), Toast.LENGTH_SHORT).show();
                            JSONArray offerdateArray = Wholeoffer.getJSONArray("data");
                            offerImageURLlist = new String[offerdateArray.length()];

                            noOfOffers = offerdateArray.length();
                            for (int o = 0; o < offerdateArray.length(); o++) {
                                JSONObject everySingleOffer = offerdateArray.getJSONObject(o);
                                //viewPageOfferContentdsArrayListtemp.add(new ViewPageOfferContentds(everySingleOffer.getString("offer_id"),everySingleOffer.getString("offer_name"),everySingleOffer.getString("description"),everySingleOffer.getString("offer_image")));
                                offerImageURLlist[o] = everySingleOffer.getString("offer_image");
                                //Toast.makeText(Home.this, "2222222222222\n"+offerImageURLlist[o], Toast.LENGTH_SHORT).show();
                                if (offerImageURLlist[o].length() < 5)
                                    offerImageURLlist[o] = "http://webdesignmysore.com/wp-content/uploads/screenshot-trendzapartments.com-2017-05-15-13-43-54.jpg";
                            }
                            offerviewpager = (ViewPager) findViewById(R.id.offerviewpager);
                            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

                            //viewPageOfferContentdsArrayList=getResponseParseItAndgetArraylist();
                            ViewPageOfferAdapter viewPageOfferAdapter = new ViewPageOfferAdapter(Home.this, offerImageURLlist);
                            offerviewpager.setAdapter(viewPageOfferAdapter);
                            indicator.setViewPager(offerviewpager);


                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (offerviewpager.getCurrentItem() == (noOfOffers - 1)) {
                                        offerviewpager.setCurrentItem(0);
                                    } else {
                                        int x = offerviewpager.getCurrentItem();
                                        //Toast.makeText(Home.this, String.valueOf(noOfOffers), Toast.LENGTH_SHORT).show();
                                        offerviewpager.setCurrentItem(++x, true);

                                    }

                                }
                            };

                            final Timer timer = new Timer(); // This will create a new Thread
                            timer.schedule(new TimerTask() { // task to be scheduled

                                @Override
                                public void run() {
                                    if (run)
                                        handler.post(Update);
                                    else {
                                        timer.cancel();
                                        timer.purge();
                                    }
                                }
                            }, 90, 5000);


                            offerviewpager.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    run = false;
                                    return false;
                                }
                            });
                        } catch (JSONException e) {
                            ((TextView) findViewById(R.id.nooa)).setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //dialog.dismiss();
                        if (error.networkResponse != null) {
                            parseVolleyError(error);
                        }
                        if (error instanceof ServerError) {
                            Toast.makeText(Home.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                            Log.d("Error", String.valueOf(error instanceof ServerError));
                            error.printStackTrace();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Home.this, "Authentication Error", Toast.LENGTH_LONG).show();
                            Log.d("Error", "Authentication Error");
                            error.printStackTrace();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(Home.this, "Parse Error", Toast.LENGTH_LONG).show();
                            Log.d("Error", "Parse Error");
                            error.printStackTrace();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(Home.this, "Server is under maintenance.Please try later.", Toast.LENGTH_LONG).show();
                            Log.d("Error", "No Connection Error");
                            error.printStackTrace();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Home.this, "Please check your connection.", Toast.LENGTH_LONG).show();
                            Log.d("Error", "Network Error");
                            error.printStackTrace();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(Home.this, "Timeout Error", Toast.LENGTH_LONG).show();
                            Log.d("Error", "Timeout Error");
                            error.printStackTrace();
                        } else {
                            Toast.makeText(Home.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                try {
                    JSONObject parseForSID = new JSONObject(wholeResponcepassedToHome);

                    params.put("customer_id", parseForSID.getJSONArray("List of units").getJSONObject(0).getString("customer_id"));
                    params.put("sid", parseForSID.getString("sid"));
                    params.put("api_key", "4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        stringRequestforoffer.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingleTon.getInnstance(Home.this).addREquest(stringRequestforoffer);

        // return viewPageOfferContentdsArrayListtemp;
    }

    private void getArrayListBottomHome() {


        // COMMENTED FOR FUTURE USE
        // ArrayList<DocumentsContent> arrayList=new ArrayList<>();
        /*for (int i=0;i<9;i++)
            arrayList.add(new DocumentsContent(String.valueOf(i)));
        return arrayList;*/

        // ArrayList<DocumentsContent> PicarrayList=new ArrayList<>();
        //ArrayList<DocumentsContent> DOcarrayList=new ArrayList<>();

        /*for (int i=0;i<9;i++){
            arrayList.add(new DocumentsContent(String.valueOf(i)));
        }*/

        // parse JSON of Response
        JSONObject docList = null;
        try {
            docList = new JSONObject(wholeResponcepassedToHome);
            JSONArray UnitBlockProjDocuments = docList.getJSONArray("Units Documents");
            for (int i = 0; i < UnitBlockProjDocuments.length(); i++) {
                JSONObject oneDoc = UnitBlockProjDocuments.getJSONObject(i);
                JSONArray unitQ = oneDoc.getJSONArray("unit_document");
                getDocsPathTypeNameID(unitQ);
                JSONArray blockQ = oneDoc.getJSONArray("block_document");
                getDocsPathTypeNameID(blockQ);
                JSONArray projQ = oneDoc.getJSONArray("project_document");
                getDocsPathTypeNameID(projQ);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }/*
        PicsArrayList=PicarrayList;
        DocsArrayList=DOcarrayList;
*/

    }

    //Handling Volley Error
    public void parseVolleyError(VolleyError error) {
        try {

            //get response to
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message = data.getString("Message");
            //Toast.makeText(Home.this, message, Toast.LENGTH_LONG).show();
            android.app.AlertDialog.Builder loginErrorBuilder = new android.app.AlertDialog.Builder(Home.this);
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


    private void getDocsPathTypeNameID(JSONArray unitQ) {
        //load only image to gridview
        for (int i = 0; i < unitQ.length(); i++)
            try {
                JSONObject singleDocQ = unitQ.getJSONObject(i);
                if (!(singleDocQ.getString("doc_path").contains(".pdf")))
                    PicsArrayList.add(new DocumentsContent(singleDocQ.getString("name"), singleDocQ.getString("doc_path"), singleDocQ.getString("association_id"), singleDocQ.getString("table_name"), singleDocQ.getString("doc_type")));

            } catch (JSONException e) {

                e.printStackTrace();
            }
    }

    // TO GET LIST OF UNIT DOCUMENTS
    private ArrayList<FlatContents> getArrayList() {
        ArrayList<FlatContents> ar = new ArrayList<>();

        // COMMENTED FOR FUTURE USE
        /*  for (int i=1;i<=9;i++){
            ar.add(new FlatContents(String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i)));

        }*/
        //adding params to sharedPreference
        try {
            JSONObject flatList = new JSONObject(wholeResponcepassedToHome);
            JSONArray listOfUnits = flatList.getJSONArray("List of units");
            editor.putString("sid", flatList.getString("sid"));
            ((TextView) findViewById(R.id.custname)).setText("Welcome " + flatList.getString("customer_name"));
            editor.putString("customer_id", flatList.getJSONArray("List of units").getJSONObject(0).getString("customer_id"));
            editor.putString("responseatoz", wholeResponcepassedToHome);
            editor.commit();

            JSONArray UnitBlockProjDocuments = flatList.getJSONArray("Units Documents");

            for (int i = 0; i < listOfUnits.length(); i++) {
                // parse response from home
                JSONObject oneFlat = listOfUnits.getJSONObject(i);
                JSONObject oneDoc = UnitBlockProjDocuments.getJSONObject(i);
                JSONArray blockQ = oneDoc.getJSONArray("block_name");
                JSONObject oneBlockName = blockQ.getJSONObject(0);
                JSONArray projQ = oneDoc.getJSONArray("project_name");
                JSONObject oneProjName = projQ.getJSONObject(0);

                editor.putString("customer_id", oneFlat.getString("customer_id"));
                editor.commit();


                //add contents to arraylist of flatdetails
                ar.add(new FlatContents(oneFlat.getString("unit_id"), oneFlat.getString("unit_name"),
                        oneBlockName.getString("block_name"), oneProjName.getString("name"), oneFlat.getString("floor"),
                        oneFlat.getString("value"),
                        oneFlat.getString("customer_id"), oneFlat.getString("sale_value"), oneFlat.getString("received_amount"),
                        oneFlat.getString("balance_amount"),
                        oneFlat.getString("size"), oneFlat.getString("unit_status"), oneFlat.getString("created_at")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ar;
    }

    public void GoToMyDocsClassMethod(View view) {


        // COMMENTED FOR FUTURE USE
        //start MyDocuments intent
        //Intent goToDocs=new Intent(Home.this,MyDocuments.class);
     /*   Intent goToDocs=new Intent(Home.this,CategoryDocs.class);
        goToDocs.putExtra("responseFromHome",wholeResponcepassedToHome);
       goToDocs.putExtra("flatposition",351);
        //Intent goToDocs=new Intent(Home.this,TestActivity.class);
        startActivity(goToDocs);
*/

    }

    public void GoToMyDocsClassMethodall(View view) {
        Intent goToDocs = new Intent(Home.this, MyDocuments.class);
        goToDocs.putExtra("responseFromHome", wholeResponcepassedToHome);
        goToDocs.putExtra("flatposition", 351);
        //Intent goToDocs=new Intent(Home.this,TestActivity.class);
        startActivity(goToDocs);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.aa)
                .setTitle("Closing Trendz Housing")
                .setMessage("Are you sure you want quit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
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
