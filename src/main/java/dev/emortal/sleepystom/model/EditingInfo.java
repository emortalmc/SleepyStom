package dev.emortal.sleepystom.model;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class EditingInfo {
    private @Nullable Player editor;
    private @Nullable Instant editStartTime;

    public void startEditing(Player editor) throws IllegalStateException {
        if (this.editor != null)
            throw new IllegalStateException("Item is already being edited.");
        this.editor = editor;
        this.editStartTime = Instant.now();
    }

    public void stopEditing() {
        this.editor = null;
    }

    public @Nullable Player getEditor() {
        return this.editor;
    }

    public @Nullable Instant getEditStartTime() {
        return this.editStartTime;
    }
}
