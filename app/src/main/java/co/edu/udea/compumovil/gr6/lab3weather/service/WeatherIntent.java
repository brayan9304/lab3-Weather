package co.edu.udea.compumovil.gr6.lab3weather.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import co.edu.udea.compumovil.gr6.lab3weather.R;
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
    public static final String STOP_SERVICE = "co.edu.udea.compumovil.gr6.lab3weather.service.action.STOP_SERVICE";
    public static final String TEMPERATURE = "TEMPERATURE.WEATHERLOOP";
    public static final String HUMIDITY = "HUMIDITY.WEATHERLOOP";
    public static final String ICON = "ICON.WEATHERLOOP";
    public static final String DESCRIPTION = "DESCRIPTION.WEATHERLOOP";
    public static final String MESSAGE = "MESSAGE.WEATHELOOP";

    private static final String TAG = "WeatherIntent";
    private Volley chargeWeather;
    private boolean running;
    private int timeRefresh;
    private final IBinder mBinder = new LocalBinder();

    public WeatherIntent() {
        super("WeatherIntent");
        running = true;
        timeRefresh = 60;

        setIntentRedelivery(true);
    }

    public int getTimeRefresh() {
        return timeRefresh;
    }

    public void setTimeRefresh(int timeRefresh) {
        this.timeRefresh = timeRefresh;
    }

    @Override

    public void onCreate() {
        Log.e(TAG, "onCreate:");
        super.onCreate();
    }

    public void sendResult(String temperatura, String humedad, String icon, String descripcion, String mensaje) {
        Intent intent = new Intent();
        intent.setAction(WeatherIntent.ACTION_CHARGEWEATHER);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(TEMPERATURE, temperatura);
        intent.putExtra(HUMIDITY, humedad);
        intent.putExtra(ICON, icon);
        intent.putExtra(DESCRIPTION, descripcion);
        intent.putExtra(MESSAGE, mensaje);
        sendBroadcast(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHARGEWEATHER.equals(action)) {
                SharedPreferences prefs = getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
                String city = prefs.getString("ciudad", "medellin");
                timeRefresh = prefs.getInt("timeRefresh", 60);
                handleActionChargeWeather(city);
            } else if (STOP_SERVICE.equals(action)) {
                Log.e(TAG, "en StopSelft: ");
                //setIntentRedelivery(false);
                stopSelf();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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
    public void handleActionChargeWeather(String city) {
        try {
            while (running) {
                Log.e(TAG, "handleActionChargeWeather: ESTOY CORRIENDO " + city);
                chargeWeather = new Volley(city, getApplicationContext());
                chargeWeather.sendRequestName(new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Main m = chargeWeather.chargeGSONMain(result);
                        Weather w = chargeWeather.chargeGSONWeather(result);
                        sendResult(m.getTemp(), m.getHumidity(), w.getIcon(), w.getDescription(), "");
                        Toast.makeText(getApplicationContext(), "entro en 1", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        chargeWeather.sendRequestID(new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Main m = chargeWeather.chargeGSONMain(result);
                                Weather w = chargeWeather.chargeGSONWeather(result);
                                sendResult(m.getTemp(), m.getHumidity(), w.getIcon(), w.getDescription(), "");
                                Toast.makeText(getApplicationContext(), "entro en 2", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getApplicationContext(), "entro en 3", Toast.LENGTH_SHORT).show();
                                sendResult("", "", "", "", getResources().getString(R.string.errorNotFound));
                            }
                        });
                    }
                });
                Thread.sleep(timeRefresh * 1000);
            }
        } catch (InterruptedException e) {

        }
    }

    public void stopRunning() {
        running = false;
        Log.e(TAG, "stopRunning: STOP!!!!");
    }

    public void startRunning() {
        running = true;
        Log.e(TAG, "stopRunning: START!!!!");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
