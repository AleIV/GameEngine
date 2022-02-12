package me.aleiv.core.paper.globalUtilities.events.participantEvents;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ParticipantEvent extends Event {

    private final Participant participant;

    public ParticipantEvent(Participant participant) {
        this.participant = participant;
    }

    public ParticipantEvent(Player player) {
        this.participant = Core.getInstance().getGamesManager().getPlayerManager().getParticipant(player);
    }

    public Participant getParticipant() {
        return this.participant;
    }

    public Player getPlayer() {
        return this.participant.getPlayer();
    }

}
