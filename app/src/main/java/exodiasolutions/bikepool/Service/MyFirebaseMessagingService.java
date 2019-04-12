package exodiasolutions.bikepool.Service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import exodiasolutions.bikepool.GenerateNotification;
import exodiasolutions.bikepool.Config.Config;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
    }

    private static final String TAG = "MyFirebaseMessagingServ";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Refreshed token: " + remoteMessage.getMessageId());

        if(remoteMessage.getData().size() > 0)
            getImage(remoteMessage);
    }

    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.content = data.get("content");
        Config.imageUrl = data.get("imageUrl");
        Config.gameUrl = data.get("gameUrl");

        Log.d(TAG,data.get("title"));
        Log.d(TAG,data.get("content"));
        Log.d(TAG,data.get("imageUrl"));
        Log.d(TAG,data.get("gameUrl"));

        GenerateNotification generateNotification = new GenerateNotification(getApplicationContext());
        generateNotification.execute(data.get("title"),data.get("content"),data.get("imageUrl"));

    }
}