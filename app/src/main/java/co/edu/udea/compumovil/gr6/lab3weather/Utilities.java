package co.edu.udea.compumovil.gr6.lab3weather;

/**
 * Created by jaime on 25/09/2016.
 */

public class Utilities {

    public static String kelvinToCelsius(String fahren) {
        float k = 0F;
        try {
            k = Float.parseFloat(fahren);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int c = (int) (k - 273.15);
        return c + "";
    }
}
