package in.siteurl.www.trendzcrm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.io.Serializable;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class MyDocuments extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    ArrayList<DocumentsContent> PicsArrayList = new ArrayList<>();
    ArrayList<DocumentsContent> unit_unitproj = new ArrayList<>();
    ArrayList<DocumentsContent> proj_unitproj = new ArrayList<>();

    ArrayList<DocumentsContent> DocsArrayList = new ArrayList<>();
    int total, timePassed, tp2 = 100;
    ProgressBar bar;
    String responceFromHome = "";
    RelativeLayout.LayoutParams lpFOrSizeDocs,lpFOrSizePics;
    RecyclerView docPicsRecyclerView;
    RecyclerView docDocsRecyclerView;
    TextView pictv, doctv;
    Dialog alertDialog;
    boolean addPicToDocPlace = false, saveToUnit = false, saveToProj = false;

    CheckBox flatcb;
    CheckBox blockcb;
    CheckBox projcb;
    CheckBox mydoccb;

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
                Intent intent = new Intent(MyDocuments.this, Home.class);
                startActivity(intent);
                return true;
            case R.id.chngpswd:
                Intent intent2 = new Intent(MyDocuments.this, ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(MyDocuments.this, Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_documents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lpFOrSizeDocs= (RelativeLayout.LayoutParams) ((RelativeLayout)findViewById(R.id.fornopics)).getLayoutParams();
        lpFOrSizePics= (RelativeLayout.LayoutParams) ((RelativeLayout)findViewById(R.id.fornodocs)).getLayoutParams();
        //get response frreaom home
        responceFromHome = readFromFile(MyDocuments.this, "intentToMyDocs");//asqgetIntent().getExtras().getString("responseFromHome");
        responceFromHome.replace("\"No records found\"", "[]");
        pictv = new TextView(getApplicationContext());
        doctv = new TextView(getApplicationContext());

        bar = (ProgressBar) findViewById(R.id.progress);

        // COMMENTED FOR FUTURE USE
        /* if(MyDocuments.this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            bar.getLayoutParams().height=20;
            bar.getLayoutParams().width=20;

        }*/

       setTitle(getIntent().getStringExtra("naam"));
        alertDialog = new Dialog(this);
        checkConnection();
        flatcb = findViewById(R.id.flatchkbox);
        blockcb = findViewById(R.id.blockchkbox);
        projcb = findViewById(R.id.projectchkbox);
        mydoccb = findViewById(R.id.mydocschkbox);

        // FILTER BASED ON CHECKBOX
        flatcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((RelativeLayout)findViewById(R.id.fornopics)).setLayoutParams(lpFOrSizeDocs);
                    ((RelativeLayout)findViewById(R.id.fornodocs)).setLayoutParams(lpFOrSizePics);
                    blockcb.setChecked(false);
                    projcb.setChecked(false);
                    mydoccb.setChecked(false);
                    filterFor("units");
                } else
                    putAll();
            }
        });

        blockcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    ((RelativeLayout)findViewById(R.id.fornopics)).setLayoutParams(lpFOrSizeDocs);
                    ((RelativeLayout)findViewById(R.id.fornodocs)).setLayoutParams(lpFOrSizePics);
                    flatcb.setChecked(false);
                    projcb.setChecked(false);
                    mydoccb.setChecked(false);
                    filterFor("Blocks");
                } else
                    putAll();
            }
        });

        projcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((RelativeLayout)findViewById(R.id.fornopics)).setLayoutParams(lpFOrSizeDocs);
                    ((RelativeLayout)findViewById(R.id.fornodocs)).setLayoutParams(lpFOrSizePics);
                    blockcb.setChecked(false);
                    flatcb.setChecked(false);
                    mydoccb.setChecked(false);
                    filterFor("projects");

                } else
                    putAll();
            }
        });

        mydoccb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((RelativeLayout)findViewById(R.id.fornopics)).setLayoutParams(lpFOrSizeDocs);
                    ((RelativeLayout)findViewById(R.id.fornodocs)).setLayoutParams(lpFOrSizePics);
                    blockcb.setChecked(false);
                    flatcb.setChecked(false);
                    projcb.setChecked(false);
                    filterFor("mydocs");

                } else
                    putAll();
            }
        });


        bar.setProgress(total);
        int oneMin = 5 * 1000;
        //tp2=timePassed;
        CountDownTimer cdt = new CountDownTimer(oneMin, 1000) {

            public void onTick(long millisUntilFinished) {

                total = (int) ((timePassed / 60) * 100);
                bar.setProgress(total);
            }

            public void onFinish() {
                bar.setVisibility(View.GONE);
            }
        }.start();
        docPicsRecyclerView = (RecyclerView) findViewById(R.id.picsDocumentsREcyclerView);
        docDocsRecyclerView = (RecyclerView) findViewById(R.id.docsDocumentsRecyclerView);


        if (MyDocuments.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            docDocsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this, 2));
            docPicsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this, 2));
        }
        if (MyDocuments.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            docDocsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this, 2));
            docPicsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this, 2));
        }
        //getArrayList();
        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw = new DocumentsAdapterRcyclrvw(MyDocuments.this, PicsArrayList);
        DocumentPDFadapterRecyclrvw documentPDFadapterRecyclrvw = new DocumentPDFadapterRecyclrvw(MyDocuments.this, DocsArrayList);


        docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        docPicsRecyclerView.setAdapter(documentsAdapterRcyclrvw);

        int flatPosition = getIntent().getExtras().getInt("flatposition");

        if (flatPosition != 351)
            getArrayListForUnits(flatPosition);
        else getArrayList();

        if (getIntent().getExtras().getBoolean("unitProjPics", false))
            putUnitAndProjPhotos();

        if (PicsArrayList.isEmpty()) {
            bar.setVisibility(View.GONE);



            ((TextView) findViewById(R.id.nopics)).setVisibility(View.VISIBLE);

            RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.fornodocs);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView

            relativeLayout.setLayoutParams(lp);


            RelativeLayout relativeLayout1=(RelativeLayout) findViewById(R.id.fornopics);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                    0, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView

            relativeLayout1.setLayoutParams(lp1);

        } else ((TextView) findViewById(R.id.nopics)).setVisibility(View.GONE);


        if (DocsArrayList.isEmpty()) {
            bar.setVisibility(View.GONE);
    ((TextView) findViewById(R.id.nodocs)).setVisibility(View.VISIBLE);

            RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.fornopics);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView
            //lp.setMargins(9,900,9,9);
            relativeLayout.setLayoutParams(lp);

            RelativeLayout relativeLayout1=(RelativeLayout) findViewById(R.id.fornodocs);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                    0, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView
            //lp.setMargins(9,900,9,9);
            relativeLayout1.setLayoutParams(lp1);

        } else ((TextView) findViewById(R.id.nodocs)).setVisibility(View.GONE);


    }

    private void putUnitAndProjPhotos() {
        // docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        addPicToDocPlace = false;
        saveToProj = true;
        filterFor("projects");
        saveToProj = false;
        //docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        addPicToDocPlace = true;
        saveToUnit = true;
        filterFor("units");
        saveToProj = false;
        saveToUnit = false;

        Intent intent = new Intent(MyDocuments.this, UnitAndProjPics.class);
       Log.d("wer23a", String.valueOf(unit_unitproj.size()) + String.valueOf(unit_unitproj.size()));

        intent.putExtra("unit_unitproja", unit_unitproj);
        intent.putExtra("proj_unitproja", proj_unitproj);
        intent.putExtra("naam",getIntent().getStringExtra("naam"));
        startActivity(intent);

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
    //method to get arraylist of units

    private void getArrayListForUnits(int flatPosition) {
        JSONObject docList = null;
        try {
            docList = new JSONObject(responceFromHome);
            JSONArray UnitBlockProjDocuments = docList.getJSONArray("Units Documents");
            JSONObject oneDoc = UnitBlockProjDocuments.getJSONObject(flatPosition);
            JSONArray unitQ = oneDoc.getJSONArray("unit_document");
            getDocsPathTypeNameID(unitQ);
            JSONArray blockQ = oneDoc.getJSONArray("block_document");
            getDocsPathTypeNameID(blockQ);
            JSONArray projQ = oneDoc.getJSONArray("project_document");
            getDocsPathTypeNameID(projQ);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void putAll() {
        ((RelativeLayout)findViewById(R.id.fornopics)).setLayoutParams(lpFOrSizeDocs);
        ((RelativeLayout)findViewById(R.id.fornodocs)).setLayoutParams(lpFOrSizePics);

        bar.setProgress(total);
        int oneMin = 5 * 1000;

        CountDownTimer cdt = new CountDownTimer(oneMin, 1000) {

            public void onTick(long millisUntilFinished) {

                total = (int) ((tp2 / 60) * 100);
                bar.setProgress(total);
            }

            public void onFinish() {
                bar.setVisibility(View.GONE);
            }
        }.start();
        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw = new DocumentsAdapterRcyclrvw(MyDocuments.this, PicsArrayList);
        DocumentPDFadapterRecyclrvw documentPDFadapterRecyclrvw = new DocumentPDFadapterRecyclrvw(MyDocuments.this, DocsArrayList);


        docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        docPicsRecyclerView.setAdapter(documentsAdapterRcyclrvw);
    }

    /// filter the array list for particular string
    private void filterFor(String units) {

        ArrayList<DocumentsContent> newPicArray = new ArrayList<>();
        ArrayList<DocumentsContent> newDocArray = new ArrayList<>();

        for (int i = 0; i < PicsArrayList.size(); i++)
            if (PicsArrayList.get(i).getTableName().contains(units))
                newPicArray.add(PicsArrayList.get(i));


        for (int i = 0; i < DocsArrayList.size(); i++)
            if (DocsArrayList.get(i).getTableName().contains(units))
                newDocArray.add(DocsArrayList.get(i));

        // adapters for recycle view
        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw = new DocumentsAdapterRcyclrvw(MyDocuments.this, newPicArray);
        DocumentPDFadapterRecyclrvw documentPDFadapterRecyclrvw = new DocumentPDFadapterRecyclrvw(MyDocuments.this, newDocArray);
        if (addPicToDocPlace) {
            docDocsRecyclerView.setAdapter(documentsAdapterRcyclrvw);
            //return;

        }
        if (saveToUnit && (unit_unitproj.size() == 0))
            unit_unitproj = newPicArray;
        if (saveToProj & (proj_unitproj.size() == 0))
            proj_unitproj = newPicArray;


        if (newPicArray.isEmpty()) {
            bar.setVisibility(View.GONE);
    RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.fornodocs);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView
            relativeLayout.setLayoutParams(lp);


            RelativeLayout relativeLayout1=(RelativeLayout) findViewById(R.id.fornopics);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                   0, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView
            relativeLayout1.setLayoutParams(lp1);


            ((TextView) findViewById(R.id.nopics)).setVisibility(View.VISIBLE);

        } else ((TextView) findViewById(R.id.nopics)).setVisibility(View.GONE);


        // display text view saying no documents are available
        if (newDocArray.isEmpty()) {
            bar.setVisibility(View.GONE);
            RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.fornopics);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView
            relativeLayout.setLayoutParams(lp);

            RelativeLayout relativeLayout1=(RelativeLayout) findViewById(R.id.fornodocs);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                    0, // Width of TextView
                    RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView
            relativeLayout1.setLayoutParams(lp1);

            ((TextView) findViewById(R.id.nodocs)).setVisibility(View.VISIBLE);

        } else ((TextView) findViewById(R.id.nodocs)).setVisibility(View.GONE);


        docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        docPicsRecyclerView.setAdapter(documentsAdapterRcyclrvw);

    }

    // get arraylist of documents
    private void getArrayList() {

       JSONObject docList = null;
        try {
            docList = new JSONObject(responceFromHome);
            JSONArray UnitBlockProjDocuments = docList.getJSONArray("Units Documents");
            for (int i = 0; i < UnitBlockProjDocuments.length(); i++) {
                JSONObject oneDoc = UnitBlockProjDocuments.getJSONObject(i);

                if (!oneDoc.get("unit_document").toString().contains("No recor")) {
                    JSONArray unitQ = oneDoc.getJSONArray("unit_document");
                    getDocsPathTypeNameID(unitQ);
                }

                if (!oneDoc.get("block_document").toString().contains("No recor")) {
                    JSONArray blockQ = oneDoc.getJSONArray("block_document");
                    getDocsPathTypeNameID(blockQ);
                }

                if (!oneDoc.get("project_document").toString().contains("No recor")) {
                    JSONArray projQ = oneDoc.getJSONArray("project_document");
                    getDocsPathTypeNameID(projQ);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // get name and ID of document path name
    private void getDocsPathTypeNameID(JSONArray unitQ) {
        for (int i = 0; i < unitQ.length(); i++)
            try {
                JSONObject singleDocQ = unitQ.getJSONObject(i);
                if (singleDocQ.getString("doc_path").contains(".pdf"))
                    DocsArrayList.add(new DocumentsContent(singleDocQ.getString("name"), singleDocQ.getString("doc_path"), singleDocQ.getString("association_id"), singleDocQ.getString("table_name"), singleDocQ.getString("doc_type")));
                else
                    PicsArrayList.add(new DocumentsContent(singleDocQ.getString("name"), singleDocQ.getString("doc_path"), singleDocQ.getString("association_id"), singleDocQ.getString("table_name"), singleDocQ.getString("doc_type")));
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
