package me.aleiv.gameengine.globalUtilities.events.participantEvents;

import lombok.Getter;
import lombok.NonNull;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.Nullable;

public class ParticipantDeathEvent extends ParticipantEvent {

    private static final @Getter HandlerList HandlerList = new HandlerList();

    private final @Getter HandlerList Handlers = HandlerList;

    private final @Getter Entity killer;
    private final @Getter PlayerDeathEvent bukkitEvent;

    public ParticipantDeathEvent(Participant participant, @Nullable Entity killer, @NonNull PlayerDeathEvent event) {
        super(participant);
        this.killer = killer;
        this.bukkitEvent = event;
    }

    public ParticipantDeathEvent(Player player, @Nullable Entity killer, @NonNull PlayerDeathEvent event) {
        this(Core.getInstance().getGamesManager().getPlayerManager().getParticipant(player), killer, event);
    }

}
