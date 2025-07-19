package com.leogianfagna;

import net.fabricmc.api.ClientModInitializer;
import com.leogianfagna.command.TrackerCommand;

public class EnchantCooldownClient implements ClientModInitializer {

    public static final CooldownManager COOLDOWN_MANAGER = new CooldownManager();

    @Override
    public void onInitializeClient() {
        COOLDOWN_MANAGER.loadConfig();
        HudRenderer.register();
        TrackerCommand.register();
    }
}
