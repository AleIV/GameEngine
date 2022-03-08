package me.aleiv.gameengine.listener;

import me.aleiv.gameengine.Core;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GamemodeHiderListener implements Listener {

    private final Core instance;

    public GamemodeHiderListener(final Core instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onGamemodeChange(PlayerGameModeChangeEvent e) {
        if (e.isCancelled()) return;

        GameMode newGamemode = e.getNewGameMode();
        if (newGamemode == GameMode.SPECTATOR || newGamemode == GameMode.CREATIVE) {
            hide(e.getPlayer());
        } else {
            unhide(e.getPlayer());
        }
    }

    private void hide(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(instance, player));
    }

    private void unhide(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(instance, player));
    }

}
