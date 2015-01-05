package de.kwirz.yapne.utils;


/**
 * Klasse enthält sonstige Hilfsfunktionen
 *
 */
public class Utils {

    /**
     * Gibt {@code true} zurück falls {@code value >= min && value <= max}
     */
    public static <T extends Comparable> boolean inRange(T value, T min, T max) {
        return value.compareTo(min) >=0 && value.compareTo(max) <= 0;
    }

    /**
     * Versichert dass {@code value} nicht kleiner als {@code min} und nicht größer als
     * {@code max} ist
     * @see #inRange
     */
    public static <T extends Comparable> T ensureRange(T value, T min, T max) {
        if (inRange(value, min, max))
            return value;

        return value.compareTo(min) < 0 ? min : max;
    }
}
