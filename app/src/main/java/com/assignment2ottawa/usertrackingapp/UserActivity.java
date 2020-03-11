package com.assignment2ottawa.usertrackingapp;

/* User activities tracking */
public class UserActivity
{
    int _id;
    String _customIdentifier;
    String _startTime;
    String _endTime;
    String _activity;
    String _duration;

    public void UserActivity(){}

    public void UserActivity(int _id, String _startTime, String _endTime, String _activity,String _customIdentifier, String _duration)
    {
        this._id=_id;
        this._startTime=_startTime;
        this._endTime=_endTime;
        this._activity=_activity;
        this._customIdentifier=_customIdentifier;
        this._duration=_duration;
    }

    public void UserActivity(String _startTime, String _endTime,String _activity, String _customIdentifier, String _duration)
    {
        this._startTime=_startTime;
        this._endTime=_endTime;
        this._activity=_activity;
        this._customIdentifier=_customIdentifier;
        this._duration=_duration;
    }

    public int getID() {
        return this._id;
    }
    public void setID(int id) {
        this._id = id;
    }

    public String getCustomIdentifier() {
        return this._customIdentifier;
    }
    public void setCustomIdentifier(String customIdentifier){this._customIdentifier = customIdentifier;}


    public String getStartTime() {
        return this._startTime;
    }
    public void setStartTime(String _startTime) {this._startTime = _startTime;}


    public String getEndTime() {
        return this._endTime;
    }
    public void setEndTime(String endTime) {
        this._endTime = endTime;
    }

    public String getActivity() {
        return this._activity;
    }
    public void setActivity(String activity) {this._activity = activity;}

    public String getDuration() {
        return this._duration;
    }
    public void setDuration(String _duration) {this._duration = _duration;}
}
