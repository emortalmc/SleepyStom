package dev.emortal.sleepystom.model.config.map;

import com.google.gson.JsonObject;
import dev.emortal.sleepystom.utils.JsonUtils;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

public record MapTeam(@NotNull String teamName, @NotNull Color color, @NotNull Pos spawnPos) {

    public static MapTeam fromJson(JsonObject json) {
        String teamName = json.get("teamName").getAsString();
        Color color = parseHex(json.get("color").getAsString());
        Pos spawnPos = JsonUtils.jsonToPos(json.get("spawnPos").getAsJsonObject());

        return new MapTeam(teamName, color, spawnPos);
    }

    private static Color parseHex(String hex) {
        java.awt.Color awtColor = java.awt.Color.decode(hex);
        return new Color(awtColor.getRGB());
    }

}
