package org.echomobile.refer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import org.echomobile.refer.adapters.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Campaign extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle("Campaign List");
        toolbar.setLogo(R.mipmap.ic_launcher);

        // get the ListView
        expListView=(ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        //setting list adapter
        expListView.setAdapter(listAdapter);
    }

    //Preparing the list data

    private void prepareListData(){
        listDataHeader =new ArrayList<String>();
        listDataChild=new HashMap<String, List<String>>();
        //Adding child data - Populate List with Campaign Headers
        listDataHeader.add("Campaign 1");
        //Adding child data
        List<String> campaign = new ArrayList<String>();
        campaign.add("Refer your friends to buy a DSTV and get your monthly subscription for free");
        campaign.add("Number of Referrals =25");
        campaign.add("Total Rewards : 1000 MB");

        listDataChild.put(listDataHeader.get(0),campaign);
    }
}
