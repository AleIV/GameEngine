package me.aleiv.gameengine.games.beast.listeners;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerSecondEvent;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerStopEvent;
import me.aleiv.gameengine.utilities.Animation;
import me.aleiv.gameengine.utilities.Frames;
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
    private final BeastEngine engine;
    private final Animation animation;

    public BeastGlobalListener(Core instance, BeastEngine engine){
        this.instance = instance;
        this.engine = engine;
        this.animation = new Animation(Frames.getFramesCharsIntegers(0, 184), false);
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

    @EventHandler
    public void onTimerTick(GlobalTimerSecondEvent e) {
        if (e.getSeconds() == 10 && engine.getGameStage() == EngineEnums.GameStage.PREGAME) {
            animation.play();
        }
    }

    @EventHandler
    public void onTimerStop(GlobalTimerStopEvent e) {
        animation.stop();
    }

}
