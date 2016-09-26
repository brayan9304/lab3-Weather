package co.edu.udea.compumovil.gr6.lab3weather.menu_fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr6.lab3weather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {

    private String[] ciudades;
    View fragment;
    String ciudadActual;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        ciudades = fragment.getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> ciudadesAdap = new ArrayAdapter<>(fragment.getContext(), android.R.layout.simple_dropdown_item_1line, ciudades);
        AutoCompleteTextView ciudadesAuto = (AutoCompleteTextView) fragment.findViewById(R.id.ciudad_select);
        ciudadesAuto.setAdapter(ciudadesAdap);
        ciudadesAuto.setOnFocusChangeListener(this);
        ciudadesAuto.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence charSequence) {
                AutoCompleteTextView ciudadesAuto2 = (AutoCompleteTextView) fragment.findViewById(R.id.ciudad_select);
                for (String ciudadselect : ciudades) {
                    if ((ciudadesAuto2.getText() + "").equalsIgnoreCase(ciudadselect)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence charSequence) {
                return null;
            }
        });

        SharedPreferences preferencias = fragment.getContext().getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
        ciudadActual = preferencias.getString("ciudad", "Medellin");
        TextView actual = (TextView) fragment.findViewById(R.id.ciudad_actual);
        actual.setText(fragment.getResources().getString(R.string.ciudadActual) + " " + ciudadActual);
        Button acept = (Button) fragment.findViewById(R.id.submit);
        acept.setOnClickListener(this);

        return fragment;
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.ciudad_select:
                if (!hasFocus) {
                    ((AutoCompleteTextView) view).performValidation();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                AutoCompleteTextView ciudad = (AutoCompleteTextView) fragment.findViewById(R.id.ciudad_select);
                ciudad.performValidation();
                if (!ciudad.getText().toString().equals("")) {
                    SharedPreferences prefs = fragment.getContext().getSharedPreferences("CiudadActualPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("ciudad", ciudad.getText().toString());
                    editor.commit();
                    ciudadActual = prefs.getString("ciudad", "medellin");
                    TextView actual = (TextView) fragment.findViewById(R.id.ciudad_actual);
                    actual.setText(fragment.getResources().getString(R.string.ciudadActual) + " " + ciudadActual);
                    Toast.makeText(fragment.getContext(), ciudad.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
