package me.aleiv.gameengine.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class SoundUtils {

    public static void playSound(String sound, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 10L, pitch));
    }

    public static void playSound(Sound sound, float pitch) {
        playSound(sound.getKey().getKey(), pitch);
    }

    public static void playSound(Location location, String sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(location, sound, volume, pitch));
    }

    public static void playDirectionalSound(Location loc, String sound, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            int distance = (int) loc.distance(player.getLocation())-3;
            player.playSound(player.getLocation(), sound, (long) Math.max(distance, 0.5), pitch);
        });
    }

    public static void playBeastSound(List<Player> beasts, String sound) {
        beasts.forEach(p -> p.getLocation().getWorld().playSound(p.getLocation(), sound, 15L, 1.0F));
    }

}
