package me.aleiv.gameengine.games.beast.config;

import me.aleiv.gameengine.globalUtilities.config.BaseConfig;
import me.aleiv.gameengine.globalUtilities.config.ConfigParameter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class BeastConfig extends BaseConfig {

    private List<BeastMapConfig> maps;

    public BeastConfig(String[] maps) {
        super("beastgame");

        this.add(
                ConfigParameter.create("map", this.getActiveMap()),
                ConfigParameter.create(BeastMapConfig.keys.lobbyloc.getKey(), this.getString(BeastMapConfig.keys.lobbyloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
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

    public Location getLobbyLoc() {
        return this.getLoc(BeastMapConfig.keys.lobbyloc);
    }

    /*public Location getBeastLoc() {
        return this.getLoc(BeastMapConfig.keys.beastloc);
    }*/

    @Override
    public List<BaseConfig> getSubConfigs() {
        return new ArrayList<>(this.maps);
    }

    private Location getLoc(BeastMapConfig.keys key) {
        ConfigParameter param = this.getConfigParameter(key.getKey());
        return param == null ? null : param.getAsLocation();
    }
}
