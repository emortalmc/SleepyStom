package dev.emortal.sleepystom.commands.admin;

import dev.emortal.sleepystom.BedWarsExtension;
import dev.emortal.sleepystom.config.MapManager;
import dev.emortal.sleepystom.game.GameManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import org.jetbrains.annotations.NotNull;

public class BedWarsDebugCommand extends Command {
    private final @NotNull MapManager mapManager;
    private final @NotNull GameManager gameManager;

    public BedWarsDebugCommand(@NotNull BedWarsExtension extension) {
        super("bedwarsdebug", "bwdebug", "bwd");
        this.mapManager = extension.getMapManager();
        this.gameManager = extension.getGameManager();

        // todo permissions
        ArgumentLiteral getArgument = new ArgumentLiteral("get");
        ArgumentWord availableGetsArgument = new ArgumentWord("type").from("map", "game");

        this.addSyntax(this::executeGetCommand, getArgument, availableGetsArgument);
    }

    private void executeGetCommand(@NotNull CommandSender sender, @NotNull CommandContext context) {
        String target = context.get("type");
        switch (target) {
            case "map" -> sender.sendMessage(this.mapManager.getMaps().toString());
            case "game" -> sender.sendMessage(this.gameManager.getGames().toString());
        }
    }
}
