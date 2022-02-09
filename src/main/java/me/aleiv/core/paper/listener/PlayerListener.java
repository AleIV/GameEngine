package me.aleiv.core.paper.listener;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.gamesManager.PlayerRole;
import me.aleiv.core.paper.globalUtilities.events.inGameEvents.ParticipantDeathEvent;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Core plugin;

    public static final String joinMessage = ChatColor.translateAlternateColorCodes('&', "&7[&a+&7] &a%player% &7se ha unido a la partida.");
    public static final String leaveMessage = ChatColor.translateAlternateColorCodes('&', "&7[&c-&7] &a%player% &7se ha salido de la partida.");

    public PlayerListener(final Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Participant p = plugin.getGamesManager().getPlayerManager().joinPlayer(player);
        if (p.getPlayerRole() == PlayerRole.PLAYER) {
            e.setJoinMessage(joinMessage.replaceAll("%player%", e.getPlayer().getName()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        Participant p = plugin.getGamesManager().getPlayerManager().leavePlayer(player);
        if (p.getPlayerRole() == PlayerRole.PLAYER) {
            e.setQuitMessage(leaveMessage.replaceAll("%player%", e.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onParticipantDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Participant p = plugin.getGamesManager().getPlayerManager().getParticipant(player);

        e.setDeathMessage(null);

        if (p.getPlayerRole() == PlayerRole.PLAYER && !p.isDead()) {
            Bukkit.getPluginManager().callEvent(new ParticipantDeathEvent(p, player.getKiller()));
        }
    }

}
