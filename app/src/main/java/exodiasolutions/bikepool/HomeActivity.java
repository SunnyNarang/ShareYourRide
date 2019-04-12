package exodiasolutions.bikepool;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

import exodiasolutions.bikepool.Custom.Store;

public class HomeActivity extends AppCompatActivity {

    TextView name,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        name = findViewById(R.id.namev);
        email = findViewById(R.id.emailv);
        phone = findViewById(R.id.phonev);

        name.setText(new Store(HomeActivity.this).getValue("name"));
        email.setText("Email: "+new Store(HomeActivity.this).getValue("email"));
        phone.setText("Phone: "+new Store(HomeActivity.this).getValue("phone"));




    }
    public void logout(View v){

        FirebaseMessaging.getInstance().unsubscribeFromTopic(new Store(HomeActivity.this).getValue("userid"))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

        new Store(HomeActivity.this).setValue("username","");
        new Store(HomeActivity.this).setValue("login","0");
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
        i.putExtra("remember_not", true);
        startActivity(i);
        finish();
    }

    public void share(View v){

    startActivity(new Intent(HomeActivity.this, MapsActivity.class));

    }


    public void get(View v){

        startActivity(new Intent(HomeActivity.this,GetRidesActivity.class));

    }
    public void about(View v){}
    public void your(View v){

        startActivity(new Intent(HomeActivity.this,YourRides.class));


    }



}
