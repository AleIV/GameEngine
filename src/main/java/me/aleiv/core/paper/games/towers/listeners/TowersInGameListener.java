package me.aleiv.core.paper.games.towers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.events.inGameEvents.ParticipantRespawnEvent;

public class TowersInGameListener implements Listener{
    
    Core instance;

    public TowersInGameListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void setParticipantRespawnEvent(PlayerRespawnEvent e){
        Bukkit.getPluginManager().callEvent(new ParticipantRespawnEvent());
    }

    @EventHandler
    public void respawnEvent(ParticipantRespawnEvent e){
        
    }
    
}
