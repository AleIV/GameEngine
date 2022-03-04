package me.aleiv.gameengine.gamesManager;

import lombok.Getter;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.exceptions.GameStartException;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.games.towers.TowersEngine;
import me.aleiv.gameengine.gamesManager.GameSettings.EngineGameMode;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.GlobalTimer;
import me.aleiv.gameengine.globalUtilities.WorldManager;
import me.aleiv.gameengine.globalUtilities.objects.BaseEngine;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import me.aleiv.gameengine.utilities.ResourcePackManager;
import me.aleiv.gameengine.utilities.SoundUtils;
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
    @Getter private final ResourcePackManager resourcePackManager;

    public GamesManager(Core instance){
        this.instance = instance;
        this.worldManager = new WorldManager(instance);
        this.playerManager = new PlayerManager(instance);
        this.resourcePackManager = new ResourcePackManager(instance);

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

    public void startGame() throws GameStartException {
        if (!this.isGameLoaded()) {
            throw new GameStartException(GameStartException.GameStartExceptionReason.GAME_NOT_LOADED);
        }

        if (this.getCurrentGame().getGameStage() == EngineEnums.GameStage.INGAME || this.getCurrentGame().getGameStage() == EngineEnums.GameStage.POSTGAME) {
            throw new GameStartException(GameStartException.GameStartExceptionReason.GAME_ALREADY_STARTED);
        }

        this.getCurrentGame().setGameStage(EngineEnums.GameStage.INGAME);
        try {
            this.getCurrentGame().startGame();
        } catch (Exception e) {
            e.printStackTrace();
            this.stopGame(true);
            if (e instanceof GameStartException) {
                throw (GameStartException) e;
            }
            throw new GameStartException(GameStartException.GameStartExceptionReason.UNKNOWN_EXCEPTION);
        }
        this.timer.runCountdown(this.getGameSettings().getGameDuration(), () -> this.stopGame(false));
    }

    public void stopGame(boolean force) {
        if (this.getCurrentGame().getGameStage() == EngineEnums.GameStage.POSTGAME) return;

        if (force) {
            this.resetGame();
        } else {
            this.getCurrentGame().setGameStage(EngineEnums.GameStage.POSTGAME);
            this.timer.runCountdown(15, this::resetGame);
            this.getCurrentGame().stopGame();
        }
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
        List<Player> players = this.playerManager.filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).toList();
        switch (this.getCurrentGame().getGameStage()) {
            case LOBBY -> {
                if (!this.timer.isRunning() && this.getGameSettings().getAutoStart() && players.size() >= this.getGameSettings().getMinStartPlayers()) {
                    this.timer.runCountdown(this.getGameSettings().getPreGameCountdown(), () -> {
                        try {
                            this.startGame();
                        } catch (GameStartException e) {
                            e.printStackTrace();
                            if (e.getUnknownException() != null) {
                                e.getUnknownException().printStackTrace();
                            }

                            this.instance.broadcast("&cHa habido un fallo iniciando el juego. Intentandolo de nuevo en breves...");
                            this.getCurrentGame().setGameStage(EngineEnums.GameStage.LOBBY);
                            Bukkit.getScheduler().runTaskLater(this.instance, this::updatePlayerCount, 20);
                        }
                    });
                    this.instance.broadcast("&aSomos suficientes jugadores. Iniciando cuenta atras para iniciar el juego...");
                    this.getCurrentGame().setGameStage(EngineEnums.GameStage.PREGAME);
                }
            }
            case PREGAME -> {
                if (this.timer.isRunning() && players.size() < this.getGameSettings().getMinStartPlayers()) {
                    this.timer.stop();
                    this.getCurrentGame().setGameStage(EngineEnums.GameStage.LOBBY);

                    this.instance.broadcast(ChatColor.RED + "No hay suficientes jugadores para empezar la partida. Esperando a mas jugadores...");
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
