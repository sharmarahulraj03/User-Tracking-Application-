package com.assignment2ottawa.usertrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/* Storing user activities in Database */

public class UserTrackDB extends SQLiteOpenHelper
{
    public static final String Column_Activity="activity";
    public static final String Column_StartTime="startTime";
    public static final String Column_Id="id";
    public static final String Column_CustomIdentifier="customIdentifier";
    public static final String Column_EndTime="endTime";
    public static final String Column_Duration="duration";
    private static final String Database_Name="useractivity.db";
    private static final String Datatable_Name="useractivity";
    private static final int Database_Version=1;
    SQLiteDatabase db;
    public static final String Tag= UserTrackDB.class.getSimpleName();
    public static final String CreateDatabase="create table useractivity (id integer primary key autoincrement, activity text not null, startTime text not null, endTime text, customIdentifier text,duration text)";


    public UserTrackDB(Context context)
    {
        super(context,Database_Name,null,Database_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS useractivity");
        onCreate(db);
    }

    public void open()
    {
        db = getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public void addUserActivity(UserActivity userActivity)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column_Activity, userActivity.getActivity());
        values.put(Column_StartTime, userActivity.getStartTime());
        values.put(Column_EndTime, userActivity.getEndTime());
        values.put(Column_CustomIdentifier, userActivity.getCustomIdentifier());
        values.put(Column_Duration,userActivity.getDuration());
        db.insert(Datatable_Name, null, values);
        db.close();
    }

    public void truncateDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Drop table if exists useractivity");
        db.execSQL(CreateDatabase);
        db.close();
    }

    public String[] getLastActivity()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] lastAct=new String[4];
        String selectQuery = "SELECT  * FROM useractivity where id= (Select MAX(id) from useractivity)";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst())
        {
            lastAct[0]="True";
            lastAct[1]=(cursor.getString(1));
            lastAct[2]=(cursor.getString(2));
        }
        else
        {
            lastAct[0]="False";
        }
        cursor.close();
        db.close();
        return  lastAct;
    }

    public List<UserActivity> getAllActivities()
    {
        List<UserActivity> activityList = new ArrayList<UserActivity>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Datatable_Name;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserActivity ua = new UserActivity();
                ua.setID(Integer.parseInt(cursor.getString(0)));
                ua.setActivity(cursor.getString(1));
                ua.setStartTime(cursor.getString(4));
                ua.setEndTime(cursor.getString(3));
                ua.setDuration(cursor.getString(5));

                activityList.add(ua);
            } while (cursor.moveToNext());
        }
        db.close();
        return activityList;

    }

    public void UpdateEndTime(String startTime, String endTime, String duration)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Column_EndTime, endTime);
        values.put(Column_Duration, duration);

        db.update(Datatable_Name, values, Column_StartTime+ "= "+ startTime,null);
        db.close();

    }


}
