package me.aleiv.core.paper.gamesManager;

import lombok.Getter;
import lombok.Setter;
import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;

import java.util.ArrayList;
import java.util.List;

public class GameSettings extends BaseConfig {

    enum keys {
        engineGameMode("engineGameMode"),
        autoStart("autoStart"),
        minStartPlayers("minStartPlayers"),
        gameDuration("gameDuration"),;

        private String key;

        keys(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }
    
    public GameSettings() throws Exception {
        super("gameconfig.json");

        this.add(
                ConfigParameter.create(keys.engineGameMode.getKey(), this.getString(keys.engineGameMode.getKey(), "NONE")),
                ConfigParameter.create(keys.autoStart.getKey(), this.getBoolean(keys.autoStart.getKey(), false)),
                ConfigParameter.create(keys.minStartPlayers.getKey(), this.getInteger(keys.minStartPlayers.getKey(), 4)),
                ConfigParameter.create(keys.gameDuration.getKey(), this.getInteger(keys.gameDuration.getKey(), 5*60))
        );
    }

    public EngineGameMode getEngineGameMode() {
        return EngineGameMode.getFromName(this.getString(keys.engineGameMode.getKey(), "NONE"));
    }

    public void setEngineGameMode(EngineGameMode engineGameMode) {
        this.set(keys.engineGameMode.getKey(), engineGameMode.getGameName());
    }

    public enum EngineGameMode {
        NONE("NONE"), TOWERS("TOWERS"), NEXUS("NEXUS"), UHC("UHC"), BINGO("BINGO"), BEAST("BEAST"),;

        private String gameName;

        EngineGameMode(String gameName) {
            this.gameName = gameName;
        }

        public String getGameName() {
            return gameName;
        }

        public static EngineGameMode getFromName(String name) {
            for (EngineGameMode mode : EngineGameMode.values()) {
                if (mode.getGameName().equals(name)) {
                    return mode;
                }
            }
            return null;
        }
    }

}
