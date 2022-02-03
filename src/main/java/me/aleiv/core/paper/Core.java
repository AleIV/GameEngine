package me.aleiv.core.paper;

import me.aleiv.core.paper.commands.ConfigCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.gamesManager.GamesManager;
import me.aleiv.core.paper.utilities.NegativeSpaces;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import net.kyori.adventure.text.minimessage.MiniMessage;
import us.jcedeno.libs.rapidinv.RapidInvManager;

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
        commandManager.registerCommand(new ConfigCommand(this));
        

    }

    @Override
    public void onDisable() {
        this.gamesManager.getGameSettings().save();
    }

    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

}