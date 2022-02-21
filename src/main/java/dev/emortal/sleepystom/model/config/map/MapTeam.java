package dev.emortal.sleepystom.model.config.map;

import com.google.gson.JsonObject;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

public record MapTeam(@NotNull String teamName, @NotNull Color color, @NotNull Pos spawnPos) {

    public static MapTeam fromJson(JsonObject json) {
        String teamName = json.get("teamName").getAsString();
        Color color = parseHex(json.get("color").getAsString());
        Pos spawnPos = jsonToPos(json.get("spawnPos").getAsJsonObject());

        return new MapTeam(teamName, color, spawnPos);
    }

    private static Pos jsonToPos(JsonObject json) {
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();
        float yaw = json.get("yaw").getAsFloat();
        float pitch = json.get("pitch").getAsFloat();

        return new Pos(x, y, z, yaw, pitch);
    }

    private static Color parseHex(String hex) {
        java.awt.Color awtColor = java.awt.Color.decode(hex);
        return new Color(awtColor.getRGB());
    }

}
