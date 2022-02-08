package me.aleiv.core.paper.games.beast;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.commands.BeastCMD;
import me.aleiv.core.paper.games.beast.config.BeastConfig;
import me.aleiv.core.paper.games.beast.listeners.BeastGlobalListener;
import me.aleiv.core.paper.games.beast.listeners.BeastInGameListener;
import me.aleiv.core.paper.games.beast.listeners.BeastLobbyListener;
import me.aleiv.core.paper.gamesManager.PlayerRole;
import me.aleiv.core.paper.globalUtilities.EngineEnums;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BeastEngine extends BaseEngine {

    Core instance;

    BeastCMD beastCMD;
    BeastGlobalListener beastGlobalListener;
    BeastInGameListener beastInGameListener;
    BeastLobbyListener beastLobbyListener;
    private @Getter final BeastConfig beastConfig;
    private @Getter final List<Player> beasts;
    public static final String[] MAPS = new String[]{"ghost", "it", "jeison", "puppyplaytime", "slenderman"};

    public BeastEngine(Core instance) {
        super(new BeastConfig(MAPS));
        this.instance = instance;

        this.beastConfig = (BeastConfig) this.getGameConfig();
        this.beasts = new ArrayList<>();

        this.beastCMD = new BeastCMD(instance);
        this.beastGlobalListener = new BeastGlobalListener(instance);
        this.beastInGameListener = new BeastInGameListener(instance);
        this.beastLobbyListener = new BeastLobbyListener(instance);
    }

    @Override
    public void enable(){
        this.instance.getGamesManager().getWorldManager().load(MAPS);

        instance.getCommandManager().registerCommand(beastCMD);
        instance.registerListener(beastGlobalListener);
        instance.registerListener(beastInGameListener);
        instance.registerListener(beastLobbyListener);
    }

    @Override
    public void disable(){
        this.instance.getGamesManager().getWorldManager().unloadWorld(false, MAPS);

        instance.getCommandManager().unregisterCommand(beastCMD);
        instance.unregisterListener(beastGlobalListener);
        instance.unregisterListener(beastInGameListener);
        instance.unregisterListener(beastLobbyListener);

    }

    @Override
    public void startGame() {
        int beastsCount = this.getBeastConfig().getBeastsNumber();
        List<Player> players = this.instance.getGamesManager().getRoleManager().filter(PlayerRole.PLAYER);
        // Get beastsCount players randomly from players list without repeating
        for (int i = 0; i < beastsCount; i++) {
            int random = (int) (Math.random() * players.size());
            Player beast = players.get(random);
            players.remove(random);
            this.beasts.add(beast);
        }
        players.forEach(p -> p.teleport(this.getBeastConfig().getMap().getPlayerLoc()));
        this.beasts.forEach(p -> p.teleport(this.getBeastConfig().getMap().getBeastLoc()));

        Bukkit.getScheduler().runTaskLater(this.instance, () -> {
            if (this.getGameStage() == EngineEnums.GameStage.INGAME) {
                this.beasts.forEach(p -> p.teleport(this.getBeastConfig().getMap().getPlayerLoc()));
            }
        }, this.getBeastConfig().getPlayerGracePeriod() * 20L);

        // TODO: Titles and stuff
    }

    @Override
    public void stopGame() {

    }

    @Override
    public void restartGame() {
        this.instance.getGamesManager().getWorldManager().resetWorld(this.getBeastConfig().getActiveMap());

        this.instance.getGamesManager().getRoleManager().filter(PlayerRole.PLAYER).forEach(p -> {
            p.teleport(this.getBeastConfig().getMap().getLobbyLoc());
            p.getInventory().clear();
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(20);
            p.getActivePotionEffects().forEach(pe -> p.removePotionEffect(pe.getType()));
            p.setGameMode(GameMode.ADVENTURE);
        });
    }



    @Override
    public void joinPlayer(Player player) {
        if (this.getGameStage() == EngineEnums.GameStage.LOBBY) {
            player.teleport(this.getBeastConfig().getMap().getLobbyLoc());
        } else {
            player.kickPlayer("Game is already running!");
        }
    }

    @Override
    public void leavePlayer(Player player) {
        // TODO: Check for beasts. If 0, players win
    }
}
