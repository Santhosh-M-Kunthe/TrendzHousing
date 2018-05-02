package in.siteurl.www.trendzcrm;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class UnitAndProjPics extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String[] unitNameList, unitURLList, projNameList, projURLList;
    ArrayList<DocumentsContent> unit_unitproj = new ArrayList<>(), proj_unitproj = new ArrayList<>();


    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    MaterialEditText sub, msg;
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
                /*finish();
                return true;*/
            case R.id.homemenu:
                Intent intent = new Intent(UnitAndProjPics.this, Home.class);
                /*SharedPreferences preferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                intent.putExtra("response",preferences.getString("responseatoz",null));
                */
                startActivity(intent);
                return true;
            case R.id.chngpswd:
                Intent intent2 = new Intent(UnitAndProjPics.this, ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(UnitAndProjPics.this, Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_and_proj_pics);


        setTitle(getIntent().getStringExtra("naam"));
        // ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle("Trendz CRM");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        alertDialog = new Dialog(this);

        // get unit and project array list
        unit_unitproj = (ArrayList<DocumentsContent>) getIntent().getSerializableExtra("unit_unitproja");
        proj_unitproj = (ArrayList<DocumentsContent>) getIntent().getSerializableExtra("proj_unitproja");


        unitNameList = new String[unit_unitproj.size()];
        unitURLList = new String[unit_unitproj.size()];
        projNameList = new String[proj_unitproj.size()];
        projURLList = new String[proj_unitproj.size()];

        for (int i = 0; i < unit_unitproj.size(); i++) {
            unitNameList[i] = unit_unitproj.get(i).getDocName();
            unitURLList[i] = unit_unitproj.get(i).getImageURL();
            Log.d("as1", unitNameList[i]);
            //Toast.makeText(this, unitNameList[i], Toast.LENGTH_LONG).show();
        }

        for (int i = 0; i < proj_unitproj.size(); i++) {
            projNameList[i] = proj_unitproj.get(i).getDocName();
            projURLList[i] = proj_unitproj.get(i).getImageURL();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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

    // setting up offer viewpager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UnitFragments(), "Unit");
        adapter.addFragment(new ProjFragments(), "Project");
        viewPager.setAdapter(adapter);
    }


    // adapt fragment to fragment list
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {


            Bundle bundle = new Bundle();//    String [] unitNameList,unitURLList,projNameList,projURLList;

            // passing data to fragments
            bundle.putStringArray("unitNameList", unitNameList);
            bundle.putStringArray("unitURLList", unitURLList);
            bundle.putStringArray("projNameList", projNameList);
            bundle.putStringArray("projURLList", projURLList);
            // set Fragmentclass Arguments
            fragment.setArguments(bundle);


            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UnitAndProjPics.this, Home.class);
        SharedPreferences preferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
        intent.putExtra("response", preferences.getString("responseatoz", null));
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

    @Override
    protected void onResume() {
        super.onResume();
        VendorApplication.getInstance().setConnectivityListener(this);
    }


}