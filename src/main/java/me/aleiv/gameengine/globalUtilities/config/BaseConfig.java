package me.aleiv.gameengine.globalUtilities.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.NonNull;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.utilities.JsonConfig;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseConfig {

    private final JsonConfig jsonConfig;
    private final JsonObject config;
    private final String name;
    private final List<ConfigParameter> configParameters;

    private final boolean subConfig;
    private final String subConfigpath;

    public BaseConfig(@NonNull String configName) {
        this(configName, "");
    }

    public BaseConfig(@NonNull String configName, @NonNull String jsonPath) {
        this.name = configName;
        JsonConfig jsonConfig1 = null;
        try {
            jsonConfig1 = JsonConfig.loadConfig(configName.endsWith(".json") ? configName : configName + ".json");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CANNOT CREATE CONFIG. DISABLING PLUGIN...");
            Bukkit.getPluginManager().disablePlugin(Core.getInstance());
        }
        this.jsonConfig = jsonConfig1;
        this.subConfig = !jsonPath.equals("");
        if (this.subConfig) {
            if (!this.jsonConfig.getJsonObject().has(jsonPath)) {
                this.jsonConfig.getJsonObject().add(jsonPath, new JsonObject());
            }
            this.config = this.jsonConfig.getJsonObject().getAsJsonObject(jsonPath);
        } else {
            this.config = this.jsonConfig.getJsonObject();
        }
        this.configParameters = new ArrayList<>();
        this.subConfigpath = jsonPath;

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

    public String getSubconfigPath() {
        return subConfigpath;
    }

    public void save() {
        this.getConfigParameters().forEach(param -> {
            switch (param.getType()) {
                case BOOLEAN -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsBoolean()));
                case DOUBLE -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsDouble()));
                case INTEGER -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsInt()));
                case STRING, LOCATION, LOCATIONLIST -> this.config.add(param.getKey(), new JsonPrimitive(param.getAsString()));
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

    public ConfigParameter getConfigParameter(String key) {
        return this.configParameters.stream().filter(param -> param.getKey().equals(key)).findFirst().orElse(null);
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

    public List<BaseConfig> getSubConfigs() {
        return new ArrayList<>();
    }

}
