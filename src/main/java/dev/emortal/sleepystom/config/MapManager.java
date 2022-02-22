package dev.emortal.sleepystom.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);
    private static final Set<String> RESERVED_NAMES = Set.of("all");

    private final @NotNull Map<String, BedWarsMap> maps = new ConcurrentHashMap<>();

    public MapManager(BedWarsExtension extension) {
        Path basePath = extension.getDataDirectory().resolve("maps");
        try {
            for (Path path : Files.list(basePath).collect(Collectors.toSet())) {
                Optional<JsonElement> optionalElement = JsonUtils.readPath(path);
                if (optionalElement.isEmpty()) {
                    LOGGER.warn("Failed to read map file " + path.getFileName());
                    continue;
                }
                JsonElement element = optionalElement.get();
                JsonObject jsonObject = element.getAsJsonObject();
                BedWarsMap map = BedWarsMap.fromJson(jsonObject);

                this.createMap(map);
                this.maps.put(map.getName(), map);
            }
        } catch (IOException ex) {
            if (ex instanceof NoSuchFileException) {
                LOGGER.error("Could not find a maps directory so was unable to load any maps: ", ex);
            } else {
                LOGGER.error("Error whilst loading maps: ", ex);
            }
        }
    }

    public void createMap(@NotNull BedWarsMap map) {
        if (RESERVED_NAMES.contains(map.getName()))
            throw new IllegalArgumentException("Map name " + map.getName() + " is a reserved extension name and cannot be used");

        this.maps.put(map.getName(), map);
    }

    public @NotNull Map<String, BedWarsMap> getMaps() {
        return this.maps;
    }

    public @NotNull Optional<BedWarsMap> getMap(@NotNull String name) {
        return Optional.ofNullable(this.maps.get(name));
    }
}
