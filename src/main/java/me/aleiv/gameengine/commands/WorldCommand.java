package me.aleiv.gameengine.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.aleiv.gameengine.Core;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("world")
@CommandPermission("game.world")
public class WorldCommand extends BaseCommand {

    private final Core plugin;

    public WorldCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Subcommand("list")
    public void onList(CommandSender sender) {
        sender.sendMessage("Worlds: " + String.join(", ", plugin.getGamesManager().getWorldManager().getLoadedWorlds().stream().map(world -> "§a" + world.getName()).toList()));
    }

    @Subcommand("save")
    public void onSave(Player player) {
        player.sendMessage("§eSaving world...");
        player.getLocation().getWorld().save();
        player.sendMessage("§aWorld saved!");
    }

    @Subcommand("tp")
    @CommandCompletion("@worlds @players")
    @Syntax("<world> [player]")
    public void onTeleport(Player player, String world, @Optional String playerName) {
        Player target = player;
        if (playerName != null) {
            Player supposedTarget = Bukkit.getPlayer(playerName);
            if (supposedTarget == null) {
                player.sendMessage("§cPlayer not found!");
                return;
            }
            target = supposedTarget;
        }

        World w = Bukkit.getWorld(world);
        if (w == null) {
            player.sendMessage("§cWorld not found");
            return;
        }
        player.teleport(w.getSpawnLocation());
    }

    @Subcommand("weather")
    @CommandCompletion("rain|storm|clear")
    public void onWeather(Player player, String weather) {
        World world = player.getLocation().getWorld();
        if (weather.equalsIgnoreCase("rain")) {
            world.setStorm(true);
            world.setWeatherDuration(999999);
            player.sendMessage("§aWeather set to rain!");
        } else if (weather.equalsIgnoreCase("storm")) {
            world.setStorm(true);
            player.sendMessage("§aWeather set to storm!");
        } else if (weather.equalsIgnoreCase("clear")) {
            world.setStorm(false);
            player.sendMessage("§aWeather set to clear!");
        }
    }

}
