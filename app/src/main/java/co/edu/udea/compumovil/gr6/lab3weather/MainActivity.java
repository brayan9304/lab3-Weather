package co.edu.udea.compumovil.gr6.lab3weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import co.edu.udea.compumovil.gr6.lab3weather.service.WeatherIntent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View e) {

        Intent t = new Intent(getApplicationContext(), WeatherIntent.class);
        t.setAction("co.edu.udea.compumovil.gr6.lab3weather.service.action.WEATHER");
        t.putExtra("ciudad", "medellin");
        startService(t);
    }
}
