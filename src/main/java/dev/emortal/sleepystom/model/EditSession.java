package dev.emortal.sleepystom.model;

import dev.emortal.sleepystom.model.game.GameEnvironment;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public record EditSession(@NotNull GameEnvironment environment, @NotNull Player player, @NotNull EventNode<PlayerEvent> eventNode) {
}
