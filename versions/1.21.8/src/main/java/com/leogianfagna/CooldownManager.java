package com.leogianfagna;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// Usa termo "entry" para cada elemento que possa ser adicionado na lista de cooldowns, para não se limitar apenas à encantamentos, mcmmo, etc, e
// poder generalizar para qualquer situação. Portanto, entries são a lista de todos os itens rastreáveis com cooldown

public class CooldownManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("enchantcooldown.json");
    public final Map<String, Integer> entries = new HashMap<>();
    public final Map<String, Long> activeCooldowns = new HashMap<>();

    public void loadConfig() {
        try {
            // Encontrar configuração padrão ou iniciar com uma genérica
            if (Files.exists(CONFIG_PATH)) {
                entries.putAll(
                        GSON.fromJson(new FileReader(CONFIG_PATH.toFile()), new TypeToken<Map<String, Integer>>() {
                        }.getType()));
            } else {
                entries.put("Experiente", 5);
                saveConfig();
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar configurações: " + e.getMessage());
        }
    }

    public void startCooldown(String entry) {
        if (entries.containsKey(entry)) {
            long now = System.currentTimeMillis();
            long durationMs = entries.get(entry) * 1000L;
            long endTime = now + durationMs;
            activeCooldowns.put(entry, endTime);
            System.out.println("[CooldownManager] Cooldown salvo para " + entry + ": " + endTime);
        }
    }

    public float getProgress(String entry) {
        if (!activeCooldowns.containsKey(entry))
            return 1.0f;

        long now = System.currentTimeMillis();
        long endTime = activeCooldowns.get(entry);

        if (now >= endTime) {
            activeCooldowns.remove(entry);
            return 1.0f;
        }

        long durationMs = entries.get(entry) * 1000L;
        return (float) (endTime - now) / durationMs;
    }

    public void saveConfig() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(entries, writer);
        } catch (Exception e) {
            System.err.println("Erro ao salvar configurações: " + e.getMessage());
        }
    }

    public void setEntry(String entry, int seconds) {
        entries.put(entry, seconds);
        saveConfig();
    }

    public boolean removeEntry(String entry) {
        boolean removed = entries.remove(entry) != null;
        if (removed)
            saveConfig();
        return removed;
    }
}