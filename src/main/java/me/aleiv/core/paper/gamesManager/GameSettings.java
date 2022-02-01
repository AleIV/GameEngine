package me.aleiv.core.paper.gamesManager;

import lombok.Getter;
import lombok.Setter;
import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;

import java.util.ArrayList;
import java.util.List;

public class GameSettings extends BaseConfig {
    
    @Getter @Setter EngineGameMode engineGameMode;
    @Getter @Setter boolean autoStart;
    @Getter @Setter int minStartPlayers;

    public GameSettings() throws Exception {
        super("gameconfig.json");

        this.engineGameMode = EngineGameMode.getFromName(this.getString("engineGameMode", "NONE"));
        this.autoStart = this.getBoolean("autoStart", false);
        this.minStartPlayers = this.getInteger("minStartPlayers", 4);

        this.save();
    }

    @Override
    public List<ConfigParameter> getConfigParameters() {
        List<ConfigParameter> parameters = new ArrayList<>();

        parameters.add(ConfigParameter.create("engineGameMode", this.engineGameMode.getGameName()));
        parameters.add(ConfigParameter.create("autoStart", this.autoStart));
        parameters.add(ConfigParameter.create("minStartPlayers", this.minStartPlayers));

        return parameters;
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
