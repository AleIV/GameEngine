package me.aleiv.core.paper.globalUtilities.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.NonNull;
import me.aleiv.core.paper.utilities.JsonConfig;
import me.aleiv.core.paper.utilities.ParseUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseConfig {

    private final JsonConfig jsonConfig;
    private final JsonObject config;
    private final String name;
    private final List<ConfigParameter> configParameters;

    private boolean subConfig;

    public BaseConfig(@NonNull String configName) throws Exception {
        this(configName, "");
    }

    public BaseConfig(@NonNull String configName, @NonNull String jsonPath) throws Exception {
        this.name = configName;
        this.jsonConfig = new JsonConfig(configName.endsWith(".json") ? configName : configName + ".json");
        this.subConfig = !jsonPath.equals("");
        this.config = this.subConfig ? this.jsonConfig.getJsonObject().getAsJsonObject(jsonPath) : this.jsonConfig.getJsonObject();
        this.configParameters = new ArrayList<>();

        // TODO: Get values from keys from configPart
    }

    public boolean isSubConfig() {
        return this.subConfig;
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
                case BOOLEAN -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsBoolean()));
                case DOUBLE -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsDouble()));
                case INTEGER -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsInt()));
                case STRING, LOCATION -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsString()));
                case LOCATIONLIST -> {
                    JsonArray list = new JsonArray();
                    param.getAsLocationList().stream().map(ParseUtils::locationToString).toList().forEach(list::add);

                    this.config.add(param.getKey(), list);
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
        return this.config.has(key);
    }

    public Boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        JsonElement element = this.config.get(key);
        return element == null ? defaultValue : element.getAsBoolean();
    }

    public Double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }

    public Double getDouble(String key, Double defaultValue) {
        JsonElement element = this.config.get(key);
        return element == null ? defaultValue : element.getAsDouble();
    }

    public Integer getInteger(String key) {
        return this.getInteger(key, 0);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        JsonElement element = this.config.get(key);
        return element == null ? defaultValue : element.getAsInt();
    }

    public String getString(String key) {
        return this.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        JsonElement element = this.config.get(key);
        return element == null ? defaultValue : element.getAsString();
    }

    public boolean hasKey(String key) {
        return this.configParameters.stream().anyMatch(param -> param.getKey().equalsIgnoreCase(key));
    }

    public void set(String key, Object value) {
        if (this.hasKey(key)) {
            this.configParameters.stream().filter(param -> param.getKey().equalsIgnoreCase(key)).findFirst().ifPresent(param -> param.set(value));
        } else {
            this.configParameters.add(ConfigParameter.create(key, value));
        }
        this.save();
    }

    public void add(ConfigParameter... parameters) {
        Arrays.stream(parameters).forEach(param -> this.set(param.getKey(), param.get()));
        this.save();
    }

    public List<ConfigParameter> getConfigParameters() {
        return this.configParameters;
    }

}
