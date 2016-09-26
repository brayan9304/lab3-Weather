package co.edu.udea.compumovil.gr6.lab3weather.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import co.edu.udea.compumovil.gr6.lab3weather.VolleyCallback;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Main;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Weather;
import co.edu.udea.compumovil.gr6.lab3weather.webService.Volley;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class WeatherIntent extends IntentService {
    public static final String ACTION_CHARGEWEATHER = "co.edu.udea.compumovil.gr6.lab3weather.service.action.WEATHER";
    public static final String TEMPERATURE = "TEMPERATURE.WEATHERLOOP";
    public static final String HUMIDITY = "HUMIDITY.WEATHERLOOP";
    public static final String ICON = "ICON.WEATHERLOOP";
    public static final String DESCRIPTION = "DESCRIPTION.WEATHERLOOP";

    private static final String TAG = "WeatherIntent";
    private LocalBinder binderService;
    private Volley chargeWeather;

    public WeatherIntent() {
        super("WeatherIntent");
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void sendResult(String temperatura, String humedad, String icon, String descripcion) {
        Intent intent = new Intent();
        intent.setAction(WeatherIntent.ACTION_CHARGEWEATHER);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(TEMPERATURE, temperatura);
        intent.putExtra(HUMIDITY, humedad);
        intent.putExtra(ICON, icon);
        intent.putExtra(DESCRIPTION, descripcion);
        sendBroadcast(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHARGEWEATHER.equals(action)) {
                SharedPreferences prefs = getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
                String city = prefs.getString("ciudad", "medellin");
                handleActionChargeWeather(city);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binderService;
    }

    public class LocalBinder extends Binder {
        public WeatherIntent getService() {
            return WeatherIntent.this;
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionChargeWeather(String city) {
        try {
            while (true) {
                Log.e(TAG, "handleActionChargeWeather: ESTOY CORRIENDO");
                chargeWeather = new Volley(city, getApplicationContext());
                chargeWeather.sendRequest(new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Main m = chargeWeather.chargeGSONMain(result);
                        Weather w = chargeWeather.chargeGSONWeather(result);
                        sendResult(m.getTemp(), m.getHumidity(), w.getIcon(), w.getDescription());
                    }
                });

                Thread.sleep(60000);
            }
        } catch (InterruptedException e) {

        }
    }

}
