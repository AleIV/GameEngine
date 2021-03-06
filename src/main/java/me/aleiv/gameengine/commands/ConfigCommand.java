package me.aleiv.gameengine.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.config.BaseConfig;
import me.aleiv.gameengine.gui.ConfigMenu;
import org.bukkit.entity.Player;

@CommandAlias("config")
@CommandPermission("game.config")
public class ConfigCommand extends BaseCommand {

    private final Core plugin;

    public ConfigCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Default
    @CommandCompletion("game")
    public void onDefault(Player player, @Optional String arg) {
        if (arg == null) {
            new ConfigMenu(player, plugin.getGamesManager().getGameSettings(), plugin.getGamesManager().getCurrentGame().getGameConfig());
            return;
        }

        if (arg.equalsIgnoreCase("game")) {
            BaseConfig baseConfig = plugin.getGamesManager().getCurrentGame().getGameConfig();
            new ConfigMenu(player, baseConfig, baseConfig.getSubConfigs());
        }
    }

}
