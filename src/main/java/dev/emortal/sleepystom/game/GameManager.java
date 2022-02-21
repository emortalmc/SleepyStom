package dev.emortal.sleepystom.game;

import com.google.common.collect.Sets;
import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.config.MapManager;
import dev.emortal.sleepystom.model.Game;
import dev.emortal.sleepystom.model.config.map.ConfigMap;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    private static final InstanceManager INSTANCE_MANAGER = MinecraftServer.getInstanceManager();
    private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

    private final @NotNull Set<Game> games = Sets.newConcurrentHashSet();
    private final @NotNull MapManager mapManager;

    public GameManager(@NotNull BedWarsExtension extension) {
        this.mapManager = extension.getMapManager();
    }

    public @NotNull Game createGame() throws FileNotFoundException {
        Collection<ConfigMap> maps = this.mapManager.getMaps().values();
        ConfigMap map = maps.stream().skip(ThreadLocalRandom.current().nextInt(maps.size())).findFirst().orElseThrow(() -> new FileNotFoundException("Could not get random map"));
        return this.createGame(map);
    }

    public @NotNull Game createGame(@NotNull ConfigMap map) throws FileNotFoundException {
        Path worldPath = Path.of("").resolve(map.name());
        if (Files.notExists(worldPath))
            throw new FileNotFoundException("Could not find map file for " + map.name());

        Instance instance = INSTANCE_MANAGER.createInstanceContainer(new AnvilLoader(worldPath));
        Game game = new Game(instance, map);

        this.games.add(game);
        return game;
    }

    public @NotNull Set<Game> getGames() {
        return this.games;
    }
}
