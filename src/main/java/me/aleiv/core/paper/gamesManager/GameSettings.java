package me.aleiv.core.paper.gamesManager;

import lombok.Data;

@Data
public class GameSettings {
    
    EngineGameMode engineGameMode;
    

    public GameSettings(){
        this.engineGameMode = EngineGameMode.NONE;
    }

    public GameSettings(EngineGameMode engineGameMode){
        this.engineGameMode = engineGameMode;
    }

    public enum EngineGameMode {
        NONE, TOWERS, NEXUS, UHC, BINGO, BEAST
    }

}
