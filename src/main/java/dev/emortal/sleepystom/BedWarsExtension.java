package dev.emortal.sleepystom;

import dev.emortal.sleepystom.commands.admin.BedWarsAdminCommand;
import dev.emortal.sleepystom.commands.admin.BedWarsDebugCommand;
import dev.emortal.sleepystom.config.MapManager;
import dev.emortal.sleepystom.game.editor.EditorManager;
import dev.emortal.sleepystom.game.GameManager;
import dev.emortal.sleepystom.model.config.map.BedWarsMap;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.extensions.Extension;

import java.io.FileNotFoundException;

public class BedWarsExtension extends Extension {

    // config classes
    private MapManager mapManager;

    private EditorManager editorManager;
    private GameManager gameManager;

    @Override
    public void initialize() {
        this.mapManager = new MapManager(this);

        this.gameManager = new GameManager(this);
        this.editorManager = new EditorManager(this);

        this.registerCommands(
            new BedWarsAdminCommand(this),
            new BedWarsDebugCommand(this)
        );

        try {
            this.gameManager.createGame(this.mapManager.getMaps().values().toArray(new BedWarsMap[]{})[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands(Command... commands) {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        for (Command command : commands) {
            commandManager.register(command);
        }
    }

    public MapManager getMapManager() {
        return this.mapManager;
    }

    public EditorManager getEditorManager() {
        return this.editorManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    @Override
    public void terminate() {

    }
}
