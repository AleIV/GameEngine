package me.aleiv.gameengine.globalUtilities.objects.generic;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class LobbyListener implements Listener{
    
    Core instance;
    String permission_lobby_edit = "engine.lobby.edit";

    public LobbyListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        var game = instance.getGamesManager().getCurrentGame();
        var player = e.getPlayer();

        if(player.isOp()) return;

        if(game.getGameStage() != EngineEnums.GameStage.INGAME && !player.hasPermission(permission_lobby_edit)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        var game = instance.getGamesManager().getCurrentGame();
        var player = e.getPlayer();

        if(player.isOp()) return;
        if(game.getGameStage() != EngineEnums.GameStage.INGAME && !player.hasPermission(permission_lobby_edit)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        var game = instance.getGamesManager().getCurrentGame();
        var player = e.getPlayer();
        if(game.getGameStage() != EngineEnums.GameStage.INGAME && !player.hasPermission(permission_lobby_edit)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e){
        var game = instance.getGamesManager().getCurrentGame();
        var entity = e.getEntity();
        if(game.getGameStage() != EngineEnums.GameStage.INGAME && entity instanceof Player player && !player.hasPermission(permission_lobby_edit)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        var game = instance.getGamesManager().getCurrentGame();
        
        if(e instanceof EntityDamageByEntityEvent) return;

        if(game.getGameStage() != EngineEnums.GameStage.INGAME){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        var game = instance.getGamesManager().getCurrentGame();
        var entity = e.getDamager();

        if(entity instanceof Player player && player.hasPermission(permission_lobby_edit)) return;

        if(game.getGameStage() != EngineEnums.GameStage.INGAME){
            e.setCancelled(true);
        }
    }
}
