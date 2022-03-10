package dev.emortal.sleepystom.model.config.map;

import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

public record MapTeam(@NotNull String name, @NotNull Color color, @NotNull Pos spawn) {
}
