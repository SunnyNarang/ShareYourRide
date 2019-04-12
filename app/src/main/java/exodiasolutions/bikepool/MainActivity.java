package exodiasolutions.bikepool;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import exodiasolutions.bikepool.Custom.Store;

public class MainActivity extends AppCompatActivity {
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(2000);

                    if (new Store(MainActivity.this).getValue("login")!=null&&new Store(MainActivity.this).getValue("login").equalsIgnoreCase("1")
                            && new Store(MainActivity.this).getValue("username").length()>0) {


                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();


                    }

                    else {

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }



                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }
}
