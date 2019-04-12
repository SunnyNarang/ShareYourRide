package exodiasolutions.bikepool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import exodiasolutions.bikepool.Custom.CEditText;
import exodiasolutions.bikepool.Custom.Store;

public class GetRidesActivity extends AppCompatActivity {
    ArrayList<Ride> arraylist = new ArrayList<>();
    ListView list;
    ProgressDialog dialog,dialog2;
    CustomAdapter3 adapter;
    AlertDialog.Builder builder;
    CEditText des;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_rides);
        getSupportActionBar().hide();
        dialog = new ProgressDialog(this);
        dialog2 = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);
        list = (ListView) findViewById(R.id.topic_list);
        des = findViewById(R.id.des);

        des.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                arraylist.clear();
                getdata(s.toString());

            }
        });

        getdata("");

    }

    class CustomAdapter3 extends ArrayAdapter<Ride> {
        Context c;

        public CustomAdapter3(Context context, ArrayList<Ride> arrayList) {
            super(context, R.layout.card, arrayList);
            this.c = context;
        }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {

            LayoutInflater li = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.card, parent, false);

            final Ride ride = getItem(pos);

            ImageView map = convertView.findViewById(R.id.map);
            TextView file_name1 = (TextView) convertView.findViewById(R.id.topic_name1);
            TextView file_name2 = (TextView) convertView.findViewById(R.id.topic_name2);
            TextView file_name3 = (TextView) convertView.findViewById(R.id.topic_name3);
            TextView file_name4 = (TextView) convertView.findViewById(R.id.topic_name4);
            TextView file_name5 = (TextView) convertView.findViewById(R.id.topic_name5);
            TextView file_name6 = (TextView) convertView.findViewById(R.id.topic_name6);
            TextView file_name7 = (TextView) convertView.findViewById(R.id.topic_name7);


            file_name1.setText("Name: " + Html.fromHtml(ride.getName()));
            file_name2.setText("Number: " + Html.fromHtml(ride.getPhone()));
            file_name3.setText("Source: " + Html.fromHtml(ride.getFrom()));
            file_name4.setText("Destination: " + Html.fromHtml(ride.getTo()));
            file_name5.setText("Date: " + Html.fromHtml(ride.getDatetime().split(" ")[0]));
            file_name6.setText("Time: " + Html.fromHtml(ride.getDatetime().split(" ")[1]));
            file_name7.setText("Bike No. : " + Html.fromHtml(ride.getBikenumber()));

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(GetRidesActivity.this,ShowMaps.class);
                    i.putExtra("s_long",ride.getS_long());
                    i.putExtra("s_lat",ride.getS_lat());
                    i.putExtra("d_long",ride.getD_long());
                    i.putExtra("d_lat",ride.getD_lat());
                    startActivity(i);
                    //MapsActivity.this.finish();


                }
            });

            return convertView;

        }
    }

    public void getdata(final String des){
        arraylist.clear();
        if(adapter!=null)
        adapter.clear();
        adapter=new CustomAdapter3(GetRidesActivity.this,arraylist);
        list.setAdapter(adapter);

        final MyHttpClient myHttpClient = new MyHttpClient(this,"https://vintagevow-sunnynarang.legacy.cs50.io/bike_pool/getrides.php",new String[]{"des",des});
        myHttpClient.execute();
        if(des.equalsIgnoreCase("")){
        dialog.setMessage("please wait..");
        dialog.show();}
        myHttpClient.callback = new MyCallback() {
            @Override
            public void callbackCall() {
                if(des.equalsIgnoreCase(""))
                dialog.dismiss();
                try {
                    JSONArray array = new JSONArray(myHttpClient.result);
                    arraylist.clear();
                    for(int i =0 ;i< array.length();i++){
                        JSONObject obj = array.getJSONObject(i);
                        if(!obj.getString("username").equalsIgnoreCase(new Store(GetRidesActivity.this).getValue("userid"))) {
                            arraylist.add(new Ride(obj.getString("id"), obj.getString("name"), obj.getString("source"), obj.getString("destination"), obj.getString("phone"), obj.getString("bikenumber"), obj.getString("datetime"), obj.getString("s_long"), obj.getString("s_lat"), obj.getString("d_long"), obj.getString("d_lat")));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter=new CustomAdapter3(GetRidesActivity.this,arraylist);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Ride ride = arraylist.get(position);
                        //builder.setMessage("Do you want to confirm ride?") .setTitle("Confirmation");

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to confirm your ride from "+ride.getFrom()+" to "+ride.getTo()+"?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        final MyHttpClient myHttpClient1 = new MyHttpClient(GetRidesActivity.this,"https://vintagevow-sunnynarang.legacy.cs50.io/bike_pool/bookride.php",new String[]{
                                                "id",ride.getId(),
                                                "username",new Store(GetRidesActivity.this).getValue("userid")
                                        });
                                        myHttpClient1.execute();
                                        dialog2.setMessage("Booking Ride..");
                                        dialog2.show();

                                        myHttpClient1.callback = new MyCallback() {
                                            @Override
                                            public void callbackCall() {
                                                dialog2.dismiss();
                                                if(myHttpClient1.result.endsWith("1")){
                                                    Toast.makeText(GetRidesActivity.this, "Booking Done.", Toast.LENGTH_SHORT).show();
                                                    GetRidesActivity.this.finish();
                                                }
                                                else {
                                                    Toast.makeText(GetRidesActivity.this, "Booking Failed!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        };


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                        Toast.makeText(getApplicationContext(),"Ride Canceled",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle(ride.getFrom()+" - "+ride.getTo());
                        alert.show();

                    }
                });

            }
        };

    }

}
