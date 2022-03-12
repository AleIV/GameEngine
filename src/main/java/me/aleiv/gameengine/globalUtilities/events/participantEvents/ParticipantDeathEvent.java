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

    public ParticipantDeathEvent(Participant participant, @Nullable Entity killer) {
        super(participant);
        this.killer = killer;
    }

    public ParticipantDeathEvent(Player player, @Nullable Entity killer) {
        this(Core.getInstance().getGamesManager().getPlayerManager().getParticipant(player), killer);
    }

}
