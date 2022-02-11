package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.gamesManager.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        instance.getGamesManager().getPlayerManager().setPlayerRole(target.getUniqueId(), playerRole);
        player.sendMessage("§aRole set to " + ChatColor.translateAlternateColorCodes('&', playerRole.getPrefix()));
    }

    @Subcommand("reset")
    @CommandCompletion("@players")
    public void reset(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        PlayerRole defaultRole = instance.getGamesManager().getPlayerManager().getPlayerDefaultRole(target);
        instance.getGamesManager().getPlayerManager().setPlayerRole(target.getUniqueId(), defaultRole);
        player.sendMessage("§aRole reset.");
    }

    @Subcommand("info")
    @CommandCompletion("@players")
    public void info(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        PlayerRole playerRole = instance.getGamesManager().getPlayerManager().getPlayerRole(target);
        player.sendMessage("§aRole: " + playerRole.getName());
    }

}
