package edu.uw.servicedemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by iguest on 2/16/16.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private static final String TAG = "Music";
    private int NOTIFICATION_ID = 1;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.v(TAG, "MusicService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mediaPlayer = MediaPlayer.create(this, R.raw.verdi_la_traviata_brindisi_mit);
        mediaPlayer.setOnCompletionListener(this);

        String songName = "Verdi";
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                                    .addAction(android.R.drawable.ic_media_play, "Music Player", pendingIntent)
                                    .setContentText("Now Playing: " + songName)
                                    .setOngoing(true)
                                    .build();

        startForeground(NOTIFICATION_ID, notification);
        mediaPlayer.start();
        super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
        //return null;
    }

    private final IBinder mLocalBinder = new LocalBinder();

    public String sayHello(){
        return "Hello";
    }

    class LocalBinder extends Binder {

        //returns the current service
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopForeground(true);

        Log.v(TAG, "Stop pressed");

        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp){
        stopSelf();
    }


}
