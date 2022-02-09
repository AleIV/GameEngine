package me.aleiv.core.paper.games.beast.listeners;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.BeastEngine;
import me.aleiv.core.paper.globalUtilities.events.inGameEvents.ParticipantDeathEvent;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BeastInGameListener implements Listener{
    
    Core instance;
    BeastEngine beastEngine;

    public BeastInGameListener(Core instance, BeastEngine beastEngine){
        this.instance = instance;
        this.beastEngine = beastEngine;
    }

    @EventHandler
    public void onPartDeath(ParticipantDeathEvent e) {
        Player player = e.getPlayer();
        Participant part = e.getParticipant();

        if (this.instance.getGamesManager().getGameSettings().getKickOnDeath()) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cHas sido expulsado de la partida porque has muerto."));
            return;
        }
        Bukkit.broadcast(ChatColor.RED + "El jugador " + player.getName() + " ha sido eliminado.", "");

        Location lastLoc = player.getLocation().clone();
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            player.spigot().respawn();
            player.teleport(lastLoc);
            player.setGameMode(GameMode.SPECTATOR);
            part.setDead(true);
        }, 1L);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) && !(e.getDamager() instanceof Player)) {
            e.setCancelled(true);
            return;
        }

        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        e.setDamage(0);
        if (beastEngine.getBeasts().contains(damager)) {
            e.setDamage(10);
        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("SWORD") || damager.getInventory().getItemInOffHand().getType().toString().contains("SWORD")) {
            e.setDamage(2);
        }
    }

}
