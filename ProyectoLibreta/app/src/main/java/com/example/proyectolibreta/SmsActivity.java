package com.example.proyectolibreta;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import Model.User;

public class SmsActivity extends AppCompatActivity {

    private EditText txtMobile;
    private EditText txtMessage;
    private Button btnSms;
    private ChipGroup cp1;
    private FlexboxLayout cp2;
    private EditText et;
    private ImageButton ib;
    private ImageView ko;
    private int permissionCheck;
    private User mU;
    private static final int Image_Capture_Code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        //txtMobile = (EditText) findViewById(R.id.mblTxt);
        //txtMessage = (EditText) findViewById(R.id.msgTxt);
        cp1 = findViewById(R.id.chipG1);
        cp2 = findViewById(R.id.chipG2);
        et = findViewById(R.id.editTextTextMultiLine);
        ib = findViewById(R.id.imageButton);
        ko=findViewById(R.id.imageViewk);


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
        final Chip chip = new Chip(this);
        ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
        chip.setChipDrawable(drawable);
        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setElevation(20);
        chip.setPadding(80, 40, 80, 40);
        chip.setText(et.getText().toString());
        et.setText("");
        chip.setTextSize(15);
        cp2.addView(chip);
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
                et.setText(location.getLatitude()+" "+location.getLongitude());
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,30000,50,locationListener);
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
                ko.setImageBitmap(bp);
                final Chip chip = new Chip(this);
                ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                chip.setChipDrawable(drawable);
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setElevation(20);
                chip.setPadding(80, 40, 80, 40);
                chip.setText("Imagen Enviada");
                chip.setTextSize(15);
                cp2.addView(chip);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

}