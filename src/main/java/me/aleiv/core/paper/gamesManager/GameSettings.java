package me.aleiv.core.paper.gamesManager;

import lombok.Data;

@Data
public class GameSettings {
    
    EngineGameMode engineGameMode;
    boolean autoStart;
    int minStartPlayers;

    public GameSettings(){
        this.engineGameMode = EngineGameMode.NONE;
        this.autoStart = false;
        this.minStartPlayers = 4;
        
    }

    public GameSettings(EngineGameMode engineGameMode, boolean autoStart, int minStartPlayers){
        this.engineGameMode = engineGameMode;
        this.autoStart = autoStart;
        this.minStartPlayers = minStartPlayers;
    }

    public enum EngineGameMode {
        NONE, TOWERS, NEXUS, UHC, BINGO, BEAST
    }

}
