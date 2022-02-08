package me.aleiv.core.paper.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonConfig {
    private static Gson gson = new Gson();

    private static List<JsonConfig> loadedConfigs;

    private @Getter @Setter JsonObject jsonObject = new JsonObject();
    private @Getter File file;

    protected JsonConfig(String filename, String path) throws Exception {
        this.file = new File(path + File.separatorChar + filename);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            writeFile(file);
        } else {
            readFile(file);
        }
    }

    public void save() throws Exception {
        writeFile(file);
    }

    public void load() throws Exception {
        readFile(file);
    }

    private void writeFile(File path) throws Exception {
        var writer = new FileWriter(path);

        gson.toJson(jsonObject, writer);
        writer.flush();
        writer.close();

    }

    private void readFile(File path) throws Exception {
        var reader = Files.newBufferedReader(Paths.get(path.getPath()));
        var object = gson.fromJson(reader, JsonObject.class);
        reader.close();

        jsonObject = object;
    }

    public static JsonConfig loadConfig(String filename, String path) throws Exception {
        if (loadedConfigs == null) {
            loadedConfigs = new ArrayList<>();
        }

        JsonConfig cachedConfig = loadedConfigs.stream().filter(config -> config.getFile().getName().equals(filename)).findFirst().orElse(null);
        if (cachedConfig != null) {
            return cachedConfig;
        }
        JsonConfig cfg = new JsonConfig(filename, path);
        loadedConfigs.add(cfg);
        return cfg;
    }

    public static JsonConfig loadConfig(String filename) throws Exception {
        return loadConfig(filename, System.getProperty("user.dir") + File.separatorChar + "secrets");
    }

}