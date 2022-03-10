package dev.emortal.sleepystom.model.config.map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.emortal.sleepystom.model.EditingInfo;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BedWarsMap {
    private final @NotNull String name;
    private final @NotNull Path instancePath;
    private final @NotNull Set<MapTeam> teams;
    private final @NotNull Set<MapGenerator> generators;
    private final @NotNull EditingInfo editingInfo = new EditingInfo();
    private int minTeamSize = -1;
    private int maxTeamSize = -1;

    public BedWarsMap(@NotNull String name, @NotNull Set<MapTeam> teams, @NotNull Set<MapGenerator> generators, int minTeamSize, int maxTeamSize) {
        this.name = name;
        this.generators = generators;
        this.instancePath = Path.of("").resolve(this.name);
        this.teams = teams;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }

    public BedWarsMap(@NotNull String name) {
        this.name = name;
        this.instancePath = Path.of("").resolve(this.name);
        this.generators = new HashSet<>();
        this.teams = new HashSet<>();
    }

    public static @NotNull BedWarsMap fromJson(@NotNull JsonObject json) {
        String name = json.get("name").getAsString();
        int minTeamSize = json.get("minTeamSize").getAsInt();
        int maxTeamSize = json.get("maxTeamSize").getAsInt();
        Set<MapTeam> teams = StreamSupport.stream(json.get("teams").getAsJsonArray().spliterator(), false)
            .map(JsonElement::getAsJsonObject)
            .map(MapTeam::fromJson)
            .collect(Collectors.toSet());
        Set<MapGenerator> generators = StreamSupport.stream(json.get("generators").getAsJsonArray().spliterator(), false)
            .map(JsonElement::getAsJsonObject)
            .map(MapGenerator::fromJson)
            .collect(Collectors.toSet());

        return new BedWarsMap(name, teams, generators, minTeamSize, maxTeamSize);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Set<MapTeam> getTeams() {
        return this.teams;
    }

    public @NotNull Set<MapGenerator> getGenerators() {
        return this.generators;
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
