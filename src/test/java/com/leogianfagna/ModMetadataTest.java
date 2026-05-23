package com.leogianfagna;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModMetadataTest {
    @Test
    void declaresLoadableMainEntrypoint() throws Exception {
        JsonObject metadata = JsonParser.parseString(Files.readString(Path.of("src/main/resources/fabric.mod.json")))
                .getAsJsonObject();
        String entrypoint = metadata.getAsJsonObject("entrypoints")
                .getAsJsonArray("main")
                .get(0)
                .getAsString();

        Class<?> entrypointClass = Class.forName(entrypoint, false, getClass().getClassLoader());

        assertEquals("enchantcooldown", metadata.get("id").getAsString());
        assertEquals(EnchantCooldown.class.getName(), entrypoint);
        assertTrue(ModInitializer.class.isAssignableFrom(entrypointClass));
    }
}
