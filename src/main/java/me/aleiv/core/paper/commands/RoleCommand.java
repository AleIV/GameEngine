package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.gamesManager.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("role")
@CommandPermission("game.role")
public class RoleCommand extends BaseCommand {

    private final Core instance;

    public RoleCommand(Core instance) {
        this.instance = instance;
    }

    @Subcommand("set")
    @CommandCompletion("@players @roles")
    public void set(Player player, String targetName, String role) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        PlayerRole playerRole = PlayerRole.getFromName(role);
        if (playerRole == null) {
            player.sendMessage("§cRole not found.");
            return;
        }

        instance.getGamesManager().getRoleManager().setPlayerRole(target, playerRole);
        player.sendMessage("§aRole set.");
    }

    @Subcommand("reset")
    @CommandCompletion("@players")
    public void reset(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        PlayerRole defaultRole = instance.getGamesManager().getRoleManager().getPlayerDefaultRole(target);
        instance.getGamesManager().getRoleManager().setPlayerRole(target, defaultRole);
        player.sendMessage("§aRole reset.");
    }

}
