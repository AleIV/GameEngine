package me.aleiv.gameengine.utilities;

public class ObjectUtils {

    public static boolean canBeDouble(String input) {
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean canBeInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean canBeBoolean(String input) {
        return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
    }

}
