package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.EngineEnums;
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
        if (instance.getGamesManager().isGameLoaded()) {
            EngineEnums.GameStage gs = instance.getGamesManager().getCurrentGame().getGameStage();
            if (gs == EngineEnums.GameStage.INGAME || gs == EngineEnums.GameStage.POSTGAME) {
                player.sendMessage("§cA game is already running!");
                return;
            }
            instance.getGamesManager().startGame();
            player.sendMessage("§aGame started!");
            return;
        }

        player.sendMessage("§cThere is no game loaded!");
    }

    @Subcommand("stop")
    public void onStop(Player player) {
        if (!instance.getGamesManager().isGameLoaded()) {
            player.sendMessage("§cThere is no game loaded!");
            return;
        }

        EngineEnums.GameStage gs = instance.getGamesManager().getCurrentGame().getGameStage();

        if (gs != EngineEnums.GameStage.INGAME) {
            player.sendMessage("§cThere is no game running!");
            return;
        }

        instance.getGamesManager().stopGame(true);
        player.sendMessage("§aGame stopped!");
    }

}
