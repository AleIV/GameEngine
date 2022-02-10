package me.aleiv.core.paper.games.beast.config;

import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;

import java.util.ArrayList;
import java.util.List;

public class BeastConfig extends BaseConfig {

    private List<BeastMapConfig> maps;

    public BeastConfig(String[] maps) {
        super("beastgame");

        this.add(
                ConfigParameter.create("map", this.getActiveMap()),
                ConfigParameter.create("beasts", this.getBeastsNumber()),
                ConfigParameter.create("playerGracePeriod", this.getPlayerGracePeriod())
        );

        this.maps = new ArrayList<>();
        for (String map : maps) {
            this.maps.add(new BeastMapConfig(map));
        }
    }

    public String getActiveMap() {
        return this.getString("map", "it");
    }

    public int getBeastsNumber() {
        return this.getInteger("beasts", 1);
    }

    public int getPlayerGracePeriod() {
        return this.getInteger("playerGracePeriod", 20);
    }

    public BeastMapConfig getMap(String mapName) {
        for (BeastMapConfig map : this.maps) {
            if (map.getName().equalsIgnoreCase(mapName)) {
                return map;
            }
        }
        return null;
    }

    public BeastMapConfig getMap() {
        return this.getMap(this.getActiveMap());
    }

    @Override
    public List<BaseConfig> getSubConfigs() {
        return new ArrayList<>(this.maps);
    }
}
