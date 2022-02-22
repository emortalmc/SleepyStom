package dev.emortal.sleepystom.utils;

import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class GeneratorUtils {

    public static @NotNull Material suggestMaterial(@NotNull Material blockMaterial) {
        String namespace = blockMaterial.namespace().asString();
        if (namespace.endsWith("_block")) {
            Material found = Material.fromNamespaceId(namespace.substring(0, namespace.length() - 6));
            return found == null ? Material.AIR : found;
        }
        return Material.AIR;
    }
}
