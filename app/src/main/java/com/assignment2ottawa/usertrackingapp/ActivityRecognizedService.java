package com.assignment2ottawa.usertrackingapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Calendar;
import java.util.List;

/* Tracking activities check*/

public class ActivityRecognizedService extends IntentService
{

    String lastStartTime=new String();
    UserTrackDB db=new UserTrackDB(this);

    String lastActivity=new String();
    String duration=new String();
    Boolean firstActivityFlag;
    public ActivityRecognizedService()
    {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(ActivityRecognitionResult.hasResult(intent))
        {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }
/* Using switch track the activity whether it is a still, driving, running or walking */

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {

        for( DetectedActivity activity : probableActivities ) {

            switch( activity.getType() )
            {
                case DetectedActivity.IN_VEHICLE:
                {
                    Log.d( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 50 )
                    {
                        db.open();
                        String[] lastActDetails=db.getLastActivity();
                        if(lastActDetails[0].equals("True"))
                        {
                            firstActivityFlag=false;
                            lastActivity=lastActDetails[1];
                            lastStartTime=lastActDetails[2];
                        }
                        else
                        {
                            firstActivityFlag=true;
                            lastActivity="First Activity";

                        }
                        db.close();
                        if(!lastActivity.equals("Driving"))
                        {
                            if (!firstActivityFlag.equals((true)))
                            {
                                duration = timeDifference(lastStartTime, Calendar.getInstance().getTime().getTime());
                                db.open();
                                db.UpdateEndTime(lastStartTime,Calendar.getInstance().getTime().toString() ,duration);
                                db.close();
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                            builder.setContentText("Are you in vehicle?");
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setContentTitle(getString(R.string.app_name));
                            NotificationManagerCompat.from(this).notify(0, builder.build());

                            if (firstActivityFlag.equals(true))
                            {
                                String[] lastActivityInfo = {"True", lastActivity, duration};
                                startActivity(new Intent(this, Driving.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            } else
                            {
                                String[] lastActivityInfo = {"False", lastActivity, duration};
                                startActivity(new Intent(this, Driving.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            }
                        }
                    }
                    break;
                }

                case DetectedActivity.RUNNING:
                {
                    Log.d( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 25 )
                    {
                        db.open();
                        String[] lastActDetails=db.getLastActivity();
                        if(lastActDetails[0].equals("True"))
                        {
                            firstActivityFlag=false;
                            lastActivity=lastActDetails[1];
                            lastStartTime=lastActDetails[2];
                        }
                        else
                        {
                            firstActivityFlag=true;
                            lastActivity="First Activity";

                        }
                        db.close();
                        if(!lastActivity.equals("Running"))
                        {
                            if (!firstActivityFlag.equals(true))
                            {
                                duration = timeDifference(lastStartTime, Calendar.getInstance().getTime().getTime());
                                db.open();
                                db.UpdateEndTime(lastStartTime,Calendar.getInstance().getTime().toString() ,duration);
                                db.close();
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                            builder.setContentText("Are you running?");
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setContentTitle(getString(R.string.app_name));
                            NotificationManagerCompat.from(this).notify(0, builder.build());

                            if (firstActivityFlag.equals(true)) {
                                String[] lastActivityInfo = {"True", lastActivity, duration};
                                startActivity(new Intent(this, Running.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            } else {
                                String[] lastActivityInfo = {"False", lastActivity, duration};
                                startActivity(new Intent(this, Running.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            }
                        }
                    }
                    break;
                }

                case DetectedActivity.STILL:
                {
                    Log.d( "ActivityRecogition", "Still: " + activity.getConfidence() );

                    if( activity.getConfidence() >= 25 )
                    {
                        db.open();
                        String[] lastActDetails=db.getLastActivity();
                        if(lastActDetails[0].equals("True"))
                        {
                            firstActivityFlag=false;
                            lastActivity=lastActDetails[1];
                            lastStartTime=lastActDetails[2];
                        }
                        else
                        {
                            firstActivityFlag=true;
                            lastActivity="First Activity";

                        }
                        db.close();
                        Log.d("Last Activity", lastActivity);
                        if(!lastActivity.equals("Still"))
                        {
                            if (!firstActivityFlag.equals((true)))
                            {
                                duration = timeDifference(lastStartTime, Calendar.getInstance().getTime().getTime());
                                db.open();
                                db.UpdateEndTime(lastStartTime,Calendar.getInstance().getTime().toString() ,duration);
                                db.close();
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                            builder.setContentText("Are you still?");
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setContentTitle(getString(R.string.app_name));
                            NotificationManagerCompat.from(this).notify(0, builder.build());

                            if (firstActivityFlag.equals(true))
                            {
                                String[] lastActivityInfo = {"True", lastActivity, duration};
                                startActivity(new Intent(this, Still.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            }
                            else
                            {
                                String[] lastActivityInfo = {"False", lastActivity, duration};
                                startActivity(new Intent(this, Still.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            }
                        }
                    }
                    break;
                }

                case DetectedActivity.WALKING: {
                    Log.d( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 25 )
                    {
                        db.open();
                        String[] lastActDetails=db.getLastActivity();
                        if(lastActDetails[0].equals("True"))
                        {
                            firstActivityFlag=false;
                            lastActivity=lastActDetails[1];
                            lastStartTime=lastActDetails[2];
                        }
                        else
                        {
                            firstActivityFlag=true;
                            lastActivity="First Activity";

                        }
                        db.close();
                        if(!lastActivity.equals("Walking"))
                        {
                            if (!firstActivityFlag.equals((true)))
                            {
                                duration = timeDifference(lastStartTime, Calendar.getInstance().getTime().getTime());
                                db.open();
                                db.UpdateEndTime(lastStartTime,Calendar.getInstance().getTime().toString() ,duration);
                                db.close();
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                            builder.setContentText("Are you walking?");
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setContentTitle(getString(R.string.app_name));
                            NotificationManagerCompat.from(this).notify(0, builder.build());

                            if (firstActivityFlag.equals(true)) {
                                String[] lastActivityInfo = {"True", lastActivity, duration};
                                startActivity(new Intent(this, Walking.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            } else {
                                String[] lastActivityInfo = {"False", lastActivity, duration};
                                startActivity(new Intent(this, Walking.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("lastActivityInfo", lastActivityInfo));
                            }
                        }
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.d( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    break;
                }
            }
        }
    }

    public String timeDifference(String startDate, Long endDate){

        long different = endDate - Long.parseLong(startDate);

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        if(elapsedDays==0)
        {
            if(elapsedHours==0)
            {
                if(elapsedMinutes==0)
                {
                    return elapsedSeconds + " Seconds";
                }
                else
                {
                    return elapsedMinutes + " Minutes and " + elapsedSeconds + " Seconds";
                }
            }
            else
            {
                return elapsedHours + " Hours " + elapsedMinutes + " Minutes and " + elapsedSeconds + " Seconds";
            }
        }
        else
        {
            return elapsedDays + " Days, " + elapsedHours + " Hours " + elapsedMinutes + " Minutes and " + elapsedSeconds + " Seconds";
        }

    }
}
