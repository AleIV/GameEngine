package me.aleiv.gameengine.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.utilities.GameUtils;
import org.bukkit.entity.Player;

@CommandAlias("game")
@CommandPermission("game.game")
public class GameCommand extends BaseCommand {

    private final Core instance;

    public GameCommand(Core instance) {
        this.instance = instance;
    }

    @Subcommand("start")
    public void onStart(Player player) {
        GameUtils.formalGameFinish(player);
    }

    @Subcommand("stop")
    public void onStop(Player player) {
        GameUtils.formalGameFinish(player);
    }

}
