package co.edu.udea.compumovil.gr6.lab3weather;


import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import co.edu.udea.compumovil.gr6.lab3weather.adapter.SectionPageAdapter;
import co.edu.udea.compumovil.gr6.lab3weather.webService.Volley;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.submit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SectionPageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        /*
        Intent t = new Intent(this, WeatherService.class);
        t.putExtra("ciudad", "medellin");
        this.startService(t);
        */
    }
    public void aceptar(View v){
        Volley volley = new Volley ("medellin",getBaseContext());
        volley.sendRequest();
    }

}
