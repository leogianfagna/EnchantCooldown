package com.leogianfagna;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantCooldown implements ModInitializer {
    public static final String MOD_ID = "enchantcooldown";
    public static final CooldownManager COOLDOWN_MANAGER = new CooldownManager();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("EnchantCooldown carregado!");
        COOLDOWN_MANAGER.loadConfig();
    }
}