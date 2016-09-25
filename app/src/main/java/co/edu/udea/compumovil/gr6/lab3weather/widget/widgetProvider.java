package co.edu.udea.compumovil.gr6.lab3weather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.edu.udea.compumovil.gr6.lab3weather.MainActivity;
import co.edu.udea.compumovil.gr6.lab3weather.R;

/**
 * Created by jaime on 24/09/2016.
 */

public class widgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);

        ComponentName thisWidget = new ComponentName(context,widgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            // create some random data
            Calendar fechaActual = Calendar.getInstance();
            SimpleDateFormat fActual= new SimpleDateFormat("dd-MM-yyyy");
            String fechaActualFormat= fActual.format(fechaActual.getTime());

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            // Set the text
            remoteViews.setTextViewText(R.id.Clima, "El Clima en "+"Medellin");
            remoteViews.setTextViewText(R.id.fecha,fechaActualFormat);
            remoteViews.setTextViewText(R.id.temperatura,"25 °C");
            Bitmap icon = MainActivity.getBitmapFromAsset(context, "Images/01d.png");
            remoteViews.setImageViewBitmap(R.id.icon, icon);
            remoteViews.setTextViewText(R.id.humedad,"88%");

            // Register an onClickListener
            Intent intent = new Intent(context, widgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.Clima, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }
}
