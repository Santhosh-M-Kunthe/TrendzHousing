package in.siteurl.www.trendzcrm;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by SiteURL on 3/7/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    Context adapterContext;
    //ArrayList<DocumentsContent> adapterArrayList=new ArrayList<>();
    String[] strings;

    public ViewPagerAdapter(Context context, String[] arrayList) {
        strings = arrayList;
        adapterContext = context;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((CoordinatorLayout) object);
    }

    // initlaize every item to its respective view and widget
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(adapterContext).inflate(R.layout.image_gallery, container, false);
        ImageView gallery = (ImageView) view.findViewById(R.id.gallerylikeimageview);
        //  Glide.with(adapterContext).load(adapterArrayList.get(position).getImageURL())
        Glide.with(adapterContext).load(strings[position])
                .thumbnail(.05f)
                .placeholder(R.drawable.defaultofferimage)
                .into(gallery);

        ((ViewPager) container).addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((CoordinatorLayout) object);

    }
}
