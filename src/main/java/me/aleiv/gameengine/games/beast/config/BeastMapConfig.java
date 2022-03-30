package me.aleiv.gameengine.games.beast.config;

import lombok.NonNull;
import me.aleiv.gameengine.globalUtilities.config.BaseConfig;
import me.aleiv.gameengine.globalUtilities.config.ConfigParameter;
import me.aleiv.gameengine.globalUtilities.objects.Region;
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
        equipmentPos1loc("equipmentPos1loc"),
        equipmentPos2loc("equipmentPos2loc"),
        damageTraps("damageTraps"),
        slownessTraps("slownessTraps"),
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
                ConfigParameter.create(keys.playerloc.getKey(), this.getString(keys.playerloc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
                ConfigParameter.create(keys.equipmentPos1loc.getKey(), this.getString(keys.equipmentPos1loc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
                ConfigParameter.create(keys.equipmentPos2loc.getKey(), this.getString(keys.equipmentPos2loc.getKey()), ConfigParameter.ConfigParameterType.LOCATION),
                ConfigParameter.create(keys.damageTraps.getKey(), this.getString(keys.damageTraps.getKey()), ConfigParameter.ConfigParameterType.LOCATIONLIST),
                ConfigParameter.create(keys.slownessTraps.getKey(), this.getString(keys.slownessTraps.getKey()), ConfigParameter.ConfigParameterType.LOCATIONLIST)
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

    public List<Location> getDamageTraps() {
        return this.getLocList(keys.damageTraps);
    }

    public List<Location> getSlownessTraps() {
        return this.getLocList(keys.slownessTraps);
    }

    private Location getLoc(keys key) {
        ConfigParameter param = this.getConfigParameter(key.getKey());
        return param == null ? null : param.getAsLocation();
    }

    private List<Location> getLocList(keys key) {
        ConfigParameter param = this.getConfigParameter(key.getKey());
        return param == null ? null : param.getAsLocationList();
    }

    public List<Block> getBarrotes() {
        return this.barrotes;
    }

    public Region getEquipmentRegion() {
        return new Region(this.getLoc(keys.equipmentPos1loc), this.getLoc(keys.equipmentPos2loc));
    }

}
