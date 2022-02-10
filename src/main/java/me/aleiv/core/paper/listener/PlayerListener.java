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


    public PlayerListener(final Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Participant p = plugin.getGamesManager().getPlayerManager().joinPlayer(player);
        Bukkit.broadcast(ChatColor.YELLOW + "", "game.messages.join");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        Participant p = plugin.getGamesManager().getPlayerManager().leavePlayer(player);
        Bukkit.broadcast("", "game.messages.leave");
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
