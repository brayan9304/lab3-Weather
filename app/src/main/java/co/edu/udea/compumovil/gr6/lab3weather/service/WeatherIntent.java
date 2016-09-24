package co.edu.udea.compumovil.gr6.lab3weather.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class WeatherIntent extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_CHARGEWEATHER = "co.edu.udea.compumovil.gr6.lab3weather.service.action.WEATHER";
    // TODO: Rename parameters
    public static final String EXTRA_CITY = "ciudad";

    private static final String TAG = "WeatherIntent";
    private ConnectivityManager estadoConexion;
    String webPage = "";

    public WeatherIntent() {
        super("WeatherIntent");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHARGEWEATHER.equals(action)) {
                final String city = intent.getStringExtra(EXTRA_CITY);
                handleActionChargeWeather(city);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionChargeWeather(String city) {
        try {
            while (true) {
                estadoConexion = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo state = estadoConexion.getActiveNetworkInfo();

                if (state != null && state.isConnected()) {
                    String stringUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + ",uk&appid=e52e67b48e2ddf365e919f03bc8b1984";
                    URL url = null;
                    try

                    {
                        url = new URL(stringUrl);
                    } catch (
                            MalformedURLException e
                            )

                    {
                        e.printStackTrace();
                    }

                    if (url != null)

                    {
                        HttpURLConnection conexion = null;
                        try {
                            conexion = (HttpURLConnection) url.openConnection();
                            conexion.connect();
                            InputStream is = conexion.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                            String data;
                            while ((data = reader.readLine()) != null) {
                                webPage += data + "\n";
                                Log.e(TAG, "run: " + webPage);
                            }
                            conexion.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e(TAG, "handleActionWeather: No hay conexión");
                }
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {

        }
    }

}
