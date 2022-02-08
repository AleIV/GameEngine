package me.aleiv.core.paper.globalUtilities.objects;

import me.aleiv.core.paper.gamesManager.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Participant {

    private final UUID playerUUID;
    private final String playerName;
    private PlayerRole playerRole;

    public Participant(Player player, PlayerRole playerRole) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerRole = playerRole;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
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

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

}
