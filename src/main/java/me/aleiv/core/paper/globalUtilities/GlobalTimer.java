package me.aleiv.core.paper.globalUtilities;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.events.GameTickEvent;

@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalTimer extends BukkitRunnable {
    Core instance;

    long gameTime = 0;
    long startTime = 0;

    public GlobalTimer(Core instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

    }

    @Override
    public void run() {

        var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

        gameTime = new_time;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
    }
}