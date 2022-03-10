package dev.emortal.sleepystom.model.game.live;

import dev.emortal.sleepystom.model.config.map.ConfigGenerator;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.timer.Task;
import org.jetbrains.annotations.NotNull;

public class LiveGenerator {
    private final @NotNull ConfigGenerator mapGenerator;
    private @NotNull Hologram hologram;
    private @NotNull Task task;

    public LiveGenerator(@NotNull ConfigGenerator mapGenerator, @NotNull Hologram hologram, @NotNull Task task) {
        this.mapGenerator = mapGenerator;
        this.hologram = hologram;
        this.task = task;
    }

    public @NotNull ConfigGenerator getMapGenerator() {
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
