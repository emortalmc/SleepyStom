package dev.emortal.sleepystom.model.config.map;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class ConfigGenerator {
    private final @NotNull Pos pos;
    private @NotNull Material material;
    private transient @NotNull ItemStack itemStack;
    private @NotNull Duration delay;

    public ConfigGenerator(@NotNull Pos pos, @NotNull Material material, @NotNull Duration delay) {
        this.pos = pos;
        this.material = material;
        this.itemStack = ItemStack.of(this.material);
        this.delay = delay;
    }

    public ConfigGenerator(@NotNull Pos pos, @Nullable Material material) {
        this(pos, material == null ? Material.AIR : material, Duration.ofMinutes(1));
    }

    public @NotNull Pos getPos() {
        return this.pos;
    }

    public @NotNull Material getMaterial() {
        return this.material;
    }

    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setMaterial(@NotNull Material material) {
        this.material = material;
        this.itemStack = ItemStack.of(this.material);
    }

    public @NotNull Duration getDelay() {
        return this.delay;
    }

    public void setDelay(@NotNull Duration delay) {
        this.delay = delay;
    }
}
