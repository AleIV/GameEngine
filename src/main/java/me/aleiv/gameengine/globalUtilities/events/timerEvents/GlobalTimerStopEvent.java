package me.aleiv.gameengine.globalUtilities.events.timerEvents;

import lombok.Getter;
import me.aleiv.gameengine.globalUtilities.GlobalTimer;
import org.bukkit.event.HandlerList;

public class GlobalTimerStopEvent extends GlobalTimerEvent {

    private static final @Getter HandlerList HandlerList = new HandlerList();
    private final @Getter HandlerList Handlers = HandlerList;

    private final @Getter boolean forced;

    public GlobalTimerStopEvent(GlobalTimer globalTimer, boolean forced) {
        super(globalTimer);
        this.forced = forced;
    }

}
