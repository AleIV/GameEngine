package me.aleiv.core.paper.globalUtilities.events.inGameEvents;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

public class ParticipantDeathEvent extends ParticipantEvent {

    public static HandlerList handlerList;

    private final Entity killer;

    public ParticipantDeathEvent(Participant participant, @Nullable Entity killer) {
        super(participant);
        this.killer = killer;
    }

    public ParticipantDeathEvent(Player player, @Nullable Entity killer) {
        this(Core.getInstance().getGamesManager().getPlayerManager().getParticipant(player), killer);
    }

}
