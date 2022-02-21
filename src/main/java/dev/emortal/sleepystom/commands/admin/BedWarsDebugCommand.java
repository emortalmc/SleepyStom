package dev.emortal.sleepystom.commands.admin;

import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.config.MapManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import org.jetbrains.annotations.NotNull;

public class BedWarsDebugCommand extends Command {
    private final @NotNull MapManager mapManager;

    public BedWarsDebugCommand(@NotNull BedWarsExtension extension) {
        super("bedwarsdebug", "bwdebug");
        this.mapManager = extension.getMapManager();

        // todo permissions
        ArgumentLiteral getArgument = new ArgumentLiteral("get");
        ArgumentWord availableGetsArgument = new ArgumentWord("map");

        this.addSyntax(this::executeGetCommand, getArgument, availableGetsArgument);
    }

    private void executeGetCommand(@NotNull CommandSender sender, @NotNull CommandContext context) {
        sender.sendMessage(this.mapManager.getMaps().toString());
    }
}
