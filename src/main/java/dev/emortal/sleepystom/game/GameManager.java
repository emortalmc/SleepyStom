package dev.emortal.sleepystom.game;

import com.google.common.collect.Sets;
import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.config.MapManager;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import dev.emortal.sleepystom.model.config.map.MapGenerator;
import dev.emortal.sleepystom.model.game.Game;
import dev.emortal.sleepystom.model.game.GameEnvironment;
import dev.emortal.sleepystom.model.game.live.LiveGenerator;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Task;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
        Game game = new Game(this.createEnvironment(map));

        this.games.add(game);
        return game;
    }

    /**
     * Creates the instance and its elements for a map including: generators, holograms, shops, etc...
     *
     * @param map A configured map to create the instance and elements from
     * @return The created instance
     */
    public @NotNull GameEnvironment createEnvironment(@NotNull BedWarsMap map) throws FileNotFoundException {
        Instance instance = map.createInstance();

        Set<LiveGenerator> generators = new HashSet<>();
        for (MapGenerator generator : map.getGenerators()) {
            Hologram hologram = new Hologram(instance, generator.getPos(), Component.text("Temporary Text"));
            Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
                    ItemEntity drop = new ItemEntity(generator.getItemStack());
                    drop.setMergeable(false);
                    drop.setInstance(instance, generator.getPos());
                    drop.spawn();
                }).repeat(generator.getDelay())
                .schedule();
            generators.add(new LiveGenerator(generator, hologram, task));
        }

        return new GameEnvironment(instance, map, Collections.unmodifiableSet(generators));
    }

    public @NotNull Set<Game> getGames() {
        return this.games;
    }
}
