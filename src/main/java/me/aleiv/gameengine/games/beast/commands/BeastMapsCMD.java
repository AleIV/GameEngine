package me.aleiv.gameengine.games.beast.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.games.beast.gui.BeastMapsGUI;
import org.bukkit.entity.Player;

@CommandAlias("maps")
@CommandPermission("beast.maps")
public class BeastMapsCMD extends BaseCommand{
    
    private final BeastEngine engine;
    
    public BeastMapsCMD(BeastEngine engine){
        this.engine = engine;
    }

    @Default
    @CatchUnknown
    public void onDefault(Player player) {
        new BeastMapsGUI(player, engine);
    }
}
