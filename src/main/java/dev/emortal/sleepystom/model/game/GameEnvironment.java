package dev.emortal.sleepystom.model.game;

import dev.emortal.sleepystom.model.config.map.ConfigMap;
import dev.emortal.sleepystom.model.game.live.LiveGenerator;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class GameEnvironment {
    private final @NotNull Instance instance;
    private final @NotNull ConfigMap map;
    private final @NotNull Set<LiveGenerator> generators = new HashSet<>();

    public GameEnvironment(@NotNull Instance instance, @NotNull ConfigMap map) {
        this.instance = instance;
        this.map = map;
    }

    public @NotNull Instance getInstance() {
        return this.instance;
    }

    public @NotNull ConfigMap getMap() {
        return this.map;
    }

    public @NotNull Set<LiveGenerator> getGenerators() {
        return this.generators;
    }
}
