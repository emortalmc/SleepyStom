package dev.emortal.sleepystom.model.config.map;

import com.google.gson.JsonObject;
import dev.emortal.sleepystom.model.game.live.LiveGenerator;
import dev.emortal.sleepystom.utils.JsonUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class MapGenerator {
    private final @NotNull Pos pos;
    private @NotNull Material material;
    private @NotNull ItemStack itemStack;
    private @NotNull Duration delay;

    public MapGenerator(@NotNull Pos pos, @NotNull Material material, @NotNull Duration delay) {
        this.pos = pos;
        this.material = material;
        this.itemStack = ItemStack.of(this.material);
        this.delay = delay;
    }

    public MapGenerator(@NotNull Pos pos, @Nullable Material material) {
        this(pos, material == null ? Material.AIR : material, Duration.ofMinutes(1));
    }

    public static @NotNull MapGenerator fromJson(@NotNull JsonObject json) {
        Pos pos = JsonUtils.jsonToPos(json.get("pos").getAsJsonObject());
        Material material = Material.fromNamespaceId(json.get("material").getAsString());
        Duration delay = Duration.ofMillis(json.get("delay").getAsLong());
        return new MapGenerator(pos, material, delay);
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
