package com.leogianfagna;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CooldownManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configPath;
    private final Map<String, CooldownEntry> entries = new LinkedHashMap<>();
    public final Map<String, Long> activeCooldowns = new HashMap<>();

    public CooldownManager() {
        this(FabricLoader.getInstance().getConfigDir().resolve("enchantcooldown.json"));
    }

    public CooldownManager(Path configPath) {
        this.configPath = configPath;
    }

    public void loadConfig() {
        try {
            entries.clear();
            activeCooldowns.clear();

            if (Files.exists(configPath)) {
                try (Reader reader = new FileReader(configPath.toFile())) {
                    Map<String, JsonElement> loadedEntries = GSON.fromJson(reader, new TypeToken<Map<String, JsonElement>>() {
                    }.getType());

                    if (loadedEntries != null) {
                        loadedEntries.forEach((name, value) -> entries.put(name, parseEntry(value)));
                    }
                }
            } else {
                entries.put("Experiente", new CooldownEntry(5, true));
                saveConfig();
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar configuracoes: " + e.getMessage());
        }
    }

    private CooldownEntry parseEntry(JsonElement value) {
        if (value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
            return new CooldownEntry(Math.max(1, value.getAsInt()), true);
        }

        if (value != null && value.isJsonObject()) {
            JsonObject object = value.getAsJsonObject();
            int seconds = object.has("seconds") ? object.get("seconds").getAsInt() : 1;
            boolean enabled = !object.has("enabled") || object.get("enabled").getAsBoolean();
            return new CooldownEntry(Math.max(1, seconds), enabled);
        }

        return new CooldownEntry(1, true);
    }

    public Map<String, CooldownEntry> getEntries() {
        return Collections.unmodifiableMap(entries);
    }

    public void startCooldown(String entry) {
        CooldownEntry cooldownEntry = entries.get(entry);
        if (cooldownEntry != null && cooldownEntry.isEnabled()) {
            long now = System.currentTimeMillis();
            long durationMs = cooldownEntry.getSeconds() * 1000L;
            long endTime = now + durationMs;
            activeCooldowns.put(entry, endTime);
            System.out.println("[CooldownManager] Cooldown salvo para " + entry + ": " + endTime);
        }
    }

    public float getProgress(String entry) {
        if (!activeCooldowns.containsKey(entry)) {
            return 1.0f;
        }

        long now = System.currentTimeMillis();
        long endTime = activeCooldowns.get(entry);

        if (now >= endTime) {
            activeCooldowns.remove(entry);
            return 1.0f;
        }

        CooldownEntry cooldownEntry = entries.get(entry);
        if (cooldownEntry == null || !cooldownEntry.isEnabled()) {
            activeCooldowns.remove(entry);
            return 1.0f;
        }

        long durationMs = cooldownEntry.getSeconds() * 1000L;
        return (float) (endTime - now) / durationMs;
    }

    public void saveConfig() {
        try (Writer writer = new FileWriter(configPath.toFile())) {
            GSON.toJson(entries, writer);
        } catch (Exception e) {
            System.err.println("Erro ao salvar configuracoes: " + e.getMessage());
        }
    }

    public void setEntry(String entry, int seconds) {
        CooldownEntry currentEntry = entries.get(entry);
        boolean enabled = currentEntry == null || currentEntry.isEnabled();
        entries.put(entry, new CooldownEntry(Math.max(1, seconds), enabled));
        saveConfig();
    }

    public void setEntryEnabled(String entry, boolean enabled) {
        CooldownEntry cooldownEntry = entries.get(entry);
        if (cooldownEntry == null) {
            return;
        }

        cooldownEntry.setEnabled(enabled);
        if (!enabled) {
            activeCooldowns.remove(entry);
        }
        saveConfig();
    }

    public boolean removeEntry(String entry) {
        boolean removed = entries.remove(entry) != null;
        if (removed) {
            activeCooldowns.remove(entry);
            saveConfig();
        }
        return removed;
    }
}
