package dev.emortal.sleepystom.model.config.map;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class ConfigMap {
    private final @NotNull String name;
    private final @NotNull Path instancePath;
    private final @NotNull Set<ConfigTeam> teams;
    private final @NotNull Set<ConfigGenerator> generators;
    private int minTeamSize = -1;
    private int maxTeamSize = -1;

    public ConfigMap(@NotNull String name, @NotNull Set<ConfigTeam> teams, @NotNull Set<ConfigGenerator> generators, int minTeamSize, int maxTeamSize) {
        this.name = name;
        this.generators = generators;
        this.instancePath = Path.of("").resolve(this.name);
        this.teams = teams;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }

    public ConfigMap(@NotNull String name) {
        this.name = name;
        this.instancePath = Path.of("").resolve(this.name);
        this.generators = new HashSet<>();
        this.teams = new HashSet<>();
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Path getInstancePath() {
        return this.instancePath;
    }

    public @NotNull Set<ConfigTeam> getTeams() {
        return this.teams;
    }

    public @NotNull Set<ConfigGenerator> getGenerators() {
        return this.generators;
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
