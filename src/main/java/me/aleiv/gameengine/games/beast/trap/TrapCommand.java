package me.aleiv.gameengine.games.beast.trap;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.games.beast.config.BeastMapConfig;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.gui.ConfigMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@CommandAlias("trap")
@CommandPermission("game.beast.trap")
public class TrapCommand extends BaseCommand {

    private final BeastEngine engine;

    public TrapCommand(BeastEngine engine) {
        this.engine = engine;
    }

    private BeastMapConfig getConfig(String mapName) {
        return this.engine.getBeastConfig().getMap(mapName);
    }

    @CommandAlias("kill")
    public void onkill(Player player) {
        List<Integer> modeldatas = new ArrayList<>();
        for (TrapAnimation value : TrapAnimation.values()) {
            modeldatas.add(value.getInitCustomModelData());
            modeldatas.add(value.getAnimateCustomModelData());
        }

        AtomicInteger traps = new AtomicInteger();
        player.getWorld().getEntities().stream().filter(e -> e.getType() == EntityType.ARMOR_STAND).map(e -> (ArmorStand) e).forEach(e -> {
            if (e.getEquipment() == null || e.getEquipment().getHelmet() == null || e.getEquipment().getHelmet().getType() != Material.BRICK) return;
            ItemStack b = e.getEquipment().getHelmet();
            if (!b.hasItemMeta()) return;
            if (modeldatas.contains(b.getItemMeta().getCustomModelData())) {
                e.remove();
                traps.getAndIncrement();
            }
        });

        player.sendMessage("Killed " + traps + " traps");
    }

    @CommandAlias("adamage")
    public void addDamage(Player player) {
        if (this.engine.getGameStage() != EngineEnums.GameStage.LOBBY) {
            player.sendMessage("You can only modify traps when a game isn't running!");
            return;
        }
        BeastMapConfig config = this.getConfig(player.getWorld().getName());
        if (config == null) {
            player.sendMessage("You're not in a map!");
            return;
        }
        config.getConfigParameter("damageTraps").addLocation(player.getLocation());
        player.sendMessage("Added trap damage location");
    }

    @CommandAlias("aslowness")
    public void addSlowness(Player player) {
        if (this.engine.getGameStage() != EngineEnums.GameStage.LOBBY) {
            player.sendMessage("You can only modify traps when a game isn't running!");
            return;
        }
        BeastMapConfig config = this.getConfig(player.getWorld().getName());
        if (config == null) {
            player.sendMessage("You're not in a map!");
            return;
        }
        config.getConfigParameter("slownessTraps").addLocation(player.getLocation());
        player.sendMessage("Added trap slowness location");
    }

    @CommandAlias("show")
    public void show(Player player) {
        if (this.engine.getGameStage() != EngineEnums.GameStage.LOBBY) {
            player.sendMessage("You can only show traps when a game isn't running!");
            return;
        }
        BeastMapConfig config = this.getConfig(player.getWorld().getName());
        if (config == null) {
            player.sendMessage("You're not in a map!");
            return;
        }
        List<Location> dtraps = config.getConfigParameter("damageTraps").getAsLocationList();
        List<Location> straps = config.getConfigParameter("slownessTraps").getAsLocationList();

        HashMap<Block, Material> oldMats = new HashMap<>();

        for (Location loc : dtraps) {
            Block block = loc.getBlock();
            oldMats.put(block, block.getType());
            block.setType(Material.REDSTONE_BLOCK);
        }
        for (Location loc : straps) {
            Block block = loc.getBlock();
            oldMats.put(block, block.getType());
            block.setType(Material.EMERALD_ORE);
        }

        player.sendMessage("Showing traps");

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
            oldMats.forEach(Block::setType);
            player.sendMessage("Hiding traps");
        }, 20L*3);
    }

    @CommandAlias("gui")
    public void onGui(Player player) {
        if (this.engine.getGameStage() != EngineEnums.GameStage.LOBBY) {
            player.sendMessage("You can only modify traps when a game isn't running!");
            return;
        }
        BeastMapConfig config = this.getConfig(player.getWorld().getName());
        if (config == null) {
            Bukkit.dispatchCommand(player, "config game");
            return;
        }
        new ConfigMenu(player, config);
    }

}
