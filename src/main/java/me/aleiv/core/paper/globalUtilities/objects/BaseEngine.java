package me.aleiv.core.paper.globalUtilities.objects;

import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Data;
import me.aleiv.core.paper.globalUtilities.EngineEnums.GameStage;

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
    public abstract void restartGame();

}
