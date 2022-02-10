package me.aleiv.core.paper.games.beast.listeners;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.BeastEngine;
import me.aleiv.core.paper.globalUtilities.events.inGameEvents.ParticipantDeathEvent;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

    @EventHandler
    public void onButtonInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock().getType() == Material.STONE_BUTTON) {
            e.setCancelled(true);
            this.beastEngine.giveBeastItems(e.getPlayer());
        }
    }

}
