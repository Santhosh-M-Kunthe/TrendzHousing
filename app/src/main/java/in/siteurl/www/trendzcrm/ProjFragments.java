package in.siteurl.www.trendzcrm;

/**
 * Created by siteurl on 2/4/18.
 */
/*
public class ProjFragments {
}*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by siteurl on 30/3/18.
 */

public class ProjFragments extends Fragment {

    public ProjFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //    String [] unitNameList,unitURLList,projNameList,projURLList;

        String[] projNameList = getArguments().getStringArray("projNameList");
        String[] projURLList = getArguments().getStringArray("projURLList");
        ArrayList<DocumentsContent> PicsArrayList = new ArrayList<>();
        for (int i = 0; i < projNameList.length; i++)
            PicsArrayList.add(new DocumentsContent(projURLList[i], projNameList[i]));
        View view = inflater.inflate(R.layout.unit_pics_fragment, container, false);
        RecyclerView docPicsRecyclerView = (RecyclerView) view.findViewById(R.id.docsDocumentsRecyclerView_frag);
        docPicsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        UnitProjTabAdapter documentsAdapterRcyclrvw = new UnitProjTabAdapter(getContext(), PicsArrayList);
        docPicsRecyclerView.setAdapter(documentsAdapterRcyclrvw);
        return view;
    }

}