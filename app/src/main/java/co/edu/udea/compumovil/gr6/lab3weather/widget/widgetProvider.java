package co.edu.udea.compumovil.gr6.lab3weather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.edu.udea.compumovil.gr6.lab3weather.R;
import co.edu.udea.compumovil.gr6.lab3weather.service.UpdateWidget;

/**
 * Created by jaime on 24/09/2016.
 */

public class widgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);

        ComponentName thisWidget = new ComponentName(context,widgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(context.getApplicationContext(),
                UpdateWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        SharedPreferences preferencias = context.getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        String ciudad = preferencias.getString("ciudad", "london");
        intent.putExtra("ciudad", ciudad);

        // Update the widgets via the service
        context.startService(intent);
        for (int widgetId : allWidgetIds) {
            Calendar fechaActual = Calendar.getInstance();
            SimpleDateFormat fActual= new SimpleDateFormat("dd-MM-yyyy");
            String fechaActualFormat= fActual.format(fechaActual.getTime());

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            // Set the text

            // Register an onClickListener
            Intent intente = new Intent(context, widgetProvider.class);

            intente.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intente.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.Clima, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }
}
