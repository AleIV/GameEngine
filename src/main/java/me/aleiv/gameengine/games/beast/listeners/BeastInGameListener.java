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
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BeastInGameListener implements Listener{
    
    Core instance;
    BeastEngine beastEngine;

    private List<UUID> equipedPlayers;

    public BeastInGameListener(Core instance, BeastEngine beastEngine){
        this.instance = instance;
        this.beastEngine = beastEngine;

        this.equipedPlayers = new ArrayList<>();
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
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo().getBlockY() < 0 || e.getTo().getBlock().getType() == Material.LAVA) {
            e.getPlayer().setHealth(0);
            return;
        }

        boolean inside = this.beastEngine.getBeastConfig().getMap().getEquipmentRegion().contains(e.getTo());
        boolean alreadyInside = this.equipedPlayers.contains(e.getPlayer().getUniqueId());
        if (!alreadyInside && inside) {
            this.equipedPlayers.add(e.getPlayer().getUniqueId());
            this.beastEngine.giveBeastItems(e.getPlayer());
        } else if (alreadyInside && !inside) {
            this.equipedPlayers.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onBarrotesDrop(ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getType() == Material.IRON_BARS) {
            e.setCancelled(true);
        }
    }

}
