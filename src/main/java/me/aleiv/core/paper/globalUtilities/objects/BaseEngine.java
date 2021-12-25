package me.aleiv.core.paper.globalUtilities.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Data;
import me.aleiv.core.paper.globalUtilities.EngineEnums.GameStage;

@Data
public abstract class BaseEngine {
    
    GameStage gameStage = GameStage.LOBBY;
    Location lobbySpawn = new Location(Bukkit.getWorlds().get(0), 0, 180, 0);

    public abstract void enable();
    public abstract void disable();

}
