package in.siteurl.www.trendzcrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by SiteURL on 3/10/2018.
 */

// adapter for offer list
public class ViewPageOfferAdapter extends PagerAdapter {
    Context context;//ArrayList<ViewPageOfferContentds> arrayList=new ArrayList<>();

    String[] arrayListabc;

    public ViewPageOfferAdapter(Context context, String[] viewPageOfferContentdsArrayList) {
        this.context = context;
        arrayListabc = viewPageOfferContentdsArrayList;
    }

    @Override
    public int getCount() {
        //Toast.makeText(context, "new count"+String.valueOf(arrayListabc.length), Toast.LENGTH_SHORT).show();
        return arrayListabc.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (CoordinatorLayout) object;
    }

    // initialize items to views and widgets
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.offerviewpageritem, container, false);
        ImageView offer = (ImageView) view.findViewById(R.id.offerviewpagerimageview);
        ImageView offerCrop = (ImageView) view.findViewById(R.id.offerviewpagerimageviewCenterCrop);
        //Glide.with(context).load(arrayList.get(position).getOfferImageURL().toString()).thumbnail(0.05f).into(offer);
        Glide.with(context).load(arrayListabc[position]).placeholder(R.drawable.defaultofferimage).thumbnail(0.05f).into(offer);
        Glide.with(context).load(arrayListabc[position]).thumbnail(0.05f).into(offerCrop);
        //((TextView)view.findViewById(R.id.offerviewpagertv)).setText(arrayList.get(position).getOfferName().toString());
        ((TextView) view.findViewById(R.id.offerviewpagertv)).setText(arrayListabc[position]);

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "999999999999999999999999", Toast.LENGTH_SHORT).show();
                Intent toOffers = new Intent(context, Offers.class);
                toOffers.putExtra("itno", position);
                context.startActivity(toOffers);
            }
        });
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((CoordinatorLayout) object);

    }
}
