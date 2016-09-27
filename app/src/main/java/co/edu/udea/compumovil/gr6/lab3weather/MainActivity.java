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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import co.edu.udea.compumovil.gr6.lab3weather.adapter.SectionPageAdapter;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Main;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Weather;
import co.edu.udea.compumovil.gr6.lab3weather.service.WeatherIntent;
import co.edu.udea.compumovil.gr6.lab3weather.webService.Volley;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String TEMPERATURA_CAMP = "temperatura";
    private static final String HUMEDAD_CAMP = "humedad";
    private static final String ICONO_CAMP = "icono";
    private static final String DESCRIPCION_CAMP = "descripcion";
    private static final String FECHAACTUAL_CAMP = "fechaActual";

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    private String ciudad;
    private String temp, hum, ico, desc, fechaActualFormat;
    SectionPageAdapter paginador;
    BroadcastReceiver mibroadCast;
    private WeatherIntent mService;
    private boolean mBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferencias = getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        ciudad = preferencias.getString("ciudad", "Medellin");
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        paginador = new SectionPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(paginador);
        tabLayout.setupWithViewPager(viewPager);
        IntentFilter filter = new IntentFilter(WeatherIntent.ACTION_CHARGEWEATHER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mibroadCast = new BroadCastSend();
        this.registerReceiver(mibroadCast, filter);

        if (savedInstanceState == null) {
            if (isMyServiceRunning(WeatherIntent.class)) {
                //Toast.makeText(this, "Esta corriendo", Toast.LENGTH_LONG).show();

                /*Intent intent = new Intent(this, UpdateWidget.class);
                intent.putExtra(UpdateWidget.FLAG_RETURNED, UpdateWidget.FLAG_ACTIVITY);
                startService(intent);*/
                final Volley chargeWeather = new Volley(ciudad, getApplicationContext());
                chargeWeather.sendRequestName(new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        //datos
                        Main m = chargeWeather.chargeGSONMain(result);
                        Weather w = chargeWeather.chargeGSONWeather(result);
                        Calendar fechaActual = Calendar.getInstance();
                        SimpleDateFormat fActual = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
                        fechaActualFormat = fActual.format(fechaActual.getTime());
                        Bitmap icon = MainActivity.getBitmapFromAsset(getApplicationContext(), "Images/" + w.getIcon() + ".png");

                        //UPDATE UI
                        TextView tempT = (TextView) findViewById(R.id.temperature_view);
                        tempT.setText(Utilities.kelvinToCelsius(m.getTemp()));
                        TextView humT = (TextView) findViewById(R.id.humidity_view);
                        humT.setText(m.getHumidity());
                        TextView desT = (TextView) findViewById(R.id.description_view);
                        desT.setText(w.getDescription());
                        TextView fechT = (TextView) findViewById(R.id.date_view);
                        fechT.setText(fechaActualFormat);
                        ImageView icoI = (ImageView) findViewById(R.id.icon_weather);
                        icoI.setImageBitmap(icon);
                    }

                    @Override
                    public void onError() {
                        chargeWeather.sendRequestID(new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                //datos
                                Main m = chargeWeather.chargeGSONMain(result);
                                Weather w = chargeWeather.chargeGSONWeather(result);
                                Calendar fechaActual = Calendar.getInstance();
                                SimpleDateFormat fActual = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
                                fechaActualFormat = fActual.format(fechaActual.getTime());
                                Bitmap icon = MainActivity.getBitmapFromAsset(getApplicationContext(), "Images/" + w.getIcon() + ".png");

                                //UPDATE UI
                                TextView tempT = (TextView) findViewById(R.id.temperature_view);
                                tempT.setText(Utilities.kelvinToCelsius(m.getTemp()));
                                TextView humT = (TextView) findViewById(R.id.humidity_view);
                                humT.setText(m.getHumidity());
                                TextView desT = (TextView) findViewById(R.id.description_view);
                                desT.setText(w.getDescription());
                                TextView fechT = (TextView) findViewById(R.id.date_view);
                                fechT.setText(fechaActualFormat);
                                ImageView icoI = (ImageView) findViewById(R.id.icon_weather);
                                icoI.setImageBitmap(icon);
                            }

                            @Override
                            public void onError() {
                                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.errorNotFound), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } else {
                //Toast.makeText(this, "No Esta corriendo", Toast.LENGTH_LONG).show();
                Intent serviceIntent = new Intent(MainActivity.this, WeatherIntent.class);
                serviceIntent.setAction(WeatherIntent.ACTION_CHARGEWEATHER);
                startService(serviceIntent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, WeatherIntent.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void actualizarCiudad(String ciudadAuto, int tiempo) {
        SharedPreferences prefs = getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ciudad", ciudadAuto.toString());
        editor.putInt("timeRefresh", tiempo);
        editor.commit();
        ciudad = prefs.getString("ciudad", "medellin");
        TextView cityWeather = (TextView) findViewById(R.id.Clima_Ciudad);
        cityWeather.setText(getResources().getString(R.string.weather_In) + " " + ciudad);
        TextView actual = (TextView) findViewById(R.id.ciudad_actual);
        actual.setText(getResources().getString(R.string.ciudadActual) + " " + ciudad);
        if (isMyServiceRunning(WeatherIntent.class)) {
            //Toast.makeText(this, "IS RUNNING", Toast.LENGTH_SHORT).show();
            if (mBound) {
                Log.e(TAG, "actualizarCiudad: " + mBound);
                mService.stopRunning();
                Log.e(TAG, "actualizarCiudad: " + mService.getTimeRefresh());
                mService.setIntentRedelivery(false);
                mService.stopSelf();
                mService.onDestroy();
                unbindService(mConnection);
                mBound = false;
            }
        } /*else {
            Toast .makeText(this, "IS NOT RUNNING", Toast.LENGTH_SHORT).show();
        }*/

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                AutoCompleteTextView ciudadAuto = (AutoCompleteTextView) findViewById(R.id.ciudad_select);
                NumberPicker number = (NumberPicker) findViewById(R.id.spinner);
                ciudadAuto.performValidation();
                if (!ciudadAuto.getText().toString().equals("")) {
                    Toast.makeText(this, ciudadAuto.getText().toString(), Toast.LENGTH_SHORT).show();
                    String newCity = ciudadAuto.getText().toString();
                    if (!newCity.equalsIgnoreCase(ciudad)) {
                        ciudadAuto.setText("");
                        actualizarCiudad(newCity, number.getValue());
                        Intent serviceIntent = new Intent(MainActivity.this, WeatherIntent.class);
                        serviceIntent.setAction(WeatherIntent.ACTION_CHARGEWEATHER);
                        startService(serviceIntent);
                        IntentFilter filter = new IntentFilter(WeatherIntent.ACTION_CHARGEWEATHER);
                        filter.addCategory(Intent.CATEGORY_DEFAULT);
                        mibroadCast = new BroadCastSend();
                        this.registerReceiver(mibroadCast, filter);
                        Intent intent = new Intent(this, WeatherIntent.class);
                        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.newCity) + " " + ciudad, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.errorSameCity), Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.errorCampRequired), Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //datos
        temp = savedInstanceState.getString(TEMPERATURA_CAMP);
        hum = savedInstanceState.getString(HUMEDAD_CAMP);
        ico = savedInstanceState.getString(ICONO_CAMP);
        desc = savedInstanceState.getString(DESCRIPCION_CAMP);
        fechaActualFormat = savedInstanceState.getString(FECHAACTUAL_CAMP);
        Bitmap icon = MainActivity.getBitmapFromAsset(getApplicationContext(), "Images/" + ico + ".png");

        //UPDATE UI
        //Toast.makeText(this, "Recreado", Toast.LENGTH_SHORT).show();
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
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TEMPERATURA_CAMP, temp);
        outState.putString(HUMEDAD_CAMP, hum);
        outState.putString(ICONO_CAMP, ico);
        outState.putString(DESCRIPCION_CAMP, desc);
        outState.putString(FECHAACTUAL_CAMP, fechaActualFormat);
        super.onSaveInstanceState(outState);
    }

    public class BroadCastSend extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //datos
            temp = intent.getStringExtra(WeatherIntent.TEMPERATURE);
            hum = intent.getStringExtra(WeatherIntent.HUMIDITY);
            ico = intent.getStringExtra(WeatherIntent.ICON);
            desc = intent.getStringExtra(WeatherIntent.DESCRIPTION);
            String message = intent.getStringExtra(WeatherIntent.MESSAGE);
            if (message.equals("")) {
                Calendar fechaActual = Calendar.getInstance();
                SimpleDateFormat fActual = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
                fechaActualFormat = fActual.format(fechaActual.getTime());
                Bitmap icon = MainActivity.getBitmapFromAsset(getApplicationContext(), "Images/" + ico + ".png");

                //UPDATE UI
                //Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
                TextView tempT = (TextView) findViewById(R.id.temperature_view);
                tempT.setText(Utilities.kelvinToCelsius(temp) + "°C");
                TextView humT = (TextView) findViewById(R.id.humidity_view);
                humT.setText(hum + "%");
                TextView desT = (TextView) findViewById(R.id.description_view);
                desT.setText(desc);
                TextView fechT = (TextView) findViewById(R.id.date_view);
                fechT.setText(fechaActualFormat);
                ImageView icoI = (ImageView) findViewById(R.id.icon_weather);
                icoI.setImageBitmap(icon);
            } else {
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        this.unregisterReceiver(mibroadCast);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
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


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            WeatherIntent.LocalBinder binder = (WeatherIntent.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
