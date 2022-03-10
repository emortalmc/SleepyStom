package dev.emortal.sleepystom.model.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DroppedItem extends Entity {

    public DroppedItem(ItemStack droppedItem) {
        super(EntityType.ITEM);
    }
}
