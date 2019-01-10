package mobi.garden.bottomnavigationtest.Activity;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import mobi.garden.bottomnavigationtest.R;

public class MyNotificationManager {

    private Context context;
    private int ID_BIG_NOTIFICATION = 23;
    private int ID_SMALL_NOTIFICATION = 235;

    public MyNotificationManager(Context context) {
        this.context = context;
    }

    public void showBigNotification(String title, String message, String url, Intent intent){
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context,ID_BIG_NOTIFICATION,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);
        Notification notification = mBuilder.setSmallIcon(R.mipmap.logo_century).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.logo_century)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_century))
                .setContentText(message)
                .build();
        notification.flags =  Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    public void showSmallNotification(String title, String message, Intent intent){
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context,ID_SMALL_NOTIFICATION,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, channelId);
        new NotificationCompat.Builder(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
           notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = mBuilder.setSmallIcon(R.mipmap.logo_century).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.mipmap.logo_century)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_century))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .build();

        notification.flags =  Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

    private Bitmap getBitmapFromURL(String strURL) {
        InputStream input = null;
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            return  BitmapFactory.decodeStream(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
          return  BitmapFactory.decodeStream(input);
    }


}
