package co.edu.udea.compumovil.gr6.lab3weather;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.edu.udea.compumovil.gr6.lab3weather.adapter.SectionPageAdapter;
import co.edu.udea.compumovil.gr6.lab3weather.service.WeatherIntent;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    Button button;
    String ciudad;
    Intent intent;
    SectionPageAdapter paginador;
    BroadcastReceiver mibroadCast;
    private WeatherIntent miServicio;
    private boolean mIsBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferencias = getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        ciudad = preferencias.getString("ciudad", "Medellin");
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        paginador = new SectionPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(paginador);
        paginador.getItem(1);
        tabLayout.setupWithViewPager(viewPager);

        IntentFilter filter = new IntentFilter(WeatherIntent.ACTION_CHARGEWEATHER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mibroadCast = new BroadCastSend();
        this.registerReceiver(mibroadCast, filter);


        if (isMyServiceRunning(WeatherIntent.class)) {
            Toast.makeText(this, "Esta corriendo", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Esta corriendo", Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(this, WeatherIntent.class);
            serviceIntent.setAction(WeatherIntent.ACTION_CHARGEWEATHER);
            startService(serviceIntent);

        }
    }

    public class BroadCastSend extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //datos
            String temp = intent.getStringExtra(WeatherIntent.TEMPERATURE);
            String hum = intent.getStringExtra(WeatherIntent.HUMIDITY);
            String ico = intent.getStringExtra(WeatherIntent.ICON);
            String desc = intent.getStringExtra(WeatherIntent.DESCRIPTION);
            Calendar fechaActual = Calendar.getInstance();
            SimpleDateFormat fActual = new SimpleDateFormat("dd-MM-yyyy");
            String fechaActualFormat = fActual.format(fechaActual.getTime());
            Bitmap icon = MainActivity.getBitmapFromAsset(getApplicationContext(), "Images/" + ico + ".png");

            //UPDATE UI
            Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
            TextView tempT = (TextView) findViewById(R.id.temperature_view);
            tempT.setText(Utilities.kelvinToCelsius(temp));
            TextView humT = (TextView) findViewById(R.id.humidity_view);
            humT.setText(hum);
            TextView desT = (TextView) findViewById(R.id.description_view);
            desT.setText(desc);
            TextView fechT = (TextView) findViewById(R.id.date_view);
            fechT.setText(fechaActualFormat);
            ImageView icoI = (ImageView) findViewById(R.id.icon_weather);
            icoI.setImageBitmap(icon);
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            miServicio = ((WeatherIntent.LocalBinder) service).getService();

            // Tell the user about this for our demo.
            Toast.makeText(getApplicationContext(), "OnBind", Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            miServicio = null;
            Toast.makeText(getApplicationContext(), "OnUnBind", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mibroadCast);
        //this.unbindService(mConnection);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

}
