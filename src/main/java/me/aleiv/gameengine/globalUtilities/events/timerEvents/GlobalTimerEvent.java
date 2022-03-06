package me.aleiv.gameengine.globalUtilities.events.timerEvents;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.GlobalTimer;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class GlobalTimerEvent extends Event {

    private final GlobalTimer timer;

    public GlobalTimerEvent(GlobalTimer participant) {
        this.timer = participant;
    }

    public GlobalTimer getTimer() {
        return this.timer;
    }

}