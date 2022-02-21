package dev.emortal.sleepystom.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
