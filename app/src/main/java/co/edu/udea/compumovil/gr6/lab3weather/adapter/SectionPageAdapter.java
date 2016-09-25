package co.edu.udea.compumovil.gr6.lab3weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.edu.udea.compumovil.gr6.lab3weather.menu_fragments.SettingsFragment;
import co.edu.udea.compumovil.gr6.lab3weather.menu_fragments.WeatherFragment;

/**
 * Created by brayan on 23/09/16.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WeatherFragment();
            case 1:
                return new SettingsFragment();
            default:
                return new  SettingsFragment();

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Estado del clima";
            case 1:
                return "ajustes";
            default:
                return "thirdTab";
        }


    }
}
