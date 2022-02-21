package dev.emortal.sleepystom.model.config.map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.StreamSupport;

public record ConfigMap(@NotNull String name, int minTeamSize, int maxTeamSize, @NotNull List<MapTeam> teams) {

    public static ConfigMap fromJson(JsonObject json) {
        String name = json.get("name").getAsString();
        int minTeamSize = json.get("minTeamSize").getAsInt();
        int maxTeamSize = json.get("maxTeamSize").getAsInt();
        List<MapTeam> teams = StreamSupport.stream(json.get("teams").getAsJsonArray().spliterator(), false)
            .map(JsonElement::getAsJsonObject)
            .map(MapTeam::fromJson)
            .toList();

        return new ConfigMap(name, minTeamSize, maxTeamSize, teams);
    }
}
