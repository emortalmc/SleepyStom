package dev.emortal.sleepystom.config;

import com.google.gson.JsonElement;
import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import dev.emortal.sleepystom.utils.JsonUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
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
    private final @NotNull Path mapsPath;

    public MapManager(BedWarsExtension extension) {
        this.mapsPath = extension.getDataDirectory().resolve("maps");
        try {
            for (Path path : Files.list(this.mapsPath).collect(Collectors.toSet())) {
                Optional<JsonElement> optionalElement = JsonUtils.readPath(path);
                if (optionalElement.isEmpty()) {
                    LOGGER.warn("Failed to read map file " + path.getFileName());
                    continue;
                }
                JsonElement element = optionalElement.get();
                BedWarsMap map = JsonUtils.GSON.fromJson(element, BedWarsMap.class);

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

    @SneakyThrows
    public void saveMap(@NotNull BedWarsMap map) {
        Path path = this.mapsPath.resolve(map.getName() + ".json");
        if (!Files.exists(path))
            Files.createFile(path);

        JsonUtils.GSON.toJson(map, new FileWriter(path.toFile()));
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
