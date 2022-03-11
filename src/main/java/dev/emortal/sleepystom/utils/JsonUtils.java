package dev.emortal.sleepystom.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import dev.emortal.sleepystom.model.config.map.ConfigGenerator;
import dev.emortal.sleepystom.model.config.map.ConfigMap;
import dev.emortal.sleepystom.model.config.map.ConfigTeam;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class JsonUtils {

    private static final JsonSerializer<Duration> DURATION_SERIALIZER = (duration, type, context) -> {
        JsonObject json = new JsonObject();
        json.addProperty("seconds", duration.getSeconds());
        return json;
    };

    private static final JsonDeserializer<Duration> DURATION_DESERIALIZER = (element, type, context) ->
        Duration.ofSeconds(element.getAsJsonObject().get("seconds").getAsLong());

    private static final JsonSerializer<Color> COLOR_SERIALIZER = (color, type, context) -> {
        JsonObject json = new JsonObject();
        json.addProperty("red", color.red());
        json.addProperty("green", color.green());
        json.addProperty("blue", color.blue());
        return json;
    };

    private static final JsonDeserializer<Color> COLOR_DESERIALIZER = (element, type, context) -> {
        JsonObject json = element.getAsJsonObject();
        return new Color(
            json.get("red").getAsInt(),
            json.get("green").getAsInt(),
            json.get("green").getAsInt()
        );
    };

    private static final JsonSerializer<Pos> POS_SERIALIZER = (pos, type, context) -> {
        JsonObject json = new JsonObject();
        json.addProperty("x", pos.x());
        json.addProperty("y", pos.y());
        json.addProperty("z", pos.z());
        json.addProperty("yaw", pos.yaw());
        json.addProperty("pitch", pos.pitch());

        return json;
    };

    private static final JsonDeserializer<Pos> POS_DESERIALIZER = (element, type, context) -> {
        JsonObject json = element.getAsJsonObject();
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();
        float yaw = json.has("yaw") ? json.get("yaw").getAsFloat() : 0f;
        float pitch = json.has("pitch") ? json.get("pitch").getAsFloat() : 0f;

        return new Pos(x, y, z, yaw, pitch);
    };

    private static final JsonSerializer<Material> MATERIAL_SERIALIZER = (material, type, context) -> {
        JsonObject json = new JsonObject();
        json.addProperty("material", material.name());
        return json;
    };

    private static final JsonDeserializer<Material> MATERIAL_DESERIALIZER = (element, type, context) ->
        Material.fromNamespaceId(element.getAsJsonObject().get("material").getAsString());

    private static final JsonSerializer<ConfigMap> BED_WARS_MAP_SERIALIZER = (map, type, context) -> {
        JsonObject json = new JsonObject();
        json.addProperty("name", map.getName());
        json.addProperty("filePath", map.getInstancePath().toString());
        json.addProperty("minTeamSize", map.getMinTeamSize());
        json.addProperty("maxTeamSize", map.getMaxTeamSize());
        json.addProperty("filePath", map.getInstancePath().toString());
        json.add("teams", context.serialize(map.getTeams()));
        json.add("generators", context.serialize(map.getGenerators()));

        return json;
    };

    private static final JsonDeserializer<ConfigMap> BED_WARS_MAP_DESERIALIZER = (element, type, context) -> {
        JsonObject json = element.getAsJsonObject();
        String name = json.get("name").getAsString();
        int minTeamSize = json.get("minTeamSize").getAsInt();
        int maxTeamSize = json.get("maxTeamSize").getAsInt();
        Set<ConfigTeam> teams = context.deserialize(json.get("teams"), new TypeToken<HashSet<ConfigTeam>>(){}.getType());
        Set<ConfigGenerator> generators = context.deserialize(json.get("generators"), new TypeToken<HashSet<ConfigGenerator>>(){}.getType());

        if (teams == null)
            teams = new HashSet<>();
        if (generators == null)
            generators = new HashSet<>();

        return new ConfigMap(name, teams, generators, minTeamSize, maxTeamSize);
    };

    private static final JsonSerializer<ConfigTeam> MAP_TEAM_SERIALIZER = (team, type, context) -> {
        JsonObject json = new JsonObject();
        json.addProperty("name", team.name());
        json.add("color", context.serialize(team.color()));
        json.add("spawn", context.serialize(team.spawn()));

        return json;
    };

    private static final JsonDeserializer<ConfigTeam> MAP_TEAM_DESERIALIZER = (element, type, context) -> {
        JsonObject json = element.getAsJsonObject();
        String name = json.get("name").getAsString();
        Color color = context.deserialize(json.get("color"), Color.class);
        Pos spawn = context.deserialize(json.get("spawn"), Pos.class);

        return new ConfigTeam(name, color, spawn);
    };

    private static final JsonSerializer<ConfigGenerator> GENERATOR_SERIALIZER = (generator, type, context) -> {
        JsonObject json = new JsonObject();
        json.add("pos", context.serialize(generator.getPos()));
        json.addProperty("material", generator.getMaterial().name());
        json.add("delay", context.serialize(generator.getDelay()));

        return json;
    };

    private static final JsonDeserializer<ConfigGenerator> GENERATOR_DESERIALIZER = (element, type, context) -> {
        JsonObject json = element.getAsJsonObject();
        Pos pos = context.deserialize(json.get("pos"), Pos.class);
        Material material = Material.fromNamespaceId(json.get("material").getAsString());
        Duration delay = context.deserialize(json.get("delay"), Duration.class);
        if (material == null)
            throw new JsonParseException("Could not find material with namespace id " + json.get("material").getAsString());

        return new ConfigGenerator(pos, material, delay);
    };

    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        // java classes
        .registerTypeAdapter(Duration.class, DURATION_DESERIALIZER)
        .registerTypeAdapter(Duration.class, DURATION_SERIALIZER)
        // minestom classes
        .registerTypeAdapter(Color.class, COLOR_DESERIALIZER)
        .registerTypeAdapter(Color.class, COLOR_SERIALIZER)

        .registerTypeAdapter(Pos.class, POS_DESERIALIZER)
        .registerTypeAdapter(Pos.class, POS_SERIALIZER)

        .registerTypeAdapter(Material.class, MATERIAL_SERIALIZER)
        .registerTypeAdapter(Material.class, MATERIAL_DESERIALIZER)
        // bedwars classes
        .registerTypeAdapter(ConfigMap.class, BED_WARS_MAP_DESERIALIZER)
        .registerTypeAdapter(ConfigMap.class, BED_WARS_MAP_SERIALIZER)

        .registerTypeAdapter(ConfigTeam.class, MAP_TEAM_DESERIALIZER)
        .registerTypeAdapter(ConfigTeam.class, MAP_TEAM_SERIALIZER)

        .registerTypeAdapter(ConfigGenerator.class, GENERATOR_DESERIALIZER)
        .registerTypeAdapter(ConfigGenerator.class, GENERATOR_SERIALIZER)
        .create();

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
