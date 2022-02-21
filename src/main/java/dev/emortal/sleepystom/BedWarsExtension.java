package dev.emortal.sleepystom;

import dev.emortal.sleepystom.commands.admin.BedWarsDebugCommand;
import dev.emortal.sleepystom.config.MapManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

public class BedWarsExtension extends Extension {
    private MapManager mapManager;

    @Override
    public LoadStatus initialize() {
        this.mapManager = new MapManager(this);

        MinecraftServer.getCommandManager().register(new BedWarsDebugCommand(this));

        return LoadStatus.SUCCESS;
    }

    public MapManager getMapManager() {
        return this.mapManager;
    }

    @Override
    public void terminate() {

    }
}
