package in.siteurl.www.trendzcrm;

/**
 * Created by siteurl on 2/4/18.
 */

/*
public class UnitProjTabAdapter {
}
*/

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by SiteURL on 2/27/2018.
 */

public class UnitProjTabAdapter extends RecyclerView.Adapter<UnitProjTabAdapter.ViewHolder> {

    Context adapterContext;
    ArrayList<DocumentsContent> adapterArraylist;

    public UnitProjTabAdapter(Context context, ArrayList<DocumentsContent> arrayList) {
        adapterContext = context;
        adapterArraylist = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(adapterContext).inflate(R.layout.unit_proj_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(adapterContext).load(adapterArraylist.get(position).getImageURL().toString())
                .thumbnail(0.5f)
                .placeholder(R.drawable.defaultofferimage)
                //.apply(imageoptions)
                .into(holder.imageView);

        Glide.with(adapterContext).load(adapterArraylist.get(position).getImageURL().toString())
                .thumbnail(0.5f)
                //.apply(imageoptions)
                .into(holder.imageView_bg);

        holder.tyu.setText(adapterArraylist.get(position).getDocName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
   String[] stringList = new String[adapterArraylist.size() + 2];
                String[] nameList = new String[adapterArraylist.size() + 2];
                for (int w = 1; w < adapterArraylist.size() + 1; w++) {
                    stringList[w] = adapterArraylist.get(w - 1).getImageURL();
                    nameList[w] = adapterArraylist.get(w - 1).getDocName();
                }
                stringList[0] = stringList[adapterArraylist.size()];
                nameList[0] = nameList[adapterArraylist.size()];
                nameList[adapterArraylist.size() + 1] = nameList[1];
                stringList[adapterArraylist.size() + 1] = stringList[1];
                Intent intent = new Intent(adapterContext, ViewPagerClass.class);
                intent.putExtra("FILES_TO_SEND", stringList);
                intent.putExtra("FILES_TO_SENDa", nameList);

                intent.putExtra("position", position);
                adapterContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return adapterArraylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imageView_bg;
        TextView tyu;

        public ViewHolder(View itemView) {
            // initialize views and widgets
            super(itemView);
            imageView = itemView.findViewById(R.id.docContentImageView_unitprojfrag);
            imageView_bg = itemView.findViewById(R.id.docContentImageView_unitprojfrag_bg);
            tyu = itemView.findViewById(R.id.imgtv_unitprojfrag);

        }
    }


}
