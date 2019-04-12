package exodiasolutions.bikepool;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import exodiasolutions.bikepool.Custom.CEditText;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class RegisterActivity extends AppCompatActivity {

    TextView txtSinup,signin;


    CEditText email,pass ,Num, name,adhaar,address;
    // Creating Volley RequestQueue.
    com.android.volley.RequestQueue requestQueue;

    // Create string variable to hold the EditText Value.
    String User, Email, Pass,Phone,Name,Address,Adhaar;

    // Creating Progress dialog.
    android.app.ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        signin = findViewById(R.id.signinhere);
        txtSinup=(TextView)findViewById(R.id.signup1);
        // userName=(EditText)findViewById(R.id.username);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        Num=findViewById(R.id.no);
        name=findViewById(R.id.name);
        adhaar = findViewById(R.id.adhaar);
        address = findViewById(R.id.address);

        // Creating Volley newRequestQueue .
        requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(RegisterActivity.this);

        progressDialog = new android.app.ProgressDialog(RegisterActivity.this);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });


        // Adding click listener to button.
        txtSinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name.setError(null);
                email.setError(null);
                Num.setError(null);
                pass.setError(null);
                address.setError(null);
                adhaar.setError(null);

                if(Check_data(name)){if(Check_data(email)){if(Check_data(address)){if(Check_data(adhaar)&&adhaar.getText().toString().length()==12){if(Check_data(Num)&&Num.getText().toString().length()==10){if(Check_data(pass)&&pass.getText().toString().length()>=6){

                    // Showing progress dialog at user registration time.
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();

                    // Calling method to get value from EditText.
                    GetValueFromEditText();



                    StringRequest stringRequest = new StringRequest( com.android.volley.Request.Method.POST,"https://vintagevow-sunnynarang.legacy.cs50.io/bike_pool/register_new.php",
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {
                                    //  Toast.makeText(RegisterActivity.this, ""+ServerResponse, Toast.LENGTH_SHORT).show();
                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing response message coming from server.
                                    // Toast.makeText(RegisterActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                    try {
                                        JSONObject obj = new JSONObject(ServerResponse);
                                        if(obj.getString("success").equalsIgnoreCase("false")){
                                            Toast.makeText(RegisterActivity.this, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                        else if(obj.getString("success").equalsIgnoreCase("true"))
                                        {startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                            RegisterActivity.this.finish();
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(com.android.volley.VolleyError volleyError) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();
                                    volleyError.printStackTrace();
                                    // Showing error message if something goes wrong.
                                    android.widget.Toast.makeText(RegisterActivity.this, volleyError.toString(), android.widget.Toast.LENGTH_LONG).show();
                                }
                            })
                    {
                        @Override
                        protected java.util.Map<String, String> getParams() {

                            // Creating Map String Params.
                            java.util.Map<String, String> params = new java.util.HashMap<String, String>();

                            // Adding All values to Params.
                            params.put("Content-Type","application/json");
                            params.put("password", Pass);
                            params.put("email", Email);
                            params.put("number", Phone);
                            params.put("name", Name);
                            params.put("address", Address);
                            params.put("adhaar", Adhaar);



                            // {"password", Pass,"email", Email,"number", Phone,"name",Name}


                            return params;
                        }

                    };


                    // Creating RequestQueue.
                    RequestQueue requestQueue = newRequestQueue(RegisterActivity.this);

                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);


               /*
                final MyHttpClient myHttpClient = new MyHttpClient(RegisterActivity.this,"http://disastermaster.website/sandy/test/new/register_new.php",new String[]{"password", Pass,"email", Email,"number", Phone,"name",Name});
                myHttpClient.execute();
                myHttpClient.callback = new MyCallback() {
                    @Override
                    public void callbackCall() {
                        progressDialog.dismiss();
                        String ServerResponse = myHttpClient.result;
                        // Showing response message coming from server.
                         Toast.makeText(RegisterActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(ServerResponse);
                            if(obj.getString("success").equalsIgnoreCase("false")){
                                Toast.makeText(RegisterActivity.this, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else if(obj.getString("success").equalsIgnoreCase("true"))
                            {startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                RegisterActivity.this.finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
*/



                }
                else{pass.setError("Enter atleast 6 charachters");}
                }
                else{Num.setError("Enter 10 digits");}
                }
                else{
                    adhaar.setError("Enter Correct Number");
                }
                }
                }
                }

            }
        });}

    public void GetValueFromEditText(){

//            User = userName.getText().toString().trim();


        Email = email.getText().toString().trim();
        Pass = pass.getText().toString().trim();
        Phone = Num.getText().toString().trim();
        Name = name.getText().toString().trim();
        Address = address.getText().toString().trim();
        Adhaar = adhaar.getText().toString().trim();



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

