package me.aleiv.gameengine.globalUtilities.objects;

import lombok.Data;
import me.aleiv.gameengine.exceptions.GameStartException;
import me.aleiv.gameengine.globalUtilities.config.BaseConfig;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Data
public abstract class BaseEngine {
    
    EngineEnums.GameStage gameStage = EngineEnums.GameStage.LOBBY;
    Location lobbySpawn = new Location(Bukkit.getWorlds().get(0), 0, 180, 0);
    final BaseConfig gameConfig;

    public BaseEngine(BaseConfig config) {
        this.gameConfig = config;
    }

    public abstract void enable();
    public abstract void disable();
    public abstract void startGame() throws GameStartException;
    public abstract void stopGame();
    public abstract void restartGame();
    public abstract boolean joinPlayer(Player player);
    public abstract void leavePlayer(Player player);

}
