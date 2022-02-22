package dev.emortal.sleepystom.model.config.map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.emortal.sleepystom.model.EditingInfo;
import dev.emortal.sleepystom.model.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.StreamSupport;

public class BedWarsMap {
    private final @NotNull String name;
    private final @NotNull Path instancePath;
    private final @NotNull List<MapTeam> teams;
    private final @NotNull EditingInfo editingInfo = new EditingInfo();
    private int minTeamSize = -1;
    private int maxTeamSize = -1;

    public BedWarsMap(@NotNull String name, @NotNull List<MapTeam> teams, int minTeamSize, int maxTeamSize) {
        this.name = name;
        this.instancePath = Path.of("").resolve(this.name);
        this.teams = teams;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }

    public BedWarsMap(@NotNull String name, @NotNull List<MapTeam> teams) {
        this.name = name;
        this.instancePath = Path.of("").resolve(this.name);
        this.teams = teams;
    }

    public static BedWarsMap fromJson(JsonObject json) {
        String name = json.get("name").getAsString();
        int minTeamSize = json.get("minTeamSize").getAsInt();
        int maxTeamSize = json.get("maxTeamSize").getAsInt();
        List<MapTeam> teams = StreamSupport.stream(json.get("teams").getAsJsonArray().spliterator(), false)
            .map(JsonElement::getAsJsonObject)
            .map(MapTeam::fromJson)
            .toList();

        return new BedWarsMap(name, teams, minTeamSize, maxTeamSize);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull List<MapTeam> getTeams() {
        return this.teams;
    }

    public @NotNull EditingInfo getEditingInfo() {
        return this.editingInfo;
    }

    public int getMinTeamSize() {
        return this.minTeamSize;
    }

    public void setMinTeamSize(int minTeamSize) {
        this.minTeamSize = minTeamSize;
    }

    public int getMaxTeamSize() {
        return this.maxTeamSize;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public boolean hasMapFile() {
        return Files.exists(this.instancePath);
    }

    public Instance createInstance() throws FileNotFoundException {
        if (!this.hasMapFile())
            throw new FileNotFoundException("Could not find instance folder for " + this.name);

        return MinecraftServer.getInstanceManager().createInstanceContainer(new AnvilLoader(this.instancePath));
    }
}
