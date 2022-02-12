package me.aleiv.core.paper.listener;

import me.aleiv.core.paper.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {

    private final Core instance;

    public WorldListener(final Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent e) {
        e.setCancelled(true);
    }

}
