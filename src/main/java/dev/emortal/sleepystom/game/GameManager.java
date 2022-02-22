package dev.emortal.sleepystom.game;

import com.google.common.collect.Sets;
import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.config.MapManager;
import dev.emortal.sleepystom.model.Game;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    private final @NotNull Set<Game> games = Sets.newHashSet();
    private final @NotNull MapManager mapManager;

    public GameManager(@NotNull BedWarsExtension extension) {
        this.mapManager = extension.getMapManager();
    }

    public @NotNull Game createGame() throws FileNotFoundException {
        Collection<BedWarsMap> maps = this.mapManager.getMaps().values();
        BedWarsMap map = maps.stream().skip(ThreadLocalRandom.current().nextInt(maps.size())).findFirst().orElseThrow(() -> new FileNotFoundException("Could not get random map"));
        return this.createGame(map);
    }

    public @NotNull Game createGame(@NotNull BedWarsMap map) throws FileNotFoundException {
        Instance instance = map.createInstance();
        Game game = new Game(instance, map);

        this.games.add(game);
        return game;
    }

    public @NotNull Set<Game> getGames() {
        return this.games;
    }
}
