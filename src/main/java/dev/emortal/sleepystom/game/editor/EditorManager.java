package dev.emortal.sleepystom.game.editor;

import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.config.MapManager;
import dev.emortal.sleepystom.game.GameManager;
import dev.emortal.sleepystom.model.EditSession;
import dev.emortal.sleepystom.model.config.map.ConfigMap;
import dev.emortal.sleepystom.model.config.map.ConfigGenerator;
import dev.emortal.sleepystom.model.game.GameEnvironment;
import dev.emortal.sleepystom.utils.GeneratorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.block.Block;
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

    private static final ItemStack GENERATOR_ITEM = ItemStack.builder(Material.EMERALD_BLOCK)
        .displayName(Component.text("Create Generator", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
        .build().withTag(EDIT_TAG, (byte) 1);
    private static final ItemStack SAVE_ITEM = ItemStack.builder(Material.LIME_WOOL)
        .displayName(Component.text("Save Instance", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
        .build().withTag(EDIT_TAG, (byte) 1);
    private static final ItemStack EXIT_ITEM = ItemStack.builder(Material.RED_WOOL)
        .displayName(Component.text("Exit Editor", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
        .build().withTag(EDIT_TAG, (byte) 1);

    private final @NotNull Map<Player, EditSession> editUsers = new ConcurrentHashMap<>();
    private final @NotNull GameManager gameManager;
    private final @NotNull MapManager mapManager;
    private final @NotNull EventNode<Event> eventNode;

    public EditorManager(@NotNull BedWarsExtension extension) {
        this.eventNode = extension.getEventNode();
        this.gameManager = extension.getGameManager();
        this.mapManager = extension.getMapManager();

        this.addGlobalListeners();
    }

    public void startEditing(@NotNull Player player, @NotNull ConfigMap map) throws FileNotFoundException {
        GameEnvironment environment = this.gameManager.createEnvironment(map);

        player.setInstance(environment.getInstance());
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlying(true);
        player.setFlying(true);

        EventNode<PlayerEvent> eventNode = EventNode.type("editor-" + player.getUsername(), EventFilter.PLAYER, (playerEvent, player1) -> player1 == player);
        this.eventNode.addChild(eventNode);
        this.editUsers.put(player, new EditSession(environment, player, eventNode));

        this.giveItems(player);
        this.addPlayerListeners(eventNode);
    }

    private void giveItems(Player player) {
        player.getInventory().clear();

        player.getInventory().setItemStack(0, GENERATOR_ITEM);
        player.getInventory().setItemStack(7, SAVE_ITEM);
        player.getInventory().setItemStack(8, EXIT_ITEM);
    }

    private void addGlobalListeners() {
        this.eventNode.addListener(PlayerDisconnectEvent.class, event ->
            this.eventNode.getChildren()
                .stream()
                .filter(eventNode -> eventNode.getName().startsWith("editor-") && eventNode.getName().endsWith(event.getPlayer().getUsername()))
                .forEach(this.eventNode::removeChild));
    }

    private void addPlayerListeners(EventNode<PlayerEvent> eventNode) {
        eventNode
            .addListener(PlayerBlockPlaceEvent.class, event -> {
                Player player = event.getPlayer();
                event.setCancelled(player.getItemInMainHand().hasTag(EDIT_TAG));

                if (event.isCancelled()) {
                    byte heldSlot = player.getHeldSlot();
                    switch (heldSlot) {
                        case 0 -> this.createGenerator(event);
                    }
                }
            })
            .addListener(ItemDropEvent.class, event -> {
                event.setCancelled(event.getItemStack().hasTag(EDIT_TAG));
            })
            .addListener(PlayerUseItemEvent.class, event -> {
                Player player = event.getPlayer();
                byte heldSlot = player.getHeldSlot();
                event.setCancelled(true);

                switch (heldSlot) {
                    case 0 -> player.sendMessage(Component.text("You must place the generator block where you would like to create a generator.")); // generator item
                    case 7 -> {
                        EditSession editSession = this.editUsers.get(player);
                        this.saveMap(editSession.environment().getMap(), player);
                        this.saveInstance(player);
                    } // save item
                    case 8 -> {
                        // todo exit editor
                    }
                }
            });
    }

    private void createGenerator(@NotNull PlayerBlockPlaceEvent event) {
        Player player = event.getPlayer();

        Point location = event.getBlockPosition();
        Point locationBelow = location.sub(0, 1, 0);
        Block blockBelow = player.getInstance().getBlock(locationBelow);

        Material suggestedMaterial = blockBelow == Block.AIR ? Material.AIR : GeneratorUtils.suggestMaterial(Material.fromNamespaceId(blockBelow.namespace()));
        ConfigGenerator generator = new ConfigGenerator(Pos.fromPoint(locationBelow), suggestedMaterial);

        EditSession editSession = this.editUsers.get(player);
        EditGeneratorInventory editor = new EditGeneratorInventory(editSession.eventNode(), generator);
        player.openInventory(editor);

        editSession.environment().getMap().getGenerators().add(generator);
    }

    private void saveMap(@NotNull ConfigMap map, @NotNull Player player) {
        Instant startTime = Instant.now();
        player.sendMessage(Component.text("Saving map config to storage..."));
        this.mapManager.saveMap(map);
        player.sendMessage("Saved map config to storage in " + Instant.now().minusMillis(startTime.toEpochMilli()).toEpochMilli() + "ms");
    }

    private void saveInstance(@NotNull Player player) {
        Instant startTime = Instant.now();
        player.sendMessage(Component.text("Saving instance to storage..."));
        player.getInstance().saveChunksToStorage().thenAccept(unused ->
            player.sendMessage("Saved instance to storage in " + Instant.now().minusMillis(startTime.toEpochMilli()).toEpochMilli() + "ms")
        );
    }
}
