package com.leogianfagna;

import com.leogianfagna.command.TrackerCommand;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EnchantCooldown implements ModInitializer {
    public static final String MOD_ID = "enchantcooldown";
    public static final CooldownManager COOLDOWN_MANAGER = new CooldownManager();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("EnchantCooldown loaded.");
        COOLDOWN_MANAGER.loadConfig();

        // Inicialização do lado do cliente feita condicionalmente
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            initializeClient();
        }
    }

    @Environment(EnvType.CLIENT)
    private void initializeClient() {
        HudRenderer.register();
        TrackerCommand.register();
    }
}