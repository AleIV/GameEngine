package me.aleiv.core.paper.games.beast.config;

import lombok.SneakyThrows;
import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;

import java.util.ArrayList;
import java.util.List;

public class BeastConfig extends BaseConfig {

    private List<BeastMapConfig> maps;

    public BeastConfig(String[] maps) {
        super("beastgame");

        this.add(
                ConfigParameter.create("map", this.getActiveMap())
        );

        this.maps = new ArrayList<>();
        for (String map : maps) {
            this.maps.add(new BeastMapConfig(map));
        }
    }

    public String getActiveMap() {
        return this.getString("map", "it");
    }

    public BeastMapConfig getMap(String mapName) {
        for (BeastMapConfig map : this.maps) {
            if (map.getName().equals(mapName)) {
                return map;
            }
        }
        return null;
    }

    public BeastMapConfig getMap() {
        return this.getMap(this.getActiveMap());
    }
}
