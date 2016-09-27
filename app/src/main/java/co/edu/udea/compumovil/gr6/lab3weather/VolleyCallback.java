package co.edu.udea.compumovil.gr6.lab3weather;

import org.json.JSONObject;

/**
 * Created by jaime on 25/09/2016.
 */

public interface VolleyCallback {
    void onSuccess(JSONObject result);

    void onError();
}
