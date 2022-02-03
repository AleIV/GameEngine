package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.gui.ConfigMenu;
import org.bukkit.entity.Player;

@CommandAlias("config")
@CommandPermission("game.config")
public class ConfigCommand extends BaseCommand {

    private final Core plugin;

    public ConfigCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onDefault(Player player) {
        new ConfigMenu(player, plugin.getGamesManager().getGameSettings());
    }

}
