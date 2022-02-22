package dev.emortal.sleepystom.model.config.map;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class MapGenerator {
    private final @NotNull Point pos;
    private @NotNull Material material;
    private @NotNull Duration delay = Duration.ofMinutes(1);

    public MapGenerator(@NotNull Point pos, @Nullable Material material) {
        this.pos = pos;
        this.material = material == null ? Material.AIR : material;
    }

    public MapGenerator(@NotNull Point pos, @NotNull Material material, @NotNull Duration delay) {
        this.pos = pos;
        this.material = material;
        this.delay = delay;
    }

    public @NotNull Point getPos() {
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
