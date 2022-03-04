package me.aleiv.gameengine.games.towers.listeners;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.events.participantEvents.ParticipantRespawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class TowersInGameListener implements Listener{
    
    Core instance;

    public TowersInGameListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void setParticipantRespawnEvent(PlayerRespawnEvent e){
        Bukkit.getPluginManager().callEvent(new ParticipantRespawnEvent(e.getPlayer()));
    }

    @EventHandler
    public void respawnEvent(ParticipantRespawnEvent e){
        
    }
    
}
