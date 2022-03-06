package me.aleiv.gameengine.games.beast.listeners;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerSecondEvent;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerStopEvent;
import me.aleiv.gameengine.globalUtilities.objects.generic.LobbyListener;
import me.aleiv.gameengine.utilities.Animation;
import me.aleiv.gameengine.utilities.Frames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BeastLobbyListener extends LobbyListener{

    private final BeastEngine engine;
    private final Animation animation;

    public BeastLobbyListener(Core instance, BeastEngine engine) {
        super(instance);
        this.engine = engine;
        this.animation = new Animation(Frames.getFramesChars(0, 281), false);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onTimerTick(GlobalTimerSecondEvent e) {
        if (e.getSeconds() == 10 && engine.getGameStage() == EngineEnums.GameStage.PREGAME) {
            animation.play();
        }
    }

    @EventHandler
    public void onTimerStop(GlobalTimerStopEvent e) {
        animation.stop();
    }

}
