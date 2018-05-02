package in.siteurl.www.trendzcrm;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ViewPagerClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        final ViewPager gallery = (ViewPager) findViewById(R.id.viewPager);

        final String[] filelist = (String[]) getIntent().getSerializableExtra("FILES_TO_SEND");
        final String[] nameList = (String[]) getIntent().getSerializableExtra("FILES_TO_SENDa");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(ViewPagerClass.this, filelist);
        gallery.setAdapter(viewPagerAdapter);
        gallery.setCurrentItem(1 + getIntent().getExtras().getInt("position"));


        FloatingActionButton don = findViewById(R.id.share);
        don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = filelist[gallery.getCurrentItem()];
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("download");
                request.setTitle(nameList[gallery.getCurrentItem()]);
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");

// get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(ViewPagerClass.this, "Download started. . .", Toast.LENGTH_SHORT).show();
            }
        });
        gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(ViewPagerClass.this, String.valueOf(positionOffset), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageSelected(int position) {

                Snackbar.make(findViewById(R.id.coordviewpager), "| " + String.valueOf(position) + " |   " + nameList[position], Snackbar.LENGTH_LONG).show();
                //Toast.makeText(ViewPagerClass.this, String.valueOf(position)+"---"+String.valueOf(filelist.length), Toast.LENGTH_SHORT).show();
                if (position == (filelist.length - 1))
                    gallery.setCurrentItem(1);
                if (position == 0)
                    gallery.setCurrentItem(filelist.length - 2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Toast.makeText(ViewPagerClass.this, String.valueOf(state), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
