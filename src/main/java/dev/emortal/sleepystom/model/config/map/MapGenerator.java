package dev.emortal.sleepystom.model.config.map;

import com.google.gson.JsonObject;
import dev.emortal.sleepystom.utils.JsonUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class MapGenerator {
    private final @NotNull Vec vec;
    private @NotNull Material material;
    private @NotNull Duration delay = Duration.ofMinutes(1);

    public MapGenerator(@NotNull Vec vec, @Nullable Material material) {
        this.vec = vec;
        this.material = material == null ? Material.AIR : material;
    }

    public MapGenerator(@NotNull Vec vec, @NotNull Material material, @NotNull Duration delay) {
        this.vec = vec;
        this.material = material;
        this.delay = delay;
    }

    public static @NotNull MapGenerator fromJson(@NotNull JsonObject json) {
        Vec vec = JsonUtils.jsonToVec(json.get("vec").getAsJsonObject());
        Material material = Material.fromNamespaceId(json.get("material").getAsString());
        Duration delay = Duration.ofMillis(json.get("delay").getAsLong());
        return new MapGenerator(vec, material, delay);
    }

    public @NotNull Vec getVec() {
        return this.vec;
    }

    public @NotNull Material getMaterial() {
        return this.material;
    }

    public void setMaterial(@NotNull Material material) {
        this.material = material;
    }

    public @NotNull Duration getDelay() {
        return this.delay;
    }

    public void setDelay(@NotNull Duration delay) {
        this.delay = delay;
    }
}
