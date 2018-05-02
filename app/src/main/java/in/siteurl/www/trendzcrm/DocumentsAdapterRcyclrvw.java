package in.siteurl.www.trendzcrm;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SiteURL on 2/27/2018.
 */

public class DocumentsAdapterRcyclrvw extends RecyclerView.Adapter<DocumentsAdapterRcyclrvw.ViewHolder> {

    //    //document adapter global varialbles

    Context adapterContext;
    ArrayList<DocumentsContent> adapterArraylist;

    public DocumentsAdapterRcyclrvw(Context context, ArrayList<DocumentsContent> arrayList) {
        adapterContext = context;
        adapterArraylist = arrayList;
    }

    //view holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(adapterContext).inflate(R.layout.documents_content, parent, false);
        return new ViewHolder(view);
    }

    //bind view holder
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(adapterContext).load(adapterArraylist.get(position).getImageURL().toString())
                .thumbnail(0.5f)
                .placeholder(R.drawable.defaultofferimage)
                //.apply(imageoptions)
                .into(holder.imageView);

        holder.tyu.setText(adapterArraylist.get(position).getDocName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  //open image in gaallery-----------------------------------------------------------------------
                Uri myUri = Uri.parse(adapterArraylist.get(position).getImageURL());
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(myUri , "image/png");
                adapterContext.startActivity(intent);
*/
                // String[] stringList=new String[]{"http://media.jpegmini.com/images/JPEGmini_mac_300.jpg", "http://media.jpegmini.com/images/JPEGmini_mac_300.jpg", "http://media.jpegmini.com/images/JPEGmini_mac_300.jpg", "http://media.jpegmini.com/images/JPEGmini_mac_300.jpg", "http://media.jpegmini.com/images/JPEGmini_mac_300.jpg"};
                //stringList[stringList.length-2]="https://upload.wikimedia.org/wikipedia/commons/b/b4/JPEG_example_JPG_RIP_100.jpg";

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

        ImageView imageView;
        TextView tyu;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.docContentImageView);
            tyu = itemView.findViewById(R.id.imgtv);

        }
    }


}
