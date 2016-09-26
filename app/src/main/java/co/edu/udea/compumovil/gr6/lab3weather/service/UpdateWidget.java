package co.edu.udea.compumovil.gr6.lab3weather.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.widget.RemoteViews;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.edu.udea.compumovil.gr6.lab3weather.MainActivity;
import co.edu.udea.compumovil.gr6.lab3weather.R;
import co.edu.udea.compumovil.gr6.lab3weather.Utilities;
import co.edu.udea.compumovil.gr6.lab3weather.VolleyCallback;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Main;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Weather;
import co.edu.udea.compumovil.gr6.lab3weather.webService.Volley;
import co.edu.udea.compumovil.gr6.lab3weather.widget.widgetProvider;

public class UpdateWidget extends Service {
    public UpdateWidget() {
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        SharedPreferences preferencias = getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        final String city = preferencias.getString("ciudad", "london");
        final Volley chargeWeather = new Volley(city, getApplicationContext());
        final AppWidgetManager widgetMan = AppWidgetManager.getInstance(this.getApplicationContext());
        final int[] allWidgetIds = intent.getIntArrayExtra(widgetMan.EXTRA_APPWIDGET_IDS);
        chargeWeather.sendRequest(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Main m = chargeWeather.chargeGSONMain(result);
                Weather w = chargeWeather.chargeGSONWeather(result);

                for (int widgetId : allWidgetIds) {
                    RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_layout);
                    remoteViews.setTextViewText(R.id.Clima, getApplication().getString(R.string.weather_In) + city);
                    Calendar fechaActual = Calendar.getInstance();
                    SimpleDateFormat fActual = new SimpleDateFormat("dd-MM-yyyy");
                    String fechaActualFormat = fActual.format(fechaActual.getTime());
                    remoteViews.setTextViewText(R.id.fecha, fechaActualFormat);
                    remoteViews.setTextViewText(R.id.temperatura, Utilities.kelvinToCelsius(m.getTemp()) + " °C");
                    remoteViews.setTextViewText(R.id.descripcion, w.getDescription());
                    Bitmap icon = MainActivity.getBitmapFromAsset(getApplicationContext(), "Images/" + w.getIcon() + ".png");
                    remoteViews.setImageViewBitmap(R.id.icon, icon);
                    remoteViews.setTextViewText(R.id.humedad, m.getHumidity() + "%");

                    Intent clickIntent = new Intent(getApplicationContext(),
                            widgetProvider.class);
                    clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.Clima, pendingIntent);
                    widgetMan.updateAppWidget(widgetId, remoteViews);
                }
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
