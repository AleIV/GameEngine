package me.aleiv.core.paper.games.beast.config;

import lombok.NonNull;
import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;
import org.bukkit.Location;

public class BeastMapConfig extends BaseConfig {

    enum keys {
        name("name"),
        lobbyloc("lobbyloc"),
        beastloc("beastloc"),
        playerloc("playerloc");

        private String key;

        keys(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    public BeastMapConfig(@NonNull String mapName) {
        super("beastgame", mapName);

        this.add(
                ConfigParameter.create(keys.name.getKey(), this.getString(keys.name.getKey(), mapName)),
        //        ConfigParameter.create(keys.lobbyloc.getKey(), this.getString(keys.lobbyloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
        //        ConfigParameter.create(keys.beastloc.getKey(), this.getString(keys.beastloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
                ConfigParameter.create(keys.playerloc.getKey(), this.getString(keys.playerloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION)
        );
    }

    public String getName() {
        return this.getString(keys.name.getKey());
    }

    /*public Location getLobbyLoc() {
        return this.getLoc(keys.lobbyloc);
    }

    public Location getBeastLoc() {
        return this.getLoc(keys.beastloc);
    }*/

    public Location getPlayerLoc() {
        return this.getLoc(keys.playerloc);
    }

    private Location getLoc(keys key) {
        ConfigParameter param = this.getConfigParameter(key.getKey());
        return param == null ? null : param.getAsLocation();
    }

}
