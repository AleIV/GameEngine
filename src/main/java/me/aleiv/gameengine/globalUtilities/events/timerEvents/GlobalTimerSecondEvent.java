package me.aleiv.gameengine.globalUtilities.events.timerEvents;

import lombok.Getter;
import me.aleiv.gameengine.globalUtilities.GlobalTimer;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GlobalTimerSecondEvent extends GlobalTimerEvent {

    private static final @Getter HandlerList HandlerList = new HandlerList();
    private final @Getter HandlerList Handlers = HandlerList;

    private final @Getter int seconds;

    public GlobalTimerSecondEvent(GlobalTimer globalTimer, int seconds) {
        super(globalTimer);
        this.seconds = seconds;
    }

}
