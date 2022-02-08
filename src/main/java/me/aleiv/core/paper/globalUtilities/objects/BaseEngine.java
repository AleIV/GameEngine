package me.aleiv.core.paper.globalUtilities.objects;

import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Data;
import me.aleiv.core.paper.globalUtilities.EngineEnums.GameStage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public abstract class BaseEngine {
    
    GameStage gameStage = GameStage.LOBBY;
    Location lobbySpawn = new Location(Bukkit.getWorlds().get(0), 0, 180, 0);
    final BaseConfig gameConfig;

    public BaseEngine(BaseConfig config) {
        this.gameConfig = config;
    }

    public abstract void enable();
    public abstract void disable();
    public abstract void startGame();
    public abstract void stopGame();
    public abstract void restartGame();
    public abstract void joinPlayer(Player player);
    public abstract void leavePlayer(Player player);

}
