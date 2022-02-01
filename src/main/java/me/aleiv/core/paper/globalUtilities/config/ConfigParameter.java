package me.aleiv.core.paper.globalUtilities.config;

import me.aleiv.core.paper.utilities.ParseUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigParameter {

    public enum ConfigParameterType {
        STRING(String.class, "", false),
        INTEGER(Integer.class, 0, false),
        DOUBLE(Double.class, 0D, false),
        BOOLEAN(Boolean.class, true, false),
        LOCATION(Location.class, null, false),
        LOCATIONLIST(Location.class, new ArrayList<String>(), true);

        private Class<?> clazz;
        private Object defaultValue;
        private boolean list;

        ConfigParameterType(Class<?> clazz, Object defaultValue, boolean list) {
            this.clazz = clazz;
            this.defaultValue = defaultValue;
            this.list = list;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public boolean isList() {
            return list;
        }

        public static ConfigParameterType getType(Object object) {
            ConfigParameterType t = Arrays.stream(ConfigParameterType.values())
                    .filter(type -> type.getClazz().isInstance(object))
                    .findFirst().orElse(null);

            if (t == STRING) {
                String pt = (String) object;
                if (pt.startsWith(ParseUtils.locationStart)) {
                    return pt.contains("||") ? LOCATIONLIST : LOCATION;
                }
            }

            return t;
        }
    }

    private String key;
    private Object value;
    private ConfigParameterType type;

    protected ConfigParameter(String key, Object value, ConfigParameterType type) {
        this.key = key;
        this.type = type;
        this.value = this.getFormattedObject(value);
    }

    public Object get() {
        return value;
    }

    public Integer getAsInt() {
        return (Integer) value;
    }

    public String getAsString() {
        return (String) value;
    }

    public Boolean getAsBoolean() {
        return (Boolean) value;
    }

    public Double getAsDouble() {
        return (Double) value;
    }

    public Location getAsLocation() {
        return ParseUtils.stringToLocation(this.getAsString());
    }

    public List<Location> getAsLocationList() {
        return Arrays.stream(((String) value).split("\\||")).stream().map(ParseUtils::stringToLocation).filter(Objects::nonNull).toList();
    }

    public void addToNumber(double amount) {
        if (this.type == ConfigParameterType.INTEGER) {
            this.set(this.getAsInt() + (int) amount);
        } else if (this.type == ConfigParameterType.DOUBLE) {
            this.set(this.getAsDouble() + amount);
        }
    }

    public void set(Object value) {
        ConfigParameterType type = ConfigParameterType.getType(value);
        if (type == null || type != this.type) {
            throw new IllegalArgumentException("Invalid type");
        }

        this.value = this.getFormattedObject(value);
    }

    public void addLocation(Location loc) {
        if (this.type != ConfigParameterType.LOCATIONLIST) {

        }
    }

    private Object getFormattedObject(Object value) {
        ConfigParameterType type = ConfigParameterType.getType(value);
        if (type == ConfigParameterType.LOCATION) {
            value = ParseUtils.locationToString((Location) value);
        } else if (type == ConfigParameterType.LOCATIONLIST) {
            value = ((List<Location>) value).stream().map(ParseUtils::locationToString).toList();
        }

        return value;
    }

    public ConfigParameterType getType() {
        return type;
    }

    public boolean isType(ConfigParameterType type) {
        return this.type == type;
    }

    public String getKey() {
        return key;
    }

    public static ConfigParameter create(String key, Object value) {
        ConfigParameterType type = ConfigParameterType.getType(value);
        if (type == null) {
            throw new IllegalArgumentException("Invalid type");
        }
        return new ConfigParameter(key, value, type);
    }

    public static ConfigParameter create(String key, ConfigParameterType type) {
        if (type == null) {
            throw new IllegalArgumentException("Invalid type");
        }
        return new ConfigParameter(key, type.getDefaultValue(), type);
    }

}
