package com.leogianfagna;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext; 
import net.minecraft.client.font.TextRenderer; 
import java.util.Map;

public class HudRenderer {
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            int x = 10;
            int y = 10;

            TextRenderer textRenderer = client.textRenderer;

            for (Map.Entry<String, Integer> entry : EnchantCooldown.COOLDOWN_MANAGER.entries.entrySet()) {
                String trackedEntry = entry.getKey();
                float progress = EnchantCooldown.COOLDOWN_MANAGER.getProgress(trackedEntry);

                if (progress < 1.0f) {
                    int secondsLeft = (int) ((EnchantCooldown.COOLDOWN_MANAGER.activeCooldowns.get(trackedEntry)
                                - System.currentTimeMillis()) / 1000);
                    String text = String.format("%s: %ds (%.0f%%)", trackedEntry, secondsLeft, progress * 100);

                    drawContext.drawText(textRenderer, text, x, y, 0xFFFFFF, false);
                    y += 10;
                }
            }
        });
    }
}