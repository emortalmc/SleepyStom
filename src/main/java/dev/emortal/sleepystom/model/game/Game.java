package dev.emortal.sleepystom.model.game;

import com.google.common.collect.Sets;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Game {
    private final @NotNull Set<Player> players = Sets.newConcurrentHashSet();
    private final @NotNull Set<Player> spectators = Sets.newConcurrentHashSet();
    private final @NotNull GameEnvironment environment;

    private @NotNull Status status = Status.LOBBY;

    public Game(@NotNull GameEnvironment environment) {
        this.environment = environment;
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

    public @NotNull GameEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public String toString() {
        return "Game{" +
            "players=" + this.players +
            ", spectators=" + this.spectators +
            ", environment=" + this.environment +
            ", status=" + this.status +
            '}';
    }

    public enum Status {
        LOBBY,
        IN_PROGRESS,
        FINISHED
    }
}
