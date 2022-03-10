package me.aleiv.gameengine;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.gameengine.commands.ConfigCommand;
import me.aleiv.gameengine.commands.GameCommand;
import me.aleiv.gameengine.commands.RoleCommand;
import me.aleiv.gameengine.commands.WorldCommand;
import me.aleiv.gameengine.gamesManager.GamesManager;
import me.aleiv.gameengine.gamesManager.PlayerRole;
import me.aleiv.gameengine.listener.GamemodeHiderListener;
import me.aleiv.gameengine.listener.PlayerListener;
import me.aleiv.gameengine.listener.WorldListener;
import me.aleiv.gameengine.utilities.NegativeSpaces;
import me.aleiv.gameengine.utilities.TCT.BukkitTCT;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import us.jcedeno.libs.rapidinv.RapidInvManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpigotPlugin
public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private @Getter GamesManager gamesManager;
    private @Getter PaperCommandManager commandManager;
    private @Getter static MiniMessage miniMessage = MiniMessage.get();
    private List<Listener> registeredListeners;

    @Override
    public void onEnable() {
        instance = this;

        this.registeredListeners = new ArrayList<>();

        RapidInvManager.register(this);
        BukkitTCT.registerPlugin(this);
        NegativeSpaces.registerCodes();

        gamesManager = new GamesManager(this);

        commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerAsyncCompletion("worlds", (ctx) -> Bukkit.getWorlds().stream().map(World::getName).toList());
        commandManager.getCommandCompletions().registerStaticCompletion("roles", Arrays.stream(PlayerRole.values()).map(PlayerRole::getName).toList());

        commandManager.registerCommand(new ConfigCommand(this));
        commandManager.registerCommand(new RoleCommand(this));
        commandManager.registerCommand(new WorldCommand(this));
        commandManager.registerCommand(new GameCommand(this));

        registerListener(new PlayerListener(this));
        registerListener(new WorldListener(this));
        registerListener(new GamemodeHiderListener(this));
    }

    @Override
    public void onDisable() {
        if (this.gamesManager.isGameLoaded()) {
            this.gamesManager.stopGame(true);
        }
        this.gamesManager.getGameSettings().save();
    }

    public void unregisterListener(Listener listener) {
        if (!this.isListenerRegistered(listener)) return;
        HandlerList.unregisterAll(listener);
        this.registeredListeners.remove(listener);
    }

    public void registerListener(Listener listener) {
        if (this.isListenerRegistered(listener)) return;
        Bukkit.getPluginManager().registerEvents(listener, this);
        this.registeredListeners.add(listener);
    }

    public boolean isListenerRegistered(Listener listener) {
        return this.registeredListeners.contains(listener);
    }

    public void broadcast(final String message) {
        if (message == null || message.isEmpty()) return;
        String finalMessage = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalMessage));
    }

    public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        String finalTitle = ChatColor.translateAlternateColorCodes('&', title == null ? ChatColor.GRAY + " " : title);
        String finalSubtitle = ChatColor.translateAlternateColorCodes('&', subtitle == null ? ChatColor.GRAY + " " : subtitle);

        Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(finalTitle, finalSubtitle, fadeIn, stay, fadeOut));
    }

}