package me.aleiv.core.paper.games.beast;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.commands.BeastCMD;
import me.aleiv.core.paper.games.beast.listeners.BeastGlobalListener;
import me.aleiv.core.paper.games.beast.listeners.BeastInGameListener;
import me.aleiv.core.paper.games.beast.listeners.BeastLobbyListener;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;

public class BeastEngine extends BaseEngine{

    Core instance;

    BeastCMD beastCMD;
    BeastGlobalListener beastGlobalListener;
    BeastInGameListener beastInGameListener;
    BeastLobbyListener beastLobbyListener;

    public BeastEngine(Core instance){
        this.beastCMD = new BeastCMD(instance);
        this.beastGlobalListener = new BeastGlobalListener(instance);
        this.beastInGameListener = new BeastInGameListener(instance);
        this.beastLobbyListener = new BeastLobbyListener(instance);

    }

    @Override
    public void enable(){
        instance.getCommandManager().registerCommand(beastCMD);
        instance.registerListener(beastGlobalListener);
        instance.registerListener(beastInGameListener);
        instance.registerListener(beastLobbyListener);

    }

    @Override
    public void disable(){
        instance.getCommandManager().unregisterCommand(beastCMD);
        instance.unregisterListener(beastGlobalListener);
        instance.unregisterListener(beastInGameListener);
        instance.unregisterListener(beastLobbyListener);

    }

    @Override
    public void startGame() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void restartGame() {
        // TODO Auto-generated method stub
        
    }
}
