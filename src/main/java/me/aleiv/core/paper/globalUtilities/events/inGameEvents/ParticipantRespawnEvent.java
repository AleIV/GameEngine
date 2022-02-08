package me.aleiv.core.paper.globalUtilities.events.inGameEvents;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ParticipantRespawnEvent extends Event {
    
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({"java:S116", "java:S1170"})
    private final @Getter HandlerList Handlers = HandlerList;


    public ParticipantRespawnEvent(boolean async) {
        super(async);
    }

    public ParticipantRespawnEvent() {
        this(false);
    }
}
