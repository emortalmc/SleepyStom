package dev.emortal.sleepystom.commands.admin;

import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.config.MapManager;
import dev.emortal.sleepystom.game.editor.EditorManager;
import dev.emortal.sleepystom.game.GameManager;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

public class BedWarsAdminCommand extends Command {
    private final @NotNull MapManager mapManager;
    private final @NotNull EditorManager editorManager;
    private final @NotNull GameManager gameManager;

    public BedWarsAdminCommand(@NotNull BedWarsExtension extension) {
        super("bedwarsadmin", "bwadmin", "bwa");
        this.mapManager = extension.getMapManager();
        this.editorManager = extension.getEditorManager();
        this.gameManager = extension.getGameManager();

        // todo permissions
        ArgumentLiteral createArgument = new ArgumentLiteral("create");
        ArgumentLiteral editArgument = new ArgumentLiteral("edit");

        ArgumentLiteral mapArgument = new ArgumentLiteral("map");
        ArgumentLiteral gameArgument = new ArgumentLiteral("game");

        ArgumentString mapNameArgument = new ArgumentString("mapName");

        // /bwa create map <mapName>
        this.addSyntax(this::executeCreateMapCommand, createArgument, mapArgument, mapNameArgument);
        // /bwa create game <mapName>
        this.addSyntax(this::executeRunGameCommand, createArgument, gameArgument, mapNameArgument);
        // /bwa edit map <mapName>
        this.addSyntax(this::executeEditMapCommand, editArgument, mapArgument, mapNameArgument);
    }

    private void executeCreateMapCommand(@NotNull CommandSender sender, @NotNull CommandContext context) {
        String mapName = context.get("mapName");
        BedWarsMap map = this.getMap(sender, mapName);
        if (map == null)
            return;

        this.mapManager.createMap(new BedWarsMap(mapName));
        sender.sendMessage("Map created! Use /bwa edit map " + mapName + " to edit its properties.");
    }

    private void executeRunGameCommand(@NotNull CommandSender sender, @NotNull CommandContext context) {
        String mapName = context.get("mapName");
        BedWarsMap map = this.getMap(sender, mapName);
        if (map == null)
            return;

        // todo create game
        sender.sendMessage("Run Map: " + mapName);
    }

    private void executeEditMapCommand(@NotNull CommandSender sender, @NotNull CommandContext context) {
        String mapName = context.get("mapName");
        BedWarsMap map = this.getMap(sender, mapName);
        if (map == null)
            return;

        if (sender instanceof Player player) {
            try {
                this.editorManager.startEditing(player, map);
            } catch (FileNotFoundException e) {
                sender.sendMessage("Map " + mapName + " does not have an associated instance file.");
            }
        } else {
            sender.sendMessage("You must be a player to edit a map via the in-game editor.");
        }

        // todo create game
        sender.sendMessage(Component.text("Now editing " + mapName, NamedTextColor.GREEN));
    }

    private BedWarsMap getMap(@NotNull CommandSender sender, @NotNull String name) {
        Optional<BedWarsMap> optionalMap = this.mapManager.getMap(name);
        if (optionalMap.isEmpty()) {
            sender.sendMessage("Could not find a map with the name " + name);
            return null;
        }
        return optionalMap.get();
    }
}
