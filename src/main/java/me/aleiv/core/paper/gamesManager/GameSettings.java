package me.aleiv.core.paper.gamesManager;

import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;

public class GameSettings extends BaseConfig {

    enum keys {
        engineGameMode("engineGameMode"),
        autoStart("autoStart"),
        minStartPlayers("minStartPlayers"),
        maxPlayers("maxPlayers"),
        startCountdown("startCountdown"),
        gameDuration("gameDuration"),
        kickOnDeath("kickOnDeath"),;

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
                ConfigParameter.create(keys.autoStart.getKey(), this.getMinStartPlayers()),
                ConfigParameter.create(keys.minStartPlayers.getKey(), this.getMinStartPlayers()),
                ConfigParameter.create(keys.maxPlayers.getKey(), this.getMaxPlayers()),
                ConfigParameter.create(keys.startCountdown.getKey(), this.getPreGameCountdown()),
                ConfigParameter.create(keys.gameDuration.getKey(), this.getGameDuration()),
                ConfigParameter.create(keys.kickOnDeath.getKey(), this.getKickOnDeath())
        );
    }

    public EngineGameMode getEngineGameMode() {
        return EngineGameMode.getFromName(this.getString(keys.engineGameMode.getKey(), "NONE"));
    }

    public void setEngineGameMode(EngineGameMode engineGameMode) {
        this.set(keys.engineGameMode.getKey(), engineGameMode.getGameName());
    }

    public boolean getAutoStart() {
        return this.getBoolean(keys.autoStart.getKey(), false);
    }

    public int getMinStartPlayers() {
        return this.getInteger(keys.minStartPlayers.getKey(), 4);
    }

    public int getMaxPlayers() {
        return this.getInteger(keys.maxPlayers.getKey(), 24);
    }

    public int getPreGameCountdown() {
        return this.getInteger(keys.startCountdown.getKey(), 30);
    }

    public int getGameDuration() {
        return this.getInteger(keys.gameDuration.getKey(), 5*60);
    }

    public boolean getKickOnDeath() {
        return this.getBoolean(keys.kickOnDeath.getKey(), true);
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
