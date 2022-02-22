package dev.emortal.sleepystom.game.editor;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public class CreateGeneratorInventory extends Inventory {

    public CreateGeneratorInventory() {
        super(InventoryType.CHEST_1_ROW, Component.text("Create Generator"));
    }
}
