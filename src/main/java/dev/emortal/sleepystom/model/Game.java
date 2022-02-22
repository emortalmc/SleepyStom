package dev.emortal.sleepystom.model;

import com.google.common.collect.Sets;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Game {
    private final @NotNull Set<Player> players = Sets.newConcurrentHashSet();
    private final @NotNull Set<Player> spectators = Sets.newConcurrentHashSet();
    private final @NotNull Instance instance;
    private final @NotNull BedWarsMap map;

    private @NotNull Status status = Status.LOBBY;

    public Game(@NotNull Instance instance, @NotNull BedWarsMap map) {
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

    @Override
    public String toString() {
        return "Game{" +
            "players=" + this.players +
            ", spectators=" + this.spectators +
            ", instance=" + this.instance +
            ", map=" + this.map +
            ", status=" + this.status +
            '}';
    }

    public enum Status {
        LOBBY,
        IN_PROGRESS,
        FINISHED
    }
}
