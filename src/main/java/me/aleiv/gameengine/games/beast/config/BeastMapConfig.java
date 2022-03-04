package me.aleiv.gameengine.games.beast.config;

import lombok.NonNull;
import me.aleiv.gameengine.globalUtilities.config.BaseConfig;
import me.aleiv.gameengine.globalUtilities.config.ConfigParameter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BeastMapConfig extends BaseConfig {

    enum keys {
        name("name"),
        lobbyloc("lobbyloc"),
        beastloc("beastloc"),
        barrotesloc("barrotesloc"),
        cinematicloc("cinematicloc"),
        playerloc("playerloc");

        private String key;

        keys(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    private final List<Block> barrotes;

    public BeastMapConfig(@NonNull String mapName) {
        super("beastgame", mapName);

        this.barrotes = new ArrayList<>();
        this.add(
                ConfigParameter.create(keys.name.getKey(), this.getString(keys.name.getKey(), mapName)),
                ConfigParameter.create(keys.beastloc.getKey(), this.getString(keys.beastloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
                ConfigParameter.create(keys.barrotesloc.getKey(), this.getString(keys.barrotesloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
                ConfigParameter.create(keys.cinematicloc.getKey(), this.getString(keys.cinematicloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
                ConfigParameter.create(keys.playerloc.getKey(), this.getString(keys.playerloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION)
        );
    }

    public String getName() {
        return this.getString(keys.name.getKey());
    }

    public Location getBeastLoc() {
        return this.getLoc(keys.beastloc);
    }

    public Location getPlayerLoc() {
        return this.getLoc(keys.playerloc);
    }

    public Location getBarrotesLoc() {
        return this.getLoc(keys.barrotesloc);
    }

    public Location getCinematicLoc() {
        return this.getLoc(keys.cinematicloc);
    }

    private Location getLoc(keys key) {
        ConfigParameter param = this.getConfigParameter(key.getKey());
        return param == null ? null : param.getAsLocation();
    }

    public List<Block> getBarrotes() {
        return this.barrotes;
    }

}
