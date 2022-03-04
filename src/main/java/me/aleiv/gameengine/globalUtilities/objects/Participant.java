package me.aleiv.gameengine.globalUtilities.objects;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.aleiv.gameengine.gamesManager.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Participant {

    private @Getter final UUID playerUUID;
    private final String playerName;
    private @Getter @Setter PlayerRole playerRole;
    private @Getter @Setter boolean dead;

    public Participant(@NonNull Player player, @NonNull PlayerRole playerRole) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerRole = playerRole;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public boolean isConnnected() {
        return getPlayer() != null;
    }

    public String getPlayerName() {
        return playerName;
    }

}
