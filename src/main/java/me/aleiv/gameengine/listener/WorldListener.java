package me.aleiv.gameengine.listener;

import me.aleiv.gameengine.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class WorldListener implements Listener {

    private final Core instance;

    public WorldListener(final Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onFireSpread(BlockIgniteEvent e) {
        e.setCancelled(true);
    }

}
