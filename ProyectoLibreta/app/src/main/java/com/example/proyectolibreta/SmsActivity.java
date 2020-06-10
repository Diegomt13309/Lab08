package com.example.proyectolibreta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User aux = (User) getIntent().getSerializableExtra("user");
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
                chip.setTextSize(30);
                cp1.addView(chip);
            }
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 1000);

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        /*SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage(txtMobile.getText().toString(), null, txtMessage.getText().toString(), null, null);
                        Toast.makeText(SmsActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();*/
                        sendMessage(v);
                    } catch (Exception e) {
                        Toast.makeText(SmsActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
        chip.setTextSize(18);
        cp2.addView(chip);
        //cp2.addView(ko);
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
        Toast toast = Toast.makeText(getApplicationContext(), "Location", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void camera() {
        Toast toast = Toast.makeText(getApplicationContext(), "Camera", Toast.LENGTH_SHORT);
        toast.show();
    }

}