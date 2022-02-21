package dev.emortal.sleepystom.config;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.model.config.map.ConfigMap;
import dev.emortal.sleepystom.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);

    private final @NotNull Map<String, ConfigMap> maps = Maps.newHashMap();

    public MapManager(BedWarsExtension extension) {
        Path basePath = extension.dataDirectory().resolve("maps");
        try {
            for (Path path : Files.list(basePath).collect(Collectors.toSet())) {
                Optional<JsonElement> optionalElement = JsonUtils.readPath(path);
                if (optionalElement.isEmpty()) {
                    LOGGER.warn("Failed to read map file " + path.getFileName());
                    continue;
                }
                JsonElement element = optionalElement.get();
                JsonObject jsonObject = element.getAsJsonObject();
                ConfigMap configMap = ConfigMap.fromJson(jsonObject);
                this.maps.put(configMap.name(), configMap);
            }
        } catch (IOException ex) {
            if (ex instanceof NoSuchFileException) {
                LOGGER.error("Could not find a maps directory so was unable to load any maps: ", ex);
            } else {
                LOGGER.error("Error whilst loading maps: ", ex);
            }
        }
    }

    public @NotNull Map<String, ConfigMap> getMaps() {
        return this.maps;
    }

    public @NotNull Optional<ConfigMap> getMap(@NotNull String name) {
        return Optional.ofNullable(this.maps.get(name));
    }
}
