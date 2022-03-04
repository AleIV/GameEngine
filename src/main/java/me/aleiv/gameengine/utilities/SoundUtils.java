package me.aleiv.gameengine.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

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

}
