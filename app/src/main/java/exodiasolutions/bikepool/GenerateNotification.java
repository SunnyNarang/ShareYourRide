package exodiasolutions.bikepool;

import android.annotation.SuppressLint;
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
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import exodiasolutions.bikepool.Config.Config;
import exodiasolutions.bikepool.MainActivity;
import exodiasolutions.bikepool.R;

public class GenerateNotification extends AsyncTask<String, Void, Bitmap> {

    Context ctx;
    String title;
    String message;

    public GenerateNotification(Context context) {
        super();
        this.ctx = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        InputStream in;
        Bitmap myBitmap=null;
        title = params[0];
        message = params[1];
        try {

            URL url = null;
            try {
                url = new URL(params[2]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();

                myBitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return myBitmap;




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        super.onPostExecute(bitmap);
        try {
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.bigPicture(bitmap);

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent intent = new Intent(ctx, YourRides.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent,0);

            NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "101";

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

                //Configure Notification Channel
                notificationChannel.setDescription("Game Notifications");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(Config.title)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(Config.content)
                    .setContentIntent(pendingIntent)
                    .setStyle(style)
                    .setLargeIcon(bitmap)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX);


            notificationManager.notify(1, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}