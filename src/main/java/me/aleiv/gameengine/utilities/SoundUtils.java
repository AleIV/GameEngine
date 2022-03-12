package me.aleiv.gameengine.utilities;

import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

  public static void playSound(String sound, float pitch) {
    Bukkit.getOnlinePlayers()
        .forEach(player -> player.playSound(player.getLocation(), sound, 100L, pitch));
  }

  public static void playSound(Sound sound, float pitch) {
    playSound(sound.getKey().getKey(), pitch);
  }

  public static void playSound(Location location, String sound, float volume, float pitch) {
    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(location, sound, volume, pitch));
  }

  public static void playDirectionalSound(Location loc, String sound, float pitch) {
    Bukkit.getOnlinePlayers().stream()
        .filter(player -> player.getWorld() == loc.getWorld())
        .forEach(
            player -> {
              player.playSound(
                  player.getLocation(),
                  sound,
                  (long) Math.max(getVolume(loc, player.getLocation(), -3), 0.5),
                  pitch);
            });
  }

  public static void playBeastSound(List<Player> beasts, String sound) {
    float randomPitch = (new Random().nextInt(6) / 10.0f) + 0.65f;

    Bukkit.getOnlinePlayers()
        .forEach(
            p -> {
              if (beasts.contains(p)) {
                p.playSound(p.getLocation(), sound, 0.5f, randomPitch);
              } else {
                beasts.forEach(
                    b -> {
                      if (b.getLocation().getWorld() == p.getWorld()
                          && b.getLocation().distance(p.getLocation()) <= 15) {
                        p.playSound(
                            b.getLocation(),
                            sound,
                            getVolume(b.getLocation(), p.getLocation(), -2),
                            randomPitch);
                      }
                    });
              }
            });
  }

  private static float getVolume(Location loc, Location playerLoc, double addition) {
    double distance = loc.distance(playerLoc);
    float calculation = (float) ((distance + addition) * (1 / 16.0));

    return distance >= 16 ? calculation : 1 - calculation;
  }
}
