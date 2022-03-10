package dev.emortal.sleepystom.model.game.live;

import dev.emortal.sleepystom.model.config.map.MapGenerator;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.timer.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiveGenerator {
    private final @NotNull MapGenerator mapGenerator;
    private @NotNull Hologram hologram;
    private @NotNull Task task;

    public LiveGenerator(@NotNull MapGenerator mapGenerator, @NotNull Hologram hologram, @NotNull Task task) {
        this.mapGenerator = mapGenerator;
        this.hologram = hologram;
        this.task = task;
    }

    public @NotNull MapGenerator getMapGenerator() {
        return this.mapGenerator;
    }

    public @NotNull Hologram getHologram() {
        return this.hologram;
    }

    public @NotNull Task getTask() {
        return this.task;
    }

    public void setTask(@NotNull Task task) {
        this.task = task;
    }
}
