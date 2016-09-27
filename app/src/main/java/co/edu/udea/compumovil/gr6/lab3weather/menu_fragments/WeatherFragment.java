package co.edu.udea.compumovil.gr6.lab3weather.menu_fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.edu.udea.compumovil.gr6.lab3weather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {


    public WeatherFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_weather, container, false);
        SharedPreferences preferencias = fragment.getContext().getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        String ciudad = preferencias.getString("ciudad", "Medellin");
        TextView cityWeather = (TextView) fragment.findViewById(R.id.Clima_Ciudad);
        cityWeather.setText(getResources().getString(R.string.weather_In) + " " + ciudad);
        return fragment;

    }


}
