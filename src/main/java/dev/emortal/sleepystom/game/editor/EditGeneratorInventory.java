package dev.emortal.sleepystom.game.editor;

import dev.emortal.sleepystom.model.config.map.ConfigGenerator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class EditGeneratorInventory extends Inventory {
    private final @NotNull ConfigGenerator generator;
    private final @NotNull EventNode<PlayerEvent> eventNode;

    public EditGeneratorInventory(EventNode<PlayerEvent> eventNode, @NotNull ConfigGenerator generator) {
        super(InventoryType.CHEST_1_ROW, Component.text("Create Generator"));
        this.generator = generator;
        this.eventNode = eventNode
            .addChild(EventNode.value("edit-generator", EventFilter.PLAYER, player -> true));

        Material currentMaterial = this.generator.getMaterial();
        String currentMaterialName = currentMaterial == Material.AIR ? "None" : currentMaterial.namespace().value();
        this.setItemStack(0,
            ItemStack.builder(currentMaterial == Material.AIR ? Material.BEDROCK : currentMaterial)
                .displayName(Component.text("Generated Material", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .lore(
                    Component.empty(),
                    Component.text("Current Material: " + currentMaterialName, NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .build()
        );
    }

    public void show(@NotNull Player player) {
        player.openInventory(this);
    }

    private void startListeners() {

    }
}
