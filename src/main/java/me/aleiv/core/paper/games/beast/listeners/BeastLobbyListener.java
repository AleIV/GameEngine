package me.aleiv.core.paper.games.beast.listeners;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.objects.generic.LobbyListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BeastLobbyListener extends LobbyListener{

    public BeastLobbyListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }

}
