package dev.emortal.sleepystom.model.game;

import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import dev.emortal.sleepystom.model.game.live.LiveGenerator;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GameEnvironment {
    private final @NotNull Instance instance;
    private final @NotNull BedWarsMap map;
    private final @NotNull Set<LiveGenerator> generators;

    public GameEnvironment(@NotNull Instance instance, @NotNull BedWarsMap map,
                           @NotNull Set<LiveGenerator> generators) {
        this.instance = instance;
        this.map = map;
        this.generators = generators;
    }

    public @NotNull Instance getInstance() {
        return this.instance;
    }

    public @NotNull BedWarsMap getMap() {
        return this.map;
    }

    public @NotNull Set<LiveGenerator> getGenerators() {
        return this.generators;
    }
}
