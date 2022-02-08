package me.aleiv.core.paper.listener;

import me.aleiv.core.paper.Core;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Core plugin;

    public static final String joinMessage = ChatColor.translateAlternateColorCodes('&', "&7[&a+&7] &a%player% &7se ha unido a la partida.");
    public static final String leaveMessage = ChatColor.translateAlternateColorCodes('&', "&7[&c-&7] &a%player% &7se ha salido de la partida.");

    public PlayerListener(final Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (plugin.getGamesManager().getRoleManager().isPlayer(player)) {
            plugin.getGamesManager().getCurrentGame().joinPlayer(player);
            e.setJoinMessage(joinMessage.replaceAll("%player%", e.getPlayer().getName()));
            plugin.getGamesManager().updatePlayerCount();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (plugin.getGamesManager().getRoleManager().isPlayer(player)) {
            plugin.getGamesManager().getCurrentGame().leavePlayer(player);
            e.setQuitMessage(leaveMessage.replaceAll("%player%", e.getPlayer().getName()));
            plugin.getGamesManager().updatePlayerCount();
        }
    }

}
