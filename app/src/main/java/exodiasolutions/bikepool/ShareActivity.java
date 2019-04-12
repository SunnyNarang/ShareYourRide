package exodiasolutions.bikepool;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;

import exodiasolutions.bikepool.Custom.Store;




public class ShareActivity extends AppCompatActivity implements
        View.OnClickListener {
    Button  btnTimePicker;
    RadioGroup radioGroup;
    TextView  txtTime;
    boolean time_select = false;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String sHour,sminute;
    RadioButton genderradioButton;
    TextView name,phone;
    boolean today = true;
    EditText from,to,bikenumber;
    String day;
    ProgressDialog progressDialog;
    int PLACE_PICKER_REQUEST = 1;
    String s_long,s_lat,d_long,d_lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        bikenumber = findViewById(R.id.bikenumber);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtTime=findViewById(R.id.in_time);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        progressDialog = new ProgressDialog(this);
        btnTimePicker.setOnClickListener(this);

        s_long = getIntent().getStringExtra("s_long");
        s_lat = getIntent().getStringExtra("s_lat");
        d_long = getIntent().getStringExtra("d_long");
        d_lat = getIntent().getStringExtra("d_lat");


        name.setText("Name: "+ new Store(ShareActivity.this).getValue("name"));
        phone.setText("Phone: "+new Store(ShareActivity.this).getValue("phone"));





    }

    public void shareride(View v){
        
        if(time_select) {

            if (from.getText().toString().length() > 0 && to.getText().toString().length() > 0 && bikenumber.getText().toString().length() > 0) {
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                if (onclickbuttonMethod().equalsIgnoreCase("Tomorrow")) {
                    today = false;
                    day = "0";
                } else {
                    today = true;
                    day = "1";
                }
                if (sHour.length() == 1) {
                    sHour = "0" + sHour;
                }
                if (sminute.length() == 1) {
                    sminute = "0" + sminute;
                }

                final MyHttpClient myHttpClient = new MyHttpClient(ShareActivity.this, "https://vintagevow-sunnynarang.legacy.cs50.io/bike_pool/offer.php", new String[]{
                        "username", new Store(this).getValue("userid"),
                        "source", from.getText().toString(),
                        "destination", to.getText().toString(),
                        "bikenumber", bikenumber.getText().toString(),
                        "datetime", day + "," + sHour + "," + sminute,
                        "s_long", s_long,
                        "s_lat", s_lat,
                        "d_long", d_long,
                        "d_lat", d_lat,
                });

                myHttpClient.execute();

                myHttpClient.callback = new MyCallback() {
                    @Override
                    public void callbackCall() {
                        progressDialog.dismiss();
                        if (myHttpClient.result.trim().equalsIgnoreCase("1")) {
                            Toast.makeText(ShareActivity.this, "Ride Offered", Toast.LENGTH_SHORT).show();
                            ShareActivity.this.finish();
                        } else
                            Toast.makeText(ShareActivity.this, "Error, Try Again!", Toast.LENGTH_SHORT).show();
                    }
                };

            } else {
                Toast.makeText(this, "Fill all details!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please Select Time.", Toast.LENGTH_SHORT).show();
        }
    }
    public String onclickbuttonMethod(){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        return  genderradioButton.getText().toString();

    }

    @Override
    public void onClick(View v) {
        
        if (v == btnTimePicker) {
            if(onclickbuttonMethod().equalsIgnoreCase("Tomorrow")){
                today = false;
            }else {
                today = true;
            }
            
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            time_select = true;
                            
                            if(today)
                            {if((mHour<hourOfDay||(mHour==hourOfDay&&mMinute<minute))) {
                                sHour = hourOfDay+"";
                                sminute = minute+"";
                                txtTime.setText(hourOfDay + ":" + minute);
                            }else{
                                Toast.makeText(ShareActivity.this, "This time can't be selected", Toast.LENGTH_SHORT).show();
                            }}
                            else{
                                sHour = hourOfDay+"";
                                sminute = minute+"";
                                txtTime.setText(hourOfDay + ":" + minute);
                            }


                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }





}
