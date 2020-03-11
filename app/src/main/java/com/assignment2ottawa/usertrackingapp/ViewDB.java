package com.assignment2ottawa.usertrackingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/* View user activities from database */

public class ViewDB extends AppCompatActivity
{
    ArrayList<String> listItems=new ArrayList<String>();
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_db);
        UserTrackDB db=new UserTrackDB(this);
        db.open();
        final ListView activityListView=(ListView) findViewById(R.id.activityView) ;
        int count=1;

        List<UserActivity> listActivity= db.getAllActivities();
        for (UserActivity ua:listActivity)
        {
            if(ua.getEndTime()!=null && ua.getDuration()!=null)
            {
                listItems.add(count + ". " + ua.getActivity() + "(" + ua.getDuration() + ")\n" + "S.Time: " + ua.getStartTime() + "\n" + "E.Time: " + ua.getEndTime());
                count++;
            }
        }

        listAdapter=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, listItems);
        activityListView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }
}
