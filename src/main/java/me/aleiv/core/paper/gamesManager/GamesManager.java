package me.aleiv.core.paper.gamesManager;

import java.util.HashMap;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.BeastEngine;
import me.aleiv.core.paper.games.towers.TowersEngine;
import me.aleiv.core.paper.gamesManager.GameSettings.EngineGameMode;
import me.aleiv.core.paper.globalUtilities.GlobalTimer;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;

public class GamesManager {
    Core instance;

    GlobalTimer timer;
    GameSettings gameSettings;
    HashMap<EngineGameMode, BaseEngine> gameEngineList = new HashMap<>();


    public GamesManager(Core instance){
        this.instance = instance;

        timer = new GlobalTimer(instance);
        timer.runTaskTimerAsynchronously(instance, 0L, 20L);

        //TODO: CHANGE TO PULL FROM JSON

        gameSettings = new GameSettings();

        gameSettings.setEngineGameMode(EngineGameMode.TOWERS);

        initGameEngines();

        runGame();        
    }

    public BaseEngine getCurrentGame(){
        var engineGameMode = gameSettings.getEngineGameMode();
        return gameEngineList.get(engineGameMode);
    }

    private void initGameEngines(){

        gameEngineList.put(EngineGameMode.TOWERS, new TowersEngine(instance));
        gameEngineList.put(EngineGameMode.BEAST, new BeastEngine(instance));

    }

    private void runGame(){
        var engineGameMode = gameSettings.getEngineGameMode();
        
        if(engineGameMode == EngineGameMode.NONE) return;

        gameEngineList.get(engineGameMode).enable();
    }
    
}
