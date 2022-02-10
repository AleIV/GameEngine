package me.aleiv.core.paper.utilities;

import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtils {

    public static void spawnWinnerFirework(final Location location) {
        final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        final FireworkMeta meta = firework.getFireworkMeta();
        final FireworkEffect effect = FireworkEffect.builder().withColor(Color.GREEN).withFade(Color.RED).with(FireworkEffect.Type.BALL_LARGE).trail(true).build();
        meta.addEffect(effect);
        meta.setPower(50);
        firework.setFireworkMeta(meta);

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), firework::detonate, 5L);
    }

}
