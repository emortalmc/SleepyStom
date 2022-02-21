package dev.emortal.sleepystom.model;

import com.google.common.collect.Sets;
import dev.emortal.sleepystom.model.config.map.ConfigMap;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Game {
    private final @NotNull Set<Player> players = Sets.newConcurrentHashSet();
    private final @NotNull Set<Player> spectators = Sets.newConcurrentHashSet();
    private final @NotNull Instance instance;
    private final @NotNull ConfigMap map;

    private @NotNull Status status = Status.LOBBY;

    public Game(@NotNull Instance instance, @NotNull ConfigMap map) {
        this.instance = instance;
        this.map = map;
    }

    public @NotNull Status getStatus() {
        return this.status;
    }

    public void setStatus(@NotNull Status status) {
        this.status = status;
    }

    public @NotNull Set<Player> getPlayers() {
        return this.players;
    }

    public @NotNull Set<Player> getSpectators() {
        return this.spectators;
    }

    public enum Status {
        LOBBY,
        IN_PROGRESS,
        FINISHED
    }
}
