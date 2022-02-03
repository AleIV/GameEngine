package me.aleiv.core.paper.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ParseUtils {

    public static String locationStart = "LOCATION=";

    public static String locationToString(Location loc) {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        String worldName = loc.getWorld().getName();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();

        return locationStart + x + ";" + y + ";" + z + ";" + worldName + ";" + pitch + ";" + yaw;
    }

    public static Location stringToLocation(String parsedLocation) {
        if (!parsedLocation.contains(locationStart)) return null;
        String[] parameters = parsedLocation.replaceAll(locationStart, "").split(";");
        if (parameters.length != 6) return null;

        double x = Double.parseDouble(parameters[0]);
        double y = Double.parseDouble(parameters[1]);
        double z = Double.parseDouble(parameters[2]);
        World world = Bukkit.getWorld(parameters[3]);
        float yaw = Float.parseFloat(parameters[4]);
        float pitch = Float.parseFloat(parameters[5]);

        if (world == null) return null;

        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getLocationLore(Location lore) {
        return "Location" + "\n" +
                "X: " + lore.getX() + "\n" +
                "Y: " + lore.getY() + "\n" +
                "Z: " + lore.getZ() + "\n" +
                "World: " + lore.getWorld().getName() + "\n" +
                "Yaw: " + lore.getYaw() + "\n" +
                "Pitch: " + lore.getPitch();
    }

}
