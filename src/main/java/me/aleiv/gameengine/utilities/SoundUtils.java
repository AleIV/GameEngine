package me.aleiv.gameengine.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class SoundUtils {

    public static void playSound(String sound, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 100L, pitch));
    }

    public static void playSound(Sound sound, float pitch) {
        playSound(sound.getKey().getKey(), pitch);
    }

    public static void playSound(Location location, String sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(location, sound, volume, pitch));
    }

    public static void playDirectionalSound(Location loc, String sound, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), sound, (long) Math.max(getVolume(loc, player.getLocation(), -3), 0.5), pitch);
        });
    }

    public static void playBeastSound(List<Player> beasts, String sound) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (beasts.contains(p)) {
                p.playSound(p.getLocation(), sound, 0.5f, 1.0F);
            } else {
                beasts.forEach(b -> {
                    p.playSound(b.getLocation(), sound, getVolume(b.getLocation(), p.getLocation(), -2), 1.0F);
                });
            }
        });
    }

    private static float getVolume(Location loc, Location playerLoc, double addition) {
        return (float) ((loc.distance(playerLoc)+addition)*(1/16.0));
    }

}
