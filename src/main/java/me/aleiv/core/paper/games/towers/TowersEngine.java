package me.aleiv.core.paper.games.towers;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.towers.commands.TowersCMD;
import me.aleiv.core.paper.games.towers.listeners.TowersGlobalListener;
import me.aleiv.core.paper.games.towers.listeners.TowersInGameListener;
import me.aleiv.core.paper.games.towers.listeners.TowersLobbyListener;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;

public class TowersEngine extends BaseEngine{

    Core instance;

    TowersCMD towersCMD;
    TowersGlobalListener towersGlobalListener;
    TowersInGameListener towersInGameListener;
    TowersLobbyListener towersLobbyListener;

    public TowersEngine(Core instance){
        this.towersCMD = new TowersCMD(instance);
        this.towersGlobalListener = new TowersGlobalListener(instance);
        this.towersInGameListener = new TowersInGameListener(instance);
        this.towersLobbyListener = new TowersLobbyListener(instance);

    }

    @Override
    public void enable(){
        instance.getCommandManager().registerCommand(towersCMD);
        instance.registerListener(towersGlobalListener);
        instance.registerListener(towersInGameListener);
        instance.registerListener(towersLobbyListener);

    }

    @Override
    public void disable(){
        instance.getCommandManager().unregisterCommand(towersCMD);
        instance.unregisterListener(towersGlobalListener);
        instance.unregisterListener(towersInGameListener);
        instance.unregisterListener(towersLobbyListener);

    }
    
}
