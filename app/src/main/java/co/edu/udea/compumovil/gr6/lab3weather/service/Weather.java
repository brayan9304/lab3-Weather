package co.edu.udea.compumovil.gr6.lab3weather.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Weather extends Service {

    private static final String TAG = "Weather";

    private ConnectivityManager estadoConexion;

    public Weather() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            while (true) {
                cargarClima clima = new cargarClima();
                clima.execute(intent);
                try {
                    String result = clima.get();
                    Log.e(TAG, "onStartCommand: " + result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return START_REDELIVER_INTENT;
    }

    private class cargarClima extends AsyncTask<Intent, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Intent... intents) {
            estadoConexion = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo state = estadoConexion.getActiveNetworkInfo();
            Intent[] local = intents.clone();
            String webPage = "";
            if (state != null && state.isConnected()) {
                String stringUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + local[0].getStringExtra("ciudad") + ",uk&appid=e52e67b48e2ddf365e919f03bc8b1984";
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
            return webPage;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
