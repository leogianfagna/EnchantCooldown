package com.leogianfagna;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CooldownManagerTest {
    @TempDir
    Path tempDir;

    @Test
    void initializesDefaultConfigWhenFileDoesNotExist() throws Exception {
        Path configPath = tempDir.resolve("enchantcooldown.json");
        CooldownManager manager = new CooldownManager(configPath);

        manager.loadConfig();

        assertTrue(Files.exists(configPath));
        assertTrue(manager.getEntries().containsKey("Experiente"));
        assertEquals(5, manager.getEntries().get("Experiente").getSeconds());
        assertTrue(manager.getEntries().get("Experiente").isEnabled());
    }

    @Test
    void migratesLegacyConfigToEnabledEntries() throws Exception {
        Path configPath = tempDir.resolve("enchantcooldown.json");
        Files.writeString(configPath, "{\"Furacao\":25}");
        CooldownManager manager = new CooldownManager(configPath);

        manager.loadConfig();

        assertEquals(25, manager.getEntries().get("Furacao").getSeconds());
        assertTrue(manager.getEntries().get("Furacao").isEnabled());
    }

    @Test
    void disabledEntriesRemainSavedButDoNotStartCooldowns() {
        CooldownManager manager = new CooldownManager(tempDir.resolve("enchantcooldown.json"));

        manager.setEntry("Furacao", 25);
        manager.setEntryEnabled("Furacao", false);
        manager.startCooldown("Furacao");

        assertTrue(manager.getEntries().containsKey("Furacao"));
        assertFalse(manager.getEntries().get("Furacao").isEnabled());
        assertFalse(manager.activeCooldowns.containsKey("Furacao"));
    }

    @Test
    void disablingEntryClearsActiveCooldown() {
        CooldownManager manager = new CooldownManager(tempDir.resolve("enchantcooldown.json"));

        manager.setEntry("Furacao", 25);
        manager.startCooldown("Furacao");
        manager.setEntryEnabled("Furacao", false);

        assertFalse(manager.activeCooldowns.containsKey("Furacao"));
    }

    @Test
    void updatesEntryNameAndCooldown() {
        CooldownManager manager = new CooldownManager(tempDir.resolve("enchantcooldown.json"));

        manager.setEntry("Furacao", 25);
        boolean updated = manager.updateEntry("Furacao", "Furacao Supremo", 40);

        assertTrue(updated);
        assertFalse(manager.getEntries().containsKey("Furacao"));
        assertEquals(40, manager.getEntries().get("Furacao Supremo").getSeconds());
        assertTrue(manager.getEntries().get("Furacao Supremo").isEnabled());
    }

    @Test
    void updateEntryPreservesDisabledState() {
        CooldownManager manager = new CooldownManager(tempDir.resolve("enchantcooldown.json"));

        manager.setEntry("Furacao", 25);
        manager.setEntryEnabled("Furacao", false);
        boolean updated = manager.updateEntry("Furacao", "Furacao Supremo", 40);

        assertTrue(updated);
        assertFalse(manager.getEntries().get("Furacao Supremo").isEnabled());
    }
}
