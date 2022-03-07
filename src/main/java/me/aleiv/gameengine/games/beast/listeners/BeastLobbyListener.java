package me.aleiv.gameengine.games.beast.listeners;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.gamesManager.PlayerRole;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerSecondEvent;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerStopEvent;
import me.aleiv.gameengine.globalUtilities.objects.generic.LobbyListener;
import me.aleiv.gameengine.utilities.Animation;
import me.aleiv.gameengine.utilities.Frames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BeastLobbyListener extends LobbyListener{

    Core instance;

    public BeastLobbyListener(Core instance) {
        super(instance);
        this.instance = instance;
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (instance.getGamesManager().getPlayerManager().getPlayerRole(p) == PlayerRole.ADMIN) {
                return;
            }
        }
        e.setCancelled(true);
    }

}
