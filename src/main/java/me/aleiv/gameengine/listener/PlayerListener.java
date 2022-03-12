package me.aleiv.gameengine.listener;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.gamesManager.PlayerRole;
import me.aleiv.gameengine.globalUtilities.events.participantEvents.ParticipantDeathEvent;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        e.setJoinMessage(null);
        Player player = e.getPlayer();

        Participant p = plugin.getGamesManager().getPlayerManager().joinPlayer(player);
        if (p == null) return; // Cannot enter/already kicked

        Bukkit.broadcast(ChatColor.YELLOW + p.getPlayerName() + " se ha unido a la partida.", "game.messages.join");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player player = e.getPlayer();

        Participant p = plugin.getGamesManager().getPlayerManager().leavePlayer(player);
        if (p == null) return; // Cannot enter/already kicked

        Bukkit.broadcast(ChatColor.YELLOW + p.getPlayerName() + " ha abandonado la partida." , "game.messages.leave");
    }

    @EventHandler
    public void onParticipantDeath(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        if (e.getDamage() >= player.getHealth()) {
            Participant p = plugin.getGamesManager().getPlayerManager().getParticipant(player);
            if (p == null) return;

            if (p.getPlayerRole() == PlayerRole.PLAYER && !p.isDead()) {
                e.setDamage(0);
                Bukkit.getPluginManager().callEvent(new ParticipantDeathEvent(p, e.getDamager()));
            }
        }
    }

    /*@EventHandler
    public void onParticipantDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Participant p = plugin.getGamesManager().getPlayerManager().getParticipant(player);

        e.setDeathMessage(null);

        if (p.getPlayerRole() == PlayerRole.PLAYER && !p.isDead()) {
        }
    }*/

}
