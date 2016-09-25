package co.edu.udea.compumovil.gr6.lab3weather.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

import co.edu.udea.compumovil.gr6.lab3weather.webService.Volley;


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
    //private ConnectivityManager estadoConexion;
    String webPage = "";
    Volley chargeWeather;

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
                AppWidgetManager widgetMan = AppWidgetManager.getInstance(this.getApplicationContext());
                int[] allWidgetIds = intent.getIntArrayExtra(widgetMan.EXTRA_APPWIDGET_IDS);

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
                Log.e(TAG, "handleActionChargeWeather: ERRORRORORORORORORO ORODOSDO E");
                chargeWeather = new Volley(city, getApplicationContext());
                chargeWeather.sendRequest();

                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {

        }
    }

}
