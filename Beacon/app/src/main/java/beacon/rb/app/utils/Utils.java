package beacon.rb.app.utils;

/**
 * Created by jesus.martinez on 04/11/2015.
 */
public class Utils {

    public static double roundToDecimals(double d, int c) {
        //Redondeamos con valor absoluto ya que los .5 los redondea siempre hacia arriba y no es lo mismo en los numero negativos que positivos.
        if (d < 0)
            return (-1) * Math.round(Math.abs(d)*Math.pow(10,c))/Math.pow(10,c);
        else
            return Math.round(d*Math.pow(10,c))/Math.pow(10,c);
    }

    public static double roundTwoDecimals(double d) {
        return roundToDecimals(d, 2);
    }

    public static String formatFourDecimals(double d){
        return String.format("%.4f", roundToDecimals(d,4));
    }
}
