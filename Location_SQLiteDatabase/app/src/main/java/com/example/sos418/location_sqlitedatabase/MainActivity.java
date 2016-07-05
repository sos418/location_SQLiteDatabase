package com.example.sos418.location_sqlitedatabase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private SQLiteDatabase db = null;

    private final static String CREATE_TABLE = "CREATE TABLE table01 (_id INTEGER PRIMARY KEY,lat DOUBLE,lon DOUBLE)";


    Button btnDo, btnFinish;
    EditText editText;
    String str,strtable ,output;
    TextView textView;
    LocationManager locationManager;

    double latitude = 0, longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDo = (Button) findViewById(R.id.btnDO);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        editText = (EditText) findViewById(R.id.edtSQL);
        textView = (TextView) findViewById(R.id.textview);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btnDo.setOnClickListener(btnDoClick);
        btnFinish.setOnClickListener(btnFinishClick);

        str = "經緯度：";
        editText.setText(str);

        db = openOrCreateDatabase("db1.db", MODE_PRIVATE, null);
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
        }
    }


    private Button.OnClickListener btnDoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {

                String s = LocationManager.GPS_PROVIDER;
                Location location = locationManager.getLastKnownLocation(s);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(s, 0, 0, locationListener);

                latitude = location.getLatitude();
                longitude = location.getLatitude();
                strtable = "INSERT INTO table01 (lat,lon) values ("+ latitude +","+ longitude +")";
                db.execSQL(strtable);
                UpdataAdapter();
                str = "經緯度："+ latitude + "," + longitude;
                editText.setText(str);
                textView.setText(output);
            }catch (Exception err){
                setTitle("SQL語法錯誤");
            }
        }
    };

    private Button.OnClickListener  btnFinishClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db.execSQL("DROP TABLE table01");
            db.close();
            finish();
        }
    };


    public void UpdataAdapter(){
        Cursor cursor = db.rawQuery("SELECT * FROM table01",null);
        int rows_num = cursor.getCount();

        if (rows_num != 0){
            cursor.moveToFirst();
            output ="";
            for (int i = 0;i<rows_num;i++){
                output += cursor.getInt(0) + "\n";
                output += cursor.getDouble(1) + "\n";
                output += cursor.getDouble(2)+ "\n";

                cursor.moveToNext();
            }
        }
        cursor.close();

    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
