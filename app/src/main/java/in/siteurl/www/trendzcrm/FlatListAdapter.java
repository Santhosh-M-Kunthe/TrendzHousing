package in.siteurl.www.trendzcrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiteURL on 2/26/2018.
 */

public class FlatListAdapter extends ArrayAdapter<FlatContents> {

    Context contextOfAdapter;
    int resourceOfAdapter;
    ArrayList<FlatContents> objectsOfAdapter;

    public FlatListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FlatContents> objects) {
        super(context, resource, objects);
        contextOfAdapter = context;
        resourceOfAdapter = resource;
        objectsOfAdapter = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(contextOfAdapter).inflate(resourceOfAdapter, parent, false);
        TextView flatname = (TextView) view.findViewById(R.id.contentflatname);
        flatname.setText(objectsOfAdapter.get(position).getUnitName() + "-" + objectsOfAdapter.get(position).getProjectId());
        flatname.setText(flatname.getText().toString().replace("Block", ""));
        flatname.setText(flatname.getText().toString().replace(" ", ""));

        ImageView img = (ImageView) view.findViewById(R.id.swiperightatoz);
        img.setBackgroundResource(R.drawable.swiperightatoz);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();//-=-=====----=-=-=_+_+-+-+-=_+-=_=_+-=_+-=_=-=_=_+-+_=_=_+-+-+-=_=_+_+-=_=-+_=_=_+-=_
        TextView flatbalnace = (TextView) view.findViewById(R.id.contentflatbbalance);
        flatbalnace.setText(objectsOfAdapter.get(position).getBlockID());
        ((TextView) view.findViewById(R.id.locasan)).setText("Mysuru");

        Button docc = (Button) view.findViewById(R.id.docc);
        Button phoo = (Button) view.findViewById(R.id.phoo);

        // go to flat related documents
        docc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToDocs = new Intent(contextOfAdapter, CategoryDocs.class);
                goToDocs.putExtra("naam",objectsOfAdapter.get(position).getUnitName() + "-" + objectsOfAdapter.get(position).getProjectId());
                SharedPreferences preferences = contextOfAdapter.getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
                JSONObject filteresJSONObject = new JSONObject();

                String filteredResponse = readFromFile(contextOfAdapter, "response");//preferences.getString("responseatoz",null);
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
                    temp2.put(orignalJSONobject.getJSONArray("List of units").getJSONObject(position));
                    filteresJSONObject.put("List of units", temp2);

                    JSONArray temp3 = new JSONArray();
                    temp3.put(orignalJSONobject.getJSONArray("Units Documents").getJSONObject(position));
                    filteresJSONObject.put("Units Documents", temp3);

                    //goToDocs.putExtra("responseFromHome",filteresJSONObject.toString(9));
                    writeToFile(filteresJSONObject.toString(9), contextOfAdapter, "intentToCatDocs");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // goToDocs.putExtra("responseFromHome",wholeResponcepassedToHome);
                goToDocs.putExtra("flatposition", 351);
                //Intent goToDocs=new Intent(Home.this,TestActivity.class);
                contextOfAdapter.startActivity(goToDocs);
            }
        });

        //go to flat related photos
        phoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // COMMENTED FOR FUTURE USE
               /* // intent to go to documents
                Intent goToDocs=new Intent(contextOfAdapter,MyDocuments.class);
                //Intent goToDocs=new Intent(Home.this,CategoryDocs.class);
                SharedPreferences preferences = contextOfAdapter.getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
                goToDocs.putExtra("responseFromHome",preferences.getString("responseatoz",null));
                //goToDocs.putExtra("responseFromHome",wholeResponcepassedToHome);
                goToDocs.putExtra("flatposition",351);
                goToDocs.putExtra("unitProjPics",true);
                //Intent goToDocs=new Intent(Home.this,TestActivity.class);
                contextOfAdapter.startActivity(goToDocs);
                //startActivity(new Intent(Home.this,UnitAndProjPics.class));*/

                Intent goToDocs = new Intent(contextOfAdapter, MyDocuments.class);
                goToDocs.putExtra("naam",objectsOfAdapter.get(position).getUnitName() + "-" + objectsOfAdapter.get(position).getProjectId());
                SharedPreferences preferences = contextOfAdapter.getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
                JSONObject filteresJSONObject = new JSONObject();

                String filteredResponse = readFromFile(contextOfAdapter, "response");//preferences.getString("responseatoz",null);
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
                    temp2.put(orignalJSONobject.getJSONArray("List of units").getJSONObject(position));
                    filteresJSONObject.put("List of units", temp2);

                    JSONArray temp3 = new JSONArray();
                    temp3.put(orignalJSONobject.getJSONArray("Units Documents").getJSONObject(position));
                    filteresJSONObject.put("Units Documents", temp3);

                    //goToDocs.putExtra("responseFromHome",filteresJSONObject.toString(9));
                    writeToFile(filteresJSONObject.toString(9), contextOfAdapter, "intentToMyDocs");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // goToDocs.putExtra("responseFromHome",wholeResponcepassedToHome);
                goToDocs.putExtra("flatposition", 351);
                goToDocs.putExtra("unitProjPics", true);
                //Intent goToDocs=new Intent(Home.this,TestActivity.class);
                contextOfAdapter.startActivity(goToDocs);


            }
        });
        return view;
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
