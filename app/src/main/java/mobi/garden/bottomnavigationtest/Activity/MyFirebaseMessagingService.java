package mobi.garden.bottomnavigationtest.Activity;



import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("notification","dapet");
        if(!remoteMessage.getData().isEmpty()){
            Log.e(TAG, "Data Payload data: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotificationChat(json);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("matatag", e+"");
            }
        }else{
            Log.d(TAG, "Data Payload body: " + remoteMessage.getNotification().getBody());
            sendNotif(remoteMessage.getNotification().getTitle()
                    ,remoteMessage.getNotification().getBody());
        }
    }

    private void sendPushNotificationChat(JSONObject json) {
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager myNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);

            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                myNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                myNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: $e");
        }catch (Exception e){
            Log.e(TAG, "Json Exception: $e");
        }

    }

    public void sendNotif(String title,String string){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyNotificationManager myNotificationManager = new MyNotificationManager(getApplicationContext());
        myNotificationManager.showSmallNotification(title, string, intent);
    }


//    private void sendNotification(String title, String messageBody) {Intent intent = new Intent(this, ChatActivity.class);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//
//        //TODO: kalo API diatas OREO
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int notificationId = 1;
//        String channelId = "channel-01";
//        String channelName = "Channel Name";
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(
//                    channelId, channelName, importance);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//        //End#
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this,channelId)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        notificationManager.notify(notificationId, notificationBuilder.build());
//
//    }
}
