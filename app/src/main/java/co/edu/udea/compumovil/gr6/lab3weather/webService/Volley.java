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
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Main;
import co.edu.udea.compumovil.gr6.lab3weather.pojo.Weather;


//http://api.openweathermap.org/data/2.5/weather?q=tokyo&appid=2aba3adc8f9a3eed10e9d43a47edd216
public class Volley  {
    private final String TAG = "Volley";

    private static final String API_KEY = "2aba3adc8f9a3eed10e9d43a47edd216";
    private static String CIUDAD_NAME = "medellin";
    private static final String REQUEST = "/data/2.5/weather";
    private static final String BASE_URL = "http://api.openweathermap.org";

    private String params;
    private String URL;
    private String nameCity;
    private RequestQueue requestQueue;
    private Main main;
    private Weather weather;
    private Context context;


    public Volley(String nameCity, Context context) {
        this.context = context;
        this.nameCity = nameCity;
        this.params = "?q=" + nameCity + "&appid=" + API_KEY;
        URL = BASE_URL+REQUEST+params;
    }

    public void sendRequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject json_main = object.optJSONObject("main");
                        JSONArray json_weather = object.optJSONArray("weather");
                        Gson outGson = new Gson();
                        main = outGson.fromJson(json_main.toString(), Main.class);
                        try {
                            weather = outGson.fromJson(json_weather.getJSONObject(0).toString(), Weather.class);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context,weather.getDescription(),Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error por culpa de Jaime",Toast.LENGTH_LONG).show();
                    }
                }
                );
        queue.add(jsObjRequest);
    }
}