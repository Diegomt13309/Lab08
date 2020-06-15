package com.example.proyectolibreta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Model.Datos;
import Model.SingletonFormList;
import Model.User;

public class MainActivity extends AppCompatActivity implements MainAdapter.JobFormAdapterListener,MainHelper.RecyclerItemTouchHelperListener{
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private Datos datos;
    private List<User> usuarios;
    private static int ft=1;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private Object User;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNew();
            }
        });

        View inflatedView = getLayoutInflater().inflate(R.layout.item_list_row, null);
        final TextView text = (TextView) inflatedView.findViewById(R.id.descriptionLbl);


        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

                int[] androidColors1 = getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor1 = androidColors1[new Random().nextInt(androidColors.length)];
                if(event.values[0]<sensor.getMaximumRange()){
                    coordinatorLayout.setBackgroundColor(randomAndroidColor);
                    fab.setBackgroundTintList(ColorStateList.valueOf(randomAndroidColor1));
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };


        mRecyclerView = findViewById(R.id.recyclerView);


        start();

        usuarios = new ArrayList<>();
        datos = new Datos();
        if (ft==1){
            for (int i = 0; i < datos.getUsuarios().size(); i++) {
                SingletonFormList.getInstance().addToArray(datos.getUsuarios().get(i));
            }
            ft++;
        }
        usuarios.addAll(SingletonFormList.getInstance().getArray());

        mAdapter = new MainAdapter(usuarios, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new MainHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        checkIntentInformation();
        mAdapter.notifyDataSetChanged();



    }

    private void checkIntentInformation() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User aux;
            aux = (User) getIntent().getSerializableExtra("addUser");
            if (aux == null) {
                aux = (User) getIntent().getSerializableExtra("editUser");
                if (aux != null) {
                    boolean founded = false;
                    for (User users : usuarios) {
                        if (users.getId()==aux.getId()) {
                            users.setName(aux.getName());
                            users.setLastName(aux.getLastName());
                            users.setNumber(aux.getNumber());
                            founded = true;
                            break;
                        }
                    }
                    if (founded) {
                        Toast.makeText(getApplicationContext(), aux.getName() + " editado correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), aux.getName() + " no encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                usuarios=SingletonFormList.getInstance().getArray();
                Toast.makeText(getApplicationContext(), aux.getName() + " agregado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addNew() {
        Intent intent = new Intent(this, addUser.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }




    public void start(){
        sensorManager.registerListener(sensorEventListener,sensor,2000*1000);
    }
    public void stop(){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
            //return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContactSelected(User user) {
        String h = user.getName();
        Intent intent = new Intent(this, addUser.class);
        intent.putExtra("editable", true);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof MainAdapter.MyViewHolder) {
                //SMS
                // get the removed item name to display it in snack bar
                User aux = usuarios.get(viewHolder.getAdapterPosition());
                String val = aux.getNumber();
                String h = val.substring(0,1);
                if(!h.equals("2")) {
                    Intent intent = new Intent(this, SmsActivity.class);
                    intent.putExtra("user", aux);
                    mAdapter.notifyDataSetChanged(); //restart left swipe view
                    startActivity(intent);
                }else{
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "No se puede enviar mensajes a numeros fijos", Toast.LENGTH_LONG).show();
                }
                mAdapter.notifyDataSetChanged();
            }
        } else {
            User aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            callPhoneNumber(aux);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber((Model.User) User);
            }
        }
    }


    public void callPhoneNumber(User aux) {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "+506" + aux.getNumber()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "+506" + aux.getNumber()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemMove(int source, int target) {
        mAdapter.onItemMove(source, target);
    }
}