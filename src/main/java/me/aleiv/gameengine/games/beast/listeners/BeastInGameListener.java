package me.aleiv.gameengine.games.beast.listeners;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.events.participantEvents.ParticipantDeathEvent;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
        if (part.isDead() || this.beastEngine.getGameStage() != EngineEnums.GameStage.INGAME) return;

        part.setDead(true);

        this.beastEngine.checkPlayerCount();
        this.beastEngine.playKillSound(player.getLocation());
        player.getLocation().getWorld().strikeLightningEffect(player.getLocation());
        e.getBukkitEvent().getDrops().clear();
        if (this.instance.getGamesManager().getGameSettings().getKickOnDeath()) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cHas sido expulsado de la partida porque has muerto."));
            return;
        }
        this.instance.broadcast(ChatColor.RED + "El jugador " + player.getName() + " ha sido eliminado.");

        Location lastLoc = player.getLocation().clone();
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            player.spigot().respawn();
            player.teleport(lastLoc);
            player.setGameMode(GameMode.SPECTATOR);
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
        if (beastEngine.getBeasts().contains(player) && beastEngine.getBeasts().contains(damager)) {
            e.setCancelled(true);
        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("SWORD") || damager.getInventory().getItemInOffHand().getType().toString().contains("SWORD")) {
            player.setHealth(player.getHealth()-2);
        } else if (beastEngine.getBeasts().contains(damager)) {
            player.setHealth(player.getHealth()-(hasArmor(player) ? 2 : 10));
        } else {
            e.setCancelled(true);
        }
    }

    private boolean hasArmor(Player player) {
        return player.getInventory().getHelmet() != null && player.getInventory().getChestplate() != null && player.getInventory().getLeggings() != null && player.getInventory().getBoots() != null;
    }

    @EventHandler
    public void onButtonInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock().getType() == Material.STONE_BUTTON) {
            e.setCancelled(true);
            this.beastEngine.giveBeastItems(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo().getBlockY() < 0 || e.getTo().getBlock().getType() == Material.LAVA) {
            e.getPlayer().setHealth(0);
        }
    }

}
