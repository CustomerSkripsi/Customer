package mobi.garden.bottomnavigationtest.Model;

import android.text.format.DateFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatModel {

     public static String name;
     public static String message;
     public static long userId;
     public static long timestamp;
     public static String formattedTime;
     public static int rid;
     public static String rname;
     public static String status;
     public static String imageChat;
     public static String cid;

    public ChatModel() {

    }

   public ChatModel(String status, int userId, int rid){
        this.status = status;
        this.userId = userId;
        this.rid = rid;
   }


    public ChatModel(String status, String message, String name, long uid, int rid, String rname,
                     long timestamp, String formattedTime, String imageChat ) {
        this.status = status;
        this.name = name;
        this.message = message;
        this.userId = uid;
        this.rid = rid;

        this.rname = rname;
        this.timestamp = timestamp;
        this.formattedTime = formattedTime;
        this.imageChat =imageChat;
    }

    public ChatModel(String status, String message, String name, long uid, String cid, String rname,
                     long timestamp, String formattedTime, String imageChat ) {
        this.status = status;
        this.name = name;
        this.message = message;
        this.userId = uid;
        this.cid = cid;

        this.rname = rname;
        this.timestamp = timestamp;
        this.formattedTime = formattedTime;
        this.imageChat =imageChat;
    }
    public String getImageChat() {
        return imageChat;
    }

    public void setImageChat(String imageChat){
        this.imageChat = imageChat;
    }

    public  String getStatus() {
        return status;
    }

    public  void setStatus(String status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTime(long time) {
        this.timestamp = time;

        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long timeDifference = System.currentTimeMillis() - time;

        if(timeDifference < oneDayInMillis){
            formattedTime = DateFormat.format("hh:mm a", time).toString();
        }else{
            formattedTime = DateFormat.format("dd MMM - hh:mm a", time).toString();
        }
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getFormattedTime(){
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long timeDifference = System.currentTimeMillis() - timestamp;

        if(timeDifference < oneDayInMillis){
            return DateFormat.format("hh:mm a", timestamp).toString();
        }else{
            return DateFormat.format("dd MMM - hh:mm a", timestamp).toString();
        }
    }
}
