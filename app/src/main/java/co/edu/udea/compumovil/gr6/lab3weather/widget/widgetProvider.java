package co.edu.udea.compumovil.gr6.lab3weather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import co.edu.udea.compumovil.gr6.lab3weather.service.UpdateWidget;

/**
 * Created by jaime on 24/09/2016.
 */

public class widgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context,widgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        Intent intent = new Intent(context, UpdateWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        intent.putExtra(UpdateWidget.FLAG_RETURNED, UpdateWidget.FLAG_WIDGET);
        SharedPreferences preferencias = context.getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        String ciudad = preferencias.getString("ciudad", "london");
        intent.putExtra("ciudad", ciudad);
        // Update the widgets via the service
        context.startService(intent);
    }
}
