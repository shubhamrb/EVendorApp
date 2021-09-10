package com.dbcorp.vendorapp.fcm;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.dbcorp.vendorapp.R;
import com.google.android.gms.stats.GCoreWakefulBroadcastReceiver;

/**
 * Created by Bhupesh Sen on 06-05-2021.
 */
public class NotificationReceiver  extends GCoreWakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        playNotificationSound(context);
    }

    public void playNotificationSound(Context context) {
        try {
//            MediaPlayer player = MediaPlayer.create(context, R.raw.vsthree);
//            player.setLooping(false); // Set looping
//            player.setVolume(1000,1000);
//            player.start();
//

            context.startService(new Intent(context, BackgroundSoundService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
