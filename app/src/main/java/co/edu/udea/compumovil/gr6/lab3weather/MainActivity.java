package co.edu.udea.compumovil.gr6.lab3weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.edu.udea.compumovil.gr6.lab3weather.service.Weather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent t = new Intent(this, Weather.class);
        t.putExtra("ciudad", "medellin");
        this.startService(t);
    }
}
