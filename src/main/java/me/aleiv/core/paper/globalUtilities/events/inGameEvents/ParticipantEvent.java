package me.aleiv.core.paper.globalUtilities.events.inGameEvents;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ParticipantEvent extends Event {

    public static HandlerList handlerList;

    private final Participant participant;

    public ParticipantEvent(Participant participant) {
        this.participant = participant;
    }

    public ParticipantEvent(Player player) {
        this.participant = Core.getInstance().getGamesManager().getPlayerManager().getParticipant(player);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Participant getParticipant() {
        return this.participant;
    }

    public Player getPlayer() {
        return this.participant.getPlayer();
    }

}
