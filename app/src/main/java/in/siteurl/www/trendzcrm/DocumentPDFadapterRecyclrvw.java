package in.siteurl.www.trendzcrm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SiteURL on 2/27/2018.
 */

public class DocumentPDFadapterRecyclrvw extends RecyclerView.Adapter<DocumentPDFadapterRecyclrvw.ViewHolder> {

    ArrayList<DocumentsContent> PDFarraylist=new ArrayList<>();
    Context PDFcontext;
    public DocumentPDFadapterRecyclrvw(Context context, ArrayList<DocumentsContent> arrayList) {
        PDFcontext=context;
        PDFarraylist=arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(PDFcontext).inflate(R.layout.pdf_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+PDFarraylist.get(position).getImageURL());
        holder.pdfName.setText(PDFarraylist.get(position).getDocName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PDFcontext, "Chrome works best for this option. . .", Toast.LENGTH_LONG).show();
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PDFarraylist.get(position).getImageURL()));
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://drive.google.com/viewerng/viewer?embedded=true&url="+PDFarraylist.get(position).getImageURL()));
                PDFcontext.startActivity(browserIntent);*/
                Intent intent=new Intent(PDFcontext,PDFview.class);
                intent.putExtra("URLpdf","http://drive.google.com/viewerng/viewer?embedded=true&url="+PDFarraylist.get(position).getImageURL());
                PDFcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PDFarraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // declare and intialize views
        WebView webview;
        ImageView imageView;
        TextView pdfName;
        public ViewHolder(View itemView) {
            super(itemView);
            webview = (WebView) itemView.findViewById(R.id.pdfviewer);
            pdfName=(TextView) itemView.findViewById(R.id.pdftv);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            imageView=itemView.findViewById(R.id.overlapimageview);

       /*     String pdf = "http://www.adobe.com/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
            webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
       */ }
    }
}