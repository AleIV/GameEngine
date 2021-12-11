package me.aleiv.core.paper.gamesManager;

import java.util.HashMap;

import me.aleiv.core.paper.Core;
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

        initGameEngine();

        runGame();        
    }

    private void initGameEngine(){

        gameEngineList.put(EngineGameMode.TOWERS, new TowersEngine(instance));

    }

    public void runGame(){
        var engineGameMode = gameSettings.getEngineGameMode();
        gameEngineList.get(engineGameMode).enable();
    }
    
}
