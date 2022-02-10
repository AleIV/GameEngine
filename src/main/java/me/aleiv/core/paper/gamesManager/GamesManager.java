package me.aleiv.core.paper.gamesManager;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.BeastEngine;
import me.aleiv.core.paper.games.towers.TowersEngine;
import me.aleiv.core.paper.gamesManager.GameSettings.EngineGameMode;
import me.aleiv.core.paper.globalUtilities.EngineEnums;
import me.aleiv.core.paper.globalUtilities.GlobalTimer;
import me.aleiv.core.paper.globalUtilities.WorldManager;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import me.aleiv.core.paper.utilities.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class GamesManager {
    Core instance;

    GlobalTimer timer;
    @Getter GameSettings gameSettings;
    HashMap<EngineGameMode, BaseEngine> gameEngineList = new HashMap<>();
    @Getter private final WorldManager worldManager;
    @Getter private final PlayerManager playerManager;

    public GamesManager(Core instance){
        this.instance = instance;
        this.worldManager = new WorldManager(instance);
        this.playerManager = new PlayerManager(instance);

        timer = new GlobalTimer(instance);
        timer.runTaskTimerAsynchronously(instance, 0L, 20L);

        try {
            gameSettings = new GameSettings();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Error while loading game settings");
            Bukkit.getPluginManager().disablePlugin(instance);
        }

        gameSettings.setEngineGameMode(EngineGameMode.BEAST);

        Bukkit.getScheduler().runTask(instance, () -> {
            initGameEngines();
            runGame();
        });
    }

    public BaseEngine getCurrentGame(){
        var engineGameMode = gameSettings.getEngineGameMode();
        return gameEngineList.get(engineGameMode);
    }

    private void initGameEngines(){
        gameEngineList.put(EngineGameMode.TOWERS, new TowersEngine(instance));
        gameEngineList.put(EngineGameMode.BEAST, new BeastEngine(instance));
    }

    private void runGame(){
        var engineGameMode = gameSettings.getEngineGameMode();

        if(engineGameMode == EngineGameMode.NONE) return;
        gameEngineList.get(engineGameMode).enable();
    }

    public void startGame() {
        if (this.getCurrentGame().getGameStage() == EngineEnums.GameStage.INGAME) return;

        this.getCurrentGame().setGameStage(EngineEnums.GameStage.INGAME);
        this.getCurrentGame().startGame();
        this.timer.runCountdown(this.getGameSettings().getGameDuration(), this::stopGame);
    }

    public void stopGame() {
        if (this.getCurrentGame().getGameStage() == EngineEnums.GameStage.POSTGAME) return;

        this.getCurrentGame().setGameStage(EngineEnums.GameStage.POSTGAME);
        this.timer.runCountdown(15, this::resetGame);
    }

    private void resetGame() {
        this.getCurrentGame().setGameStage(EngineEnums.GameStage.LOBBY);
        this.timer.stop();
        if (this.isGameLoaded()) {
            this.getCurrentGame().restartGame();
        }

        this.updatePlayerCount();
    }

    public void updatePlayerCount() {
        List<Player> players = this.getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).toList();
        switch (this.getCurrentGame().getGameStage()) {
            case LOBBY -> {
                if (!this.timer.isRunning() && this.getGameSettings().getAutoStart() && players.size() >= this.getGameSettings().getMinStartPlayers()) {
                    this.timer.runCountdown(this.getGameSettings().getPreGameCountdown(), this::startGame);
                    this.getCurrentGame().setGameStage(EngineEnums.GameStage.PREGAME);
                }
            }
            case PREGAME -> {
                if (this.timer.isRunning() && players.size() < this.getGameSettings().getMinStartPlayers()) {
                    this.timer.stop();
                    this.getCurrentGame().setGameStage(EngineEnums.GameStage.LOBBY);

                    Bukkit.broadcast(ChatColor.RED + "No hay suficientes jugadores para empezar la partida. Esperando a mas jugadores...", "");
                    SoundUtils.playSound(Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 0.5f);
                }
            }
        }
    }

    public boolean isGameLoaded() {
        try {
            BaseEngine be = this.getCurrentGame();
            return be != null;
        } catch (Exception e) {
            return false;
        }
    }
    
}
