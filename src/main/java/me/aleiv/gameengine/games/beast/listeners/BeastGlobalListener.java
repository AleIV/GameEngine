package me.aleiv.gameengine.games.beast.listeners;

import me.aleiv.gameengine.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BeastGlobalListener implements Listener{
    
    Core instance;

    public BeastGlobalListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e instanceof EntityDamageByEntityEvent)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (this.check(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (this.check(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (this.check(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();

        if (this.check(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();

        if (this.check(p)) {
            e.setCancelled(true);
        }
    }

    private boolean check(Player player) {
        return !instance.getGamesManager().getPlayerManager().isAdmin(player);
    }
    
}
