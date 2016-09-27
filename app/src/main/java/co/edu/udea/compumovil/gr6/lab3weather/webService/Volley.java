package co.edu.udea.compumovil.gr6.lab3weather.webService;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.edu.udea.compumovil.gr6.lab3weather.SingletonRequestQueue;
import co.edu.udea.compumovil.gr6.lab3weather.Utilities;
import co.edu.udea.compumovil.gr6.lab3weather.VolleyCallback;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Main;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Weather;


//http://api.openweathermap.org/data/2.5/weather?q=tokyo&appid=2aba3adc8f9a3eed10e9d43a47edd216
public class Volley {
    private final String TAG = "Volley";

    private static final String API_KEY = "2aba3adc8f9a3eed10e9d43a47edd216";
    private static final String REQUEST = "/data/2.5/weather";
    private static final String BASE_URL = "http://api.openweathermap.org";

    private String paramsID, paramsName;
    private String URL;
    private String nameCity;
    private Context context;
    private RequestQueue queue;


    public Volley(String nameCity, Context context) {
        this.context = context;
        this.nameCity = nameCity;
        queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
       /* if (nameCity.equalsIgnoreCase("Ibague")) {
            nameCity = "Ibage";
        }*/
        if (nameCity.equalsIgnoreCase("Santa Marta")) {
            nameCity = "Santa%20Marta";
        }
        this.paramsID = "?id=" + Utilities.getIDCity(nameCity) + "&appid=" + API_KEY;
        this.paramsName = "?q=" + nameCity + "&appid=" + API_KEY;

    }

    public void sendRequestID(final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        URL = BASE_URL + REQUEST + paramsID;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.toString());
                            String objectStr = object.toString();
                            if (!objectStr.contains("Error: Not found city")) {
                                callback.onSuccess(new JSONObject(response.toString()));
                            } else {
                                callback.onError();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
                );
        queue.add(jsObjRequest);
    }

    public void sendRequestName(final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        URL = BASE_URL + REQUEST + paramsName;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.toString());
                            String objectStr = object.toString();
                            if (!objectStr.contains("Error: Not found city")) {
                                callback.onSuccess(new JSONObject(response.toString()));
                            } else {
                                callback.onError();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
                );
        queue.add(jsObjRequest);
    }

    public Main chargeGSONMain(JSONObject responses) {
        Main mainLo = null;
        if (responses != null) {
            JSONObject object = responses;
            JSONObject json_main = object.optJSONObject("main");
            Gson outGson = new Gson();
            mainLo = outGson.fromJson(json_main.toString(), Main.class);
        }
        return mainLo;
    }

    public Weather chargeGSONWeather(JSONObject responses) {
        Weather weatherLo = null;
        if (responses != null) {
            JSONObject object = responses;
            JSONArray json_weather = object.optJSONArray("weather");
            Gson outGson = new Gson();
            try {
                weatherLo = outGson.fromJson(json_weather.getJSONObject(0).toString(), Weather.class);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return weatherLo;
    }
}
