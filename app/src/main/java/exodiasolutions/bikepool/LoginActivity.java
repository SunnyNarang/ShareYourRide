package exodiasolutions.bikepool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;


import exodiasolutions.bikepool.Custom.Payment;
import exodiasolutions.bikepool.Custom.Store;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class LoginActivity extends AppCompatActivity {



    TextView txtSinup,txtSinin,forgot;

    EditText email,pass;

    // Creating Volley RequestQueue.
    com.android.volley.RequestQueue requestQueue;

    // Create string variable to hold the EditText Value.
    String Email, Pass;
    String id = "";
    // Creating Progress dialog.
    android.app.ProgressDialog progressDialog;

    // Storing server url into String variable.
    String HttpUrl = "https://vintagevow-sunnynarang.legacy.cs50.io/bike_pool/login.php";
    String HttpUrlforgot = "https://vintagevow-sunnynarang.legacy.cs50.io/bike_pool/forgot.php";
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtSinup=(TextView)findViewById(R.id.create);
        txtSinin=(TextView)findViewById(R.id.signin1);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.password);
        forgot=(TextView) findViewById(R.id.forgot);
        dialog = new ProgressDialog(this);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setError(null);
                if(email.getText().toString().equalsIgnoreCase("")||email==null){
                    email.setError("Fill this field");


                }
                else {
                    dialog.setMessage("Sending Mail, please wait.");
                    dialog.show();

                    // Creating string request with post method.
                    StringRequest stringRequest = new StringRequest( com.android.volley.Request.Method.POST,HttpUrlforgot,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {
                                    //  Toast.makeText(LoginActivity.this, ""+ServerResponse, Toast.LENGTH_SHORT).show();
                                    // Hiding the progress dialog after all task complete.
                                    dialog.dismiss();
                                    if(ServerResponse.equalsIgnoreCase("1")){
                                        Toast.makeText(LoginActivity.this, "Password sent to your email", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(ServerResponse.equalsIgnoreCase("10")){
                                        Toast.makeText(LoginActivity.this, "Email Not Registered", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }

                                    // Showing response message coming from server.
                                    // Toast.makeText(LoginActivity.this, ServerResponse, Toast.LENGTH_LONG).show();

                                    //startActivity(new Intent(LoginActivity.this,SpashActivity.class));
                                }
                            },
                            new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(com.android.volley.VolleyError volleyError) {
                                    dialog.dismiss();
                                    volleyError.printStackTrace();
                                    // Showing error message if something goes wrong.
                                    android.widget.Toast.makeText(LoginActivity.this, volleyError.toString(), android.widget.Toast.LENGTH_LONG).show();
                                }
                            })
                    {
                        @Override
                        protected java.util.Map<String, String> getParams() {

                            // Creating Map String Params.
                            java.util.Map<String, String> params = new java.util.HashMap<String, String>();

                            // Adding All values to Params.

                            params.put("email", email.getText().toString());


                            return params;
                        }

                    };


                    // Creating RequestQueue.
                    RequestQueue requestQueue = newRequestQueue(LoginActivity.this);

                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);



                }

            }
        });

        // Creating Volley newRequestQueue .
        requestQueue = newRequestQueue(LoginActivity.this);

        progressDialog = new android.app.ProgressDialog(LoginActivity.this);







        txtSinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                LoginActivity.this.finish();
            }
        });

        txtSinin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(LoginActivity.this,SpashActivity.class));

                // Showing progress dialog at user registration time.
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                // Calling method to get value from EditText.
                GetValueFromEditText();

                // Creating string request with post method.
                StringRequest stringRequest = new StringRequest( com.android.volley.Request.Method.POST,HttpUrl,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String ServerResponse) {
                                //  Toast.makeText(LoginActivity.this, ""+ServerResponse, Toast.LENGTH_SHORT).show();
                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                try {
                                    JSONObject jsonObject = new JSONObject(ServerResponse);
                                    String success = jsonObject.getString("success");
                                    if (success.equalsIgnoreCase("true")){


                                        new Store(LoginActivity.this).setValue("username",Email);
                                        new Store(LoginActivity.this).setValue("login","1");
                                        new Store(LoginActivity.this).setValue("email",jsonObject.getString("email"));
                                        new Store(LoginActivity.this).setValue("name",jsonObject.getString("name"));
                                        new Store(LoginActivity.this).setValue("phone",jsonObject.getString("phone"));
                                        new Store(LoginActivity.this).setValue("userid",jsonObject.getString("id"));

                                        FirebaseMessaging.getInstance().subscribeToTopic(jsonObject.getString("id"))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        String msg = "Subscribed";
                                                        if (!task.isSuccessful()) {
                                                            msg = "NOT Subscribed";
                                                        }

                                                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        if(jsonObject.getString("id_type").equalsIgnoreCase("1")){
                                            Payment.PAID_INFO =1;
                                            new Store(LoginActivity.this).setValue("paid","1");
                                        }
                                        else{
                                            Payment.PAID_INFO =0;
                                            new Store(LoginActivity.this).setValue("paid","0");
                                        }


                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        LoginActivity.this.finish();

                                    }
                                    else if(success.equalsIgnoreCase("notactive")){
                                        //Toast.makeText(LoginActivity.this, "Account Not active", Toast.LENGTH_SHORT).show();
                                        /*Intent i = new Intent(LoginActivity.this,VerifyAccount.class);
                                        i.putExtra("Email",Email);
                                        startActivity(i);*/

                                    }

                                    else{

                                        Toast.makeText(LoginActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // Showing response message coming from server.
                                // Toast.makeText(LoginActivity.this, ServerResponse, Toast.LENGTH_LONG).show();

                                //startActivity(new Intent(LoginActivity.this,SpashActivity.class));
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(com.android.volley.VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();
                                volleyError.printStackTrace();
                                // Showing error message if something goes wrong.
                                android.widget.Toast.makeText(LoginActivity.this, volleyError.toString(), android.widget.Toast.LENGTH_LONG).show();
                            }
                        })
                {
                    @Override
                    protected java.util.Map<String, String> getParams() {

                        // Creating Map String Params.
                        java.util.Map<String, String> params = new java.util.HashMap<String, String>();

                        // Adding All values to Params.





                        params.put("password", Pass);
                        params.put("email", Email);
                        params.put("device_id", "1");


                        return params;
                    }

                };


                // Creating RequestQueue.
                RequestQueue requestQueue = newRequestQueue(LoginActivity.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);
                //   Toast.makeText(LoginActivity.this, ""+id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GetValueFromEditText(){


        Email = email.getText().toString().trim();
        Pass = pass.getText().toString().trim();
        if(new Store(LoginActivity.this).getValue("device_id")!=null){
            id= new Store(LoginActivity.this).getValue("device_id");
            id = "1";
        }

    }

    boolean Check_data(EditText et){
        if(et.getText().toString().equalsIgnoreCase("")||et==null){
            et.setError("Fill this field");
            return false;
        }
        else{
            return true;
        }
    }
}

