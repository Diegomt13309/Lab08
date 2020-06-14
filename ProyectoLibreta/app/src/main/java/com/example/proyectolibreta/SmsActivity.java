package com.example.proyectolibreta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import Model.User;

public class SmsActivity extends AppCompatActivity {

    private ScrollView scrollViewP;
    private ScrollView scrollViewC;
    private ChipGroup cp1;
    private FlexboxLayout cp2;
    private EditText et;
    private ImageButton ib;
    private int permissionCheck;
    private User mU;
    private static final int Image_Capture_Code = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        cp1 = findViewById(R.id.chipG1);
        cp2 = findViewById(R.id.chipG2);
        et = findViewById(R.id.editTextTextMultiLine);
        ib = findViewById(R.id.imageButton);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User aux = (User) getIntent().getSerializableExtra("user");
            mU = (User) getIntent().getSerializableExtra("user");
            assert aux != null;
            for(int i=0; i<2; i++) {
                final Chip chip = new Chip(this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                chip.setChipDrawable(drawable);
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setPadding(60, 10, 60, 10);
                if(i==0){
                    chip.setChipIconResource(R.drawable.ic_user);
                    chip.setText(aux.getName());
                }else {
                    chip.setChipIconResource(R.drawable.ic_phone);
                    chip.setText(aux.getNumber());
                }
                chip.setIconStartPadding(3f);
                chip.setTextSize(25);
                cp1.addView(chip);
            }
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                       SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage(mU.getNumber().toString(), null, et.getText().toString(), null, null);
                        Toast.makeText(SmsActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                        sendMessage(v);
                    } catch (Exception e) {
                        Toast.makeText(SmsActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    }

    @Override
    public void onBackPressed() { //TODO it's not working yet
        Intent a = new Intent(this, MainActivity.class);
        startActivity(a);
        super.onBackPressed();
    }

    public void sendMessage(View v){

        TextView textoMsg = new TextView(SmsActivity.this);
        textoMsg.setText(et.getText().toString());
        textoMsg.setTextSize(12);
        textoMsg.setGravity(Gravity.CENTER);
        textoMsg.setTextColor(getResources().getColor(android.R.color.black));
        textoMsg.setPadding(80,60,80,60);

        textoMsg.setBackground(getResources().getDrawable(R.drawable.ic_text_message));

        et.setText("");
        cp2.addView(textoMsg, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_camera:
                camera();
                return true;
            case R.id.action_location:
                location();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void location() {
        LocationManager locationManager = (LocationManager) SmsActivity.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(SmsActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    et.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,50,locationListener);
    }

    private void camera() {
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt,Image_Capture_Code);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                ImageView imageView = new ImageView(SmsActivity.this);
                imageView.setImageBitmap(bp);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(450, 450);
                imageView.setLayoutParams(layoutParams);
                imageView.setPadding(10,10,10,10);
                imageView.setBackground(getResources().getDrawable(R.drawable.ic_frame));
                et.setText("");
                cp2.addView(imageView,0);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
            }
        }
    }

}