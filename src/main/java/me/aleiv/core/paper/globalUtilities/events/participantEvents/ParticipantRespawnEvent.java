package me.aleiv.core.paper.globalUtilities.events.participantEvents;

import lombok.Getter;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ParticipantRespawnEvent extends ParticipantEvent {

    private static final @Getter HandlerList HandlerList = new HandlerList();
    private final @Getter HandlerList Handlers = HandlerList;

    public ParticipantRespawnEvent(Participant participant) {
        super(participant);
    }

    public ParticipantRespawnEvent(Player player) {
        super(player);
    }
}
