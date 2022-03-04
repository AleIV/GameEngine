package me.aleiv.gameengine.globalUtilities;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.worlds.VoidGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class WorldManager implements Listener {

    private final Core plugin;

    private final List<UUID> loadedWorlds;

    public WorldManager(Core plugin) {
        this.plugin = plugin;
        this.loadedWorlds = new ArrayList<>();

        plugin.registerListener(this);
    }

    public void load(String... worldName) {
        for (String name : worldName) {
            if (this.isWorldLoaded(name)) return;

            WorldCreator wc = new WorldCreator(name);
            wc.generateStructures(false);
            wc.generator(new VoidGenerator());
            World world = wc.createWorld();
            if (world != null) {
                world.setAutoSave(false);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                this.loadedWorlds.add(world.getUID());
            }
        }
    }

    public void unloadWorld(boolean save, String... worldName) {
        for (String name : worldName) {
            if (this.isWorldLoaded(name)) return;

            World unknownWorld = Bukkit.getWorld(name);
            if (unknownWorld != null) {
                this.loadedWorlds.remove(unknownWorld.getUID());
                Bukkit.unloadWorld(unknownWorld, save);
            }
        }
    }

    public void resetWorld(String... worldName) {
        this.unloadWorld(false, worldName);
        this.load(worldName);
    }

    public boolean isWorldLoaded(String worldName) {
        World unknownWorld = Bukkit.getWorld(worldName);
        if (unknownWorld != null) {
            return this.isWorldLoaded(unknownWorld.getUID());
        }
        return false;
    }

    public boolean isWorldLoaded(UUID worldUUID) {
        return this.loadedWorlds.contains(worldUUID);
    }

    public List<World> getLoadedWorlds() {
        return this.loadedWorlds.stream().map(Bukkit::getWorld).filter(Objects::nonNull).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldDisable(WorldUnloadEvent e) {
        if (this.isWorldLoaded(e.getWorld().getUID())) {
            e.setCancelled(true);
            this.loadedWorlds.remove(e.getWorld().getUID());
            this.unloadWorld(false, e.getWorld().getName());
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        if (e.getPlugin() == this.plugin) {
            this.loadedWorlds.stream().map(Bukkit::getWorld).filter(Objects::nonNull).toList().forEach(world -> this.unloadWorld(false, world.getName()));
        }
    }

}
