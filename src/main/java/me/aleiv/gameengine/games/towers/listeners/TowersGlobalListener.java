package me.aleiv.gameengine.games.towers.listeners;

import me.aleiv.gameengine.Core;
import org.bukkit.event.Listener;

public class TowersGlobalListener implements Listener{
    
    Core instance;

    public TowersGlobalListener(Core instance){
        this.instance = instance;
    }
}
