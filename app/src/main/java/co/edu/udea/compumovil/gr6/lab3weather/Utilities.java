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

    public static String getIDCity(String city) {
        if (city.equalsIgnoreCase("Medellin")) {
            return "3674962";
        }
        if (city.equalsIgnoreCase("Bogota")) {
            return "3688689";
        }
        if (city.equalsIgnoreCase("Cali")) {
            return "3687925";
        }
        if (city.equalsIgnoreCase("Popayan")) {
            return "3671916";
        }
        if (city.equalsIgnoreCase("Santa Marta")) {
            return "3668605";
        }
        if (city.equalsIgnoreCase("Cartagena")) {
            return "3687238";
        }
        if (city.equalsIgnoreCase("Mocoa")) {
            return "3674654";
        }
        if (city.equalsIgnoreCase("Pasto")) {
            return "3672778";
        }
        if (city.equalsIgnoreCase("Tumaco")) {
            return "3666640";
        }
        if (city.equalsIgnoreCase("Envigado")) {
            return "3682631";
        }
        if (city.equalsIgnoreCase("Barranquilla")) {
            return "3689147";
        }
        if (city.equalsIgnoreCase("Pereira")) {
            return "3672486";
        }
        if (city.equalsIgnoreCase("Ibague")) {
            return "3680656";
        }
        if (city.equalsIgnoreCase("Bucaramanga")) {
            return "3688465";
        }
        if (city.equalsIgnoreCase("Bello")) {
            return "3688928";
        }
        if (city.equalsIgnoreCase("Neiva")) {
            return "3673899";
        }

        if (city.equalsIgnoreCase("Cucuta")) {
            return "3685533";
        }
        if (city.equalsIgnoreCase("Buenaventura")) {
            return "3688452";
        }
        return "";
    }
}
