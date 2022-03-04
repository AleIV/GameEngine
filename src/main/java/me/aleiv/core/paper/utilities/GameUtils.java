package me.aleiv.core.paper.utilities;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.exceptions.GameStartException;
import me.aleiv.core.paper.globalUtilities.EngineEnums;
import org.bukkit.entity.Player;

public class GameUtils {

    private static Core instance = Core.getInstance();

    public static void formalGameStart(Player player) {
        try {
            instance.getGamesManager().startGame();
            player.sendMessage("§aPartida iniciada!");
        } catch (GameStartException e) {
            switch (e.getReason()) {
                case GAME_ALREADY_STARTED -> player.sendMessage("§cYa hay una partida iniciada!");
                case GAME_NOT_LOADED -> player.sendMessage("§cNo hay ningun juego cargado");
                case NOT_ENOUGTH_PLAYERS -> player.sendMessage("§cNo hay suficientes jugadores para iniciar la partida!");
                case UNKNOWN_EXCEPTION -> {
                    player.sendMessage("§cHa ocurrido un error desconocido. Mas detalles en la consola.");
                    e.getUnknownException().printStackTrace();
                }
            }
        }
    }

    public static void formalGameFinish(Player player) {
        if (!instance.getGamesManager().isGameLoaded()) {
            player.sendMessage("§cThere is no game loaded!");
            return;
        }

        EngineEnums.GameStage gs = instance.getGamesManager().getCurrentGame().getGameStage();

        if (gs != EngineEnums.GameStage.INGAME) {
            player.sendMessage("§cNo hay ninguna partida corriendo!");
            return;
        }

        instance.getGamesManager().stopGame(true);
        player.sendMessage("§aPartida parada!");
    }
}
