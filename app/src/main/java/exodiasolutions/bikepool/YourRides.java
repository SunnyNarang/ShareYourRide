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
import android.text.Html;
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

import exodiasolutions.YourRide;
import exodiasolutions.bikepool.Custom.Store;

public class YourRides extends AppCompatActivity {
    ArrayList<YourRide> arraylist = new ArrayList<>();
    ListView list;
    ProgressDialog dialog,dialog2;
    CustomAdapter3 adapter;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_rides);
        getSupportActionBar().hide();

        dialog = new ProgressDialog(this);
        dialog2 = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);
        list = (ListView) findViewById(R.id.topic_list);
        final MyHttpClient myHttpClient = new MyHttpClient(this,"https://vintagevow-sunnynarang.legacy.cs50.io/bike_pool/yourides.php",new String[]{"username",new Store(YourRides.this).getValue("userid")});
        myHttpClient.execute();
        dialog.setMessage("please wait..");
        dialog.show();
        myHttpClient.callback = new MyCallback() {
            @Override
            public void callbackCall() {

                //Toast.makeText(YourRides.this, ""+myHttpClient.result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                try {
                    JSONArray array = new JSONArray(myHttpClient.result);

                    for(int i =0 ;i< array.length();i++){
                        JSONObject obj = array.getJSONObject(i);

                        arraylist.add(new YourRide(obj.getString("username"),obj.getString("name"),obj.getString("source"),obj.getString("destination"),obj.getString("phone"),obj.getString("bikenumber"),obj.getString("datetime"),obj.getString("user_booked"),obj.getString("book_name"),obj.getString("book_phone"), obj.getString("s_long"), obj.getString("s_lat"), obj.getString("d_long"), obj.getString("d_lat")));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(arraylist.isEmpty()){
                    Toast.makeText(YourRides.this, "You have no rides!", Toast.LENGTH_SHORT).show();
                    YourRides.this.finish();
                }

                adapter=new CustomAdapter3(YourRides.this,arraylist);
                list.setAdapter(adapter);
            }
        };

    }

    class CustomAdapter3 extends ArrayAdapter<YourRide> {
        Context c;

        public CustomAdapter3(Context context, ArrayList<YourRide> arrayList) {
            super(context, R.layout.card2, arrayList);
            this.c = context;
        }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {

            LayoutInflater li = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.card2, parent, false);

            final YourRide ride = getItem(pos);
            ImageView map = convertView.findViewById(R.id.map);
            TextView file_name1 = (TextView) convertView.findViewById(R.id.topic_name1);
            TextView file_name2 = (TextView) convertView.findViewById(R.id.topic_name2);
            TextView file_name3 = (TextView) convertView.findViewById(R.id.topic_name3);
            TextView file_name4 = (TextView) convertView.findViewById(R.id.topic_name4);
            TextView file_name5 = (TextView) convertView.findViewById(R.id.topic_name5);
            TextView file_name6 = (TextView) convertView.findViewById(R.id.topic_name6);
            TextView file_name7 = (TextView) convertView.findViewById(R.id.topic_name7);
            TextView file_name = (TextView) convertView.findViewById(R.id.topic_name);

            if(ride.getId().equalsIgnoreCase(new Store(YourRides.this).getValue("userid")))
            {
            file_name.setText("Your Pool");
            file_name1.setText("Pooled with : " + Html.fromHtml(ride.getId_name()));
            file_name2.setText("Number: " + Html.fromHtml(ride.getBook_phone()));
            file_name3.setText("Source: " + Html.fromHtml(ride.getFrom()));
            file_name4.setText("Destination: " + Html.fromHtml(ride.getTo()));
            file_name5.setText("Date: " + Html.fromHtml(ride.getDatetime().split(" ")[0]));
            file_name6.setText("Time: " + Html.fromHtml(ride.getDatetime().split(" ")[1]));
            file_name7.setText("Bike No. : " + Html.fromHtml(ride.getBikenumber()));
            }
            else{
                file_name.setText("Your Booking");
                file_name1.setText("Rider Name: " + Html.fromHtml(ride.getName()));
                file_name2.setText("Rider Number: " + Html.fromHtml(ride.getPhone()));
                file_name3.setText("Source: " + Html.fromHtml(ride.getFrom()));
                file_name4.setText("Destination: " + Html.fromHtml(ride.getTo()));
                file_name5.setText("Date: " + Html.fromHtml(ride.getDatetime().split(" ")[0]));
                file_name6.setText("Time: " + Html.fromHtml(ride.getDatetime().split(" ")[1]));
                file_name7.setText("Bike No. : " + Html.fromHtml(ride.getBikenumber()));

            }

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(YourRides.this,ShowMaps.class);
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

}

