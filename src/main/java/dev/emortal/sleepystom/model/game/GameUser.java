package dev.emortal.sleepystom.model.game;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameUser {
    private final @NotNull Player player;

    public GameUser(@NotNull Player player) {
        this.player = player;
    }

    public @NotNull Player getPlayer() {
        return this.player;
    }
}
