package me.aleiv.gameengine.globalUtilities.events.participantEvents;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

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
