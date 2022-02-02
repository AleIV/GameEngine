package me.aleiv.core.paper.globalUtilities.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.aleiv.core.paper.utilities.JsonConfig;
import me.aleiv.core.paper.utilities.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseConfig {

    private final JsonConfig jsonConfig;
    private final String name;

    public BaseConfig(String configName) throws Exception {
        this.name = configName;
        this.jsonConfig = new JsonConfig(configName);
    }

    public JsonConfig getConfig() {
        return this.jsonConfig;
    }

    public String getName() {
        return name;
    }

    public void save() {
        this.getConfigParameters().forEach(param -> {
            switch (param.getType()) {
                case BOOLEAN -> this.jsonConfig.getJsonObject().add(param.getKey(), new JsonPrimitive(param.getAsBoolean()));
                case DOUBLE -> this.jsonConfig.getJsonObject().add(param.getKey(), new JsonPrimitive(param.getAsDouble()));
                case INTEGER -> this.jsonConfig.getJsonObject().add(param.getKey(), new JsonPrimitive(param.getAsInt()));
                case STRING, LOCATION -> this.jsonConfig.getJsonObject().add(param.getKey(), new JsonPrimitive(param.getAsString()));
                case LOCATIONLIST -> {
                    JsonArray list = new JsonArray();
                    param.getAsLocationList().stream().map(ParseUtils::locationToString).toList().forEach(list::add);

                    this.jsonConfig.getJsonObject().add(param.getKey(), list);
                }
            }
        });

        try {
            this.jsonConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean doesKeyExist(String key) {
        return this.jsonConfig.getJsonObject().has(key);
    }

    public Boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        JsonElement element = this.jsonConfig.getJsonObject().get(key);
        return element == null ? defaultValue : element.getAsBoolean();
    }

    public Double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }

    public Double getDouble(String key, Double defaultValue) {
        JsonElement element = this.jsonConfig.getJsonObject().get(key);
        return element == null ? defaultValue : element.getAsDouble();
    }

    public Integer getInteger(String key) {
        return this.getInteger(key, 0);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        JsonElement element = this.jsonConfig.getJsonObject().get(key);
        return element == null ? defaultValue : element.getAsInt();
    }

    public String getString(String key) {
        return this.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        JsonElement element = this.jsonConfig.getJsonObject().get(key);
        return element == null ? defaultValue : element.getAsString();
    }

    public abstract List<ConfigParameter> getConfigParameters();

}
