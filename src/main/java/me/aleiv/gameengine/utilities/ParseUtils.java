package me.aleiv.gameengine.utilities;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ParseUtils {

    public static String locationStart = "LOCATION=";

    public static String locationToString(Location loc) {
        if (loc == null) return locationStart;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        String worldName = loc.getWorld().getName();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();

        return locationStart + x + ";" + y + ";" + z + ";" + worldName + ";" + yaw + ";" + pitch;
    }

    public static Location stringToLocation(String parsedLocation) {
        if (!parsedLocation.contains(locationStart)) return null;
        String[] parameters = parsedLocation.replaceAll(locationStart, "").replaceAll("\\Q||\\E", "").split("\\Q;\\E");
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

    public static String getLocationLore(@Nullable Location lore) {
        return lore == null ? ChatColor.RED + "No Location Set" : "Location" + "\n" +
                "X: " + formatDouble(lore.getX()) + "\n" +
                "Y: " + formatDouble(lore.getY()) + "\n" +
                "Z: " + formatDouble(lore.getZ()) + "\n" +
                "World: " + lore.getWorld().getName() + "\n" +
                "Yaw: " + formatFloat(lore.getYaw()) + "\n" +
                "Pitch: " + formatFloat(lore.getPitch());
    }

    public static String formatDouble(@NonNull Double d) {
        return String.format("%.2f", d);
    }

    public static String formatFloat(@NonNull Float f) {
        return String.format("%.2f", f);
    }

}
