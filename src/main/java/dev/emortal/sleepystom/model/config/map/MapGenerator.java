package dev.emortal.sleepystom.model.config.map;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class MapGenerator {
    private final @NotNull Pos pos;
    private final @NotNull Material material;
    private @NotNull Duration delay = Duration.ofMinutes(1);

    public MapGenerator(@NotNull Pos pos, @NotNull Material material) {
        this.pos = pos;
        this.material = material;
    }

    public MapGenerator(@NotNull Pos pos, @NotNull Material material, @NotNull Duration delay) {
        this.pos = pos;
        this.material = material;
        this.delay = delay;
    }

    public @NotNull Pos getPos() {
        return this.pos;
    }

    public @NotNull Material getMaterial() {
        return this.material;
    }

    public @NotNull Duration getDelay() {
        return this.delay;
    }

    public void setDelay(@NotNull Duration delay) {
        this.delay = delay;
    }
}
