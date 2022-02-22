package dev.emortal.sleepystom.game.editor;

import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EditorManager {
    private static final Tag<Byte> EDIT_TAG = Tag.Byte("editItem");

    private static final ItemStack GENERATOR_ITEM = ItemStack.of(Material.EMERALD)
        .withDisplayName(Component.text("Create Generator", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
        .withTag(EDIT_TAG, (byte) 1);
    private static final ItemStack SAVE_ITEM = ItemStack.of(Material.LIME_WOOL)
        .withDisplayName(Component.text("Save Instance", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
        .withTag(EDIT_TAG, (byte) 1);
    private static final ItemStack EXIT_ITEM = ItemStack.of(Material.RED_WOOL)
        .withDisplayName(Component.text("Exit Editor", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
        .withTag(EDIT_TAG, (byte) 1);

    private final Map<Player, EventNode<PlayerEvent>> editUsers = new ConcurrentHashMap<>();
    private final EventNode<Event> eventNode;

    public EditorManager(@NotNull BedWarsExtension extension) {
        this.eventNode = extension.getEventNode();
    }

    public void startEditing(Player player, @NotNull BedWarsMap map) throws FileNotFoundException {
        Instance instance = map.createInstance();
        map.getEditingInfo().startEditing(player);

        player.setInstance(instance);
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlying(true);
        player.setFlying(true);

        EventNode<PlayerEvent> eventNode = EventNode.type("editor-node", EventFilter.PLAYER, (playerEvent, player1) -> {
            Audiences.all().sendMessage(Component.text("Comparing " + player.hashCode() + " and " + player1.hashCode()));
            return player1 == player;
        });
        this.eventNode.addChild(eventNode);
        this.editUsers.put(player, eventNode);


        this.giveItems(player);
        this.addListeners(eventNode);
    }

    private void giveItems(Player player) {
        player.getInventory().clear();

        player.getInventory().setItemStack(0, GENERATOR_ITEM);
        player.getInventory().setItemStack(7, SAVE_ITEM);
        player.getInventory().setItemStack(8, EXIT_ITEM);
    }

    private void addListeners(EventNode<PlayerEvent> eventNode) {
        eventNode
            .addListener(PlayerBlockPlaceEvent.class, event -> {
                Audiences.all().sendMessage(Component.text("PlayerBlockPlaceEvent " + event.getPlayer().getItemInMainHand().hasTag(EDIT_TAG)));
                event.setCancelled(event.getPlayer().getItemInMainHand().hasTag(EDIT_TAG));
            })
            .addListener(ItemDropEvent.class, event -> {
                Audiences.all().sendMessage(Component.text("ItemDropEvent " + event.getItemStack().hasTag(EDIT_TAG)));
                event.setCancelled(event.getItemStack().hasTag(EDIT_TAG));
            })
            .addListener(PlayerUseItemEvent.class, event -> {
                Audiences.all().sendMessage(Component.text("PlayerUseItemEvent "));
                Player player = event.getPlayer();
                byte heldSlot = player.getHeldSlot();
                event.setCancelled(true);

                switch (heldSlot) {
                    case 0 -> {
                        // todo create generator
                    }
                    case 7 -> {
                        // save item
                        this.saveInstance(player);
                    }
                    case 8 -> {
                        // todo exit editor
                    }
                }
            });
    }

    private void createGenerator(Player player) {

    }

    private void saveInstance(Player player) {
        Instant startTime = Instant.now();
        player.sendMessage(Component.text("Saving world to storage..."));
        player.getInstance().saveChunksToStorage().thenAccept(unused ->
            player.sendMessage("Saved instance to storage in " + Instant.now().minusMillis(startTime.toEpochMilli()).toEpochMilli() + "ms")
        );
    }
}
