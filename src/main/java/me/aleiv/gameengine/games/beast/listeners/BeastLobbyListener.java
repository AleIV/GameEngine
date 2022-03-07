package me.aleiv.gameengine.games.beast.listeners;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerSecondEvent;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerStopEvent;
import me.aleiv.gameengine.globalUtilities.objects.generic.LobbyListener;
import me.aleiv.gameengine.utilities.Animation;
import me.aleiv.gameengine.utilities.Frames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BeastLobbyListener extends LobbyListener{


    public BeastLobbyListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }



}
