package me.aleiv.core.paper;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.commands.ConfigCommand;
import me.aleiv.core.paper.commands.GameCommand;
import me.aleiv.core.paper.commands.RoleCommand;
import me.aleiv.core.paper.commands.WorldCommand;
import me.aleiv.core.paper.gamesManager.GamesManager;
import me.aleiv.core.paper.gamesManager.PlayerRole;
import me.aleiv.core.paper.utilities.NegativeSpaces;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import us.jcedeno.libs.rapidinv.RapidInvManager;

import java.util.Arrays;

@SpigotPlugin
public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private @Getter GamesManager gamesManager;
    private @Getter PaperCommandManager commandManager;
    private @Getter static MiniMessage miniMessage = MiniMessage.get();

    @Override
    public void onEnable() {
        instance = this;

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
    }

    @Override
    public void onDisable() {
        this.gamesManager.getGameSettings().save();
    }

    public void unregisterListener(Listener listener) {
        if (!this.isListenerRegistered(listener)) return;
        HandlerList.unregisterAll(listener);
    }

    public void registerListener(Listener listener) {
        if (this.isListenerRegistered(listener)) return;
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

    public boolean isListenerRegistered(Listener listener) {
        return HandlerList.getRegisteredListeners(this).stream().anyMatch(l -> l.getListener().getClass().equals(listener.getClass()));
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