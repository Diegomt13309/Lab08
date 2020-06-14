package com.example.proyectolibreta;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import Model.SingletonFormList;
import Model.User;

public class addUser extends AppCompatActivity {
    private EditText name;
    private EditText title;
    private EditText phone;
    private ImageView imagen;
    private boolean editable = true;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.editTextTextPersonName);
        title = findViewById(R.id.editTextTextPersonName2);
        phone = findViewById(R.id.editTextPhone);
        imagen = findViewById(R.id.imageViewadd);

        editable = true;

        final Bundle extras = getIntent().getExtras();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < sensor.getMaximumRange()) {
                    editable = extras.getBoolean("editable");
                    if (editable) {
                        editUser();
                    } else {
                        addUser();
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        start();


        if (extras != null) {
            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                User aux = (User) getIntent().getSerializableExtra("user");
                name.setText(aux.getName());
                title.setText(aux.getLastName());
                phone.setText(aux.getNumber());
            }
        }
    }

    public static int getRandom(int from, int to) {
        if (from < to)
            return from + new Random().nextInt(Math.abs(to - from));
        return from - new Random().nextInt(Math.abs(to - from));
    }


    private void addUser() {
        if (validarForm()) {
            new AlertDialog.Builder(addUser.this)
                    .setTitle("Nuevo Contacto")
                    .setMessage("Confirmar nuevo contacto?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int id = getRandom(1000,9999);
                            User aux = new User(name.getText().toString(), title.getText().toString(), phone.getText().toString(),id);
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            SingletonFormList.getInstance().addToArray(aux);
                            intent.putExtra("addUser", aux);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void editUser() {
        if (validarForm()) {
            new AlertDialog.Builder(addUser.this)
                    .setTitle("Editar Contacto")
                    .setMessage("Confirmar la edicion de contacto?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            User aux1 = (User) getIntent().getSerializableExtra("user");
                            User aux = new User(name.getText().toString(), title.getText().toString(), phone.getText().toString(),aux1.getId());
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.putExtra("editUser", aux);
                            startActivity(intent);
                            finish(); //prevent go back
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    private boolean validarForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.name.getText())) {
            name.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.title.getText())) {
            title.setError("Titulo o Apellido Requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.phone.getText())) {
            phone.setError("Telefono Requerido");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Campos sin completar", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() { //TODO it's not working yet
        Intent a = new Intent(this, MainActivity.class);
        startActivity(a);
        super.onBackPressed();
    }

    public void start() {
        sensorManager.registerListener(sensorEventListener, sensor, 2000 * 1000);
    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause() {
        stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        start();
        super.onResume();
    }


}