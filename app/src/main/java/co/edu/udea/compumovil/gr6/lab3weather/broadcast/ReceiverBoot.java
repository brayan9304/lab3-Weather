package co.edu.udea.compumovil.gr6.lab3weather.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import co.edu.udea.compumovil.gr6.lab3weather.MainActivity;

/**
 * Created by brayan on 23/09/16.
 */

public class ReceiverBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // LANZAR SERVICIO
        Intent serviceIntent = new Intent(context,WeatherService.class);
        //serviceIntent.setAction("co.edu.udea.compumovil.gr06.serviceboot.MyService");
        serviceIntent.putExtra("Time", 60);
        context.startService(serviceIntent);

        // LANZAR ACTIVIDAD
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
