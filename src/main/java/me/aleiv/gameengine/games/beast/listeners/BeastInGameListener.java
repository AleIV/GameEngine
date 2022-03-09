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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) && !(e.getDamager() instanceof Player)) {
            e.setCancelled(true);
            return;
        }

        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        boolean damaged = false;
        e.setDamage(0);
        if ((beastEngine.getBeasts().contains(player) && beastEngine.getBeasts().contains(damager)) || (!beastEngine.getBeasts().contains(player) && !beastEngine.getBeasts().contains(damager))) {
            e.setCancelled(true);
        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("SWORD") || damager.getInventory().getItemInOffHand().getType().toString().contains("SWORD")) {
            double newHealth = player.getHealth()-8;
            player.setHealth(newHealth >= 0 ? newHealth : 0);
            damaged = true;
        } else if (beastEngine.getBeasts().contains(damager)) {
            double newHealth = player.getHealth()-(hasArmor(player) ? 4 : 10);
            player.setHealth(newHealth >= 0 ? newHealth : 0);
            damaged = true;
        } else {
            e.setCancelled(true);
        }

        if (damaged) {
            player.setNoDamageTicks(15);
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

        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) {
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
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (instance.getGamesManager().getPlayerManager().isPlayer(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (instance.getGamesManager().getPlayerManager().isPlayer((Player) e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (instance.getGamesManager().getPlayerManager().isPlayer((Player) e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

}
