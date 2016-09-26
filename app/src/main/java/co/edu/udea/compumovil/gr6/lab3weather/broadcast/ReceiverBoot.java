package co.edu.udea.compumovil.gr6.lab3weather.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import co.edu.udea.compumovil.gr6.lab3weather.service.WeatherIntent;

/**
 * Created by brayan on 23/09/16.
 */

public class ReceiverBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // LANZAR SERVICIO
        SharedPreferences preferencias = context.getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        String ciudad = preferencias.getString("ciudad", "Medellin");
        Intent serviceIntent = new Intent(context, WeatherIntent.class);
        serviceIntent.setAction("co.edu.udea.compumovil.gr6.lab3weather.service.action.WEATHER");
        context.startService(serviceIntent);

    }
}
