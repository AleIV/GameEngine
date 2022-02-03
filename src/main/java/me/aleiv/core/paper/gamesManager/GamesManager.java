package me.aleiv.core.paper.gamesManager;

import java.util.HashMap;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.BeastEngine;
import me.aleiv.core.paper.games.towers.TowersEngine;
import me.aleiv.core.paper.gamesManager.GameSettings.EngineGameMode;
import me.aleiv.core.paper.globalUtilities.GlobalTimer;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;
import org.bukkit.Bukkit;

public class GamesManager {
    Core instance;

    GlobalTimer timer;
    @Getter GameSettings gameSettings;
    HashMap<EngineGameMode, BaseEngine> gameEngineList = new HashMap<>();


    public GamesManager(Core instance){
        this.instance = instance;

        timer = new GlobalTimer(instance);
        timer.runTaskTimerAsynchronously(instance, 0L, 20L);

        try {
            gameSettings = new GameSettings();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Error while loading game settings");
            Bukkit.getPluginManager().disablePlugin(instance);
        }

        gameSettings.setEngineGameMode(EngineGameMode.BEAST);

        Bukkit.getScheduler().runTask(instance, () -> {
            initGameEngines();
            runGame();
        });
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
