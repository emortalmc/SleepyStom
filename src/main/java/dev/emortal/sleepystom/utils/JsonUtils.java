package dev.emortal.sleepystom.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class JsonUtils {

    public static @NotNull Optional<JsonElement> readPath(Path path) {
        File file = path.toFile();
        try (FileReader fileReader = new FileReader(file)) {
            return Optional.ofNullable(JsonParser.parseReader(fileReader));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static @NotNull Pos jsonToPos(@NotNull JsonObject json) {
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();
        float yaw = json.has("yaw") ? json.get("yaw").getAsFloat() : 0f;
        float pitch = json.has("pitch") ? json.get("pitch").getAsFloat() : 0f;

        return new Pos(x, y, z, yaw, pitch);
    }

    public static @NotNull Vec jsonToVec(@NotNull JsonObject json) {
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();

        return new Vec(x, y, z);
    }
}
