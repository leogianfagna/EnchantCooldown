package com.leogianfagna;

import com.leogianfagna.client.EquippedEnchantScanner;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HudRenderer {
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) {
                return;
            }

            int x = 10;
            int y = 10;
            List<EquippedEnchant> equippedEnchantments = EquippedEnchantScanner.scan();

            for (Map.Entry<String, CooldownEntry> entry : EnchantCooldown.COOLDOWN_MANAGER.getEntries().entrySet()) {
                String trackedEntry = entry.getKey();
                if (!entry.getValue().isEnabled()) {
                    continue;
                }

                float progress = EnchantCooldown.COOLDOWN_MANAGER.getProgress(trackedEntry);
                if (progress < 1.0f) {
                    int secondsLeft = (int) ((EnchantCooldown.COOLDOWN_MANAGER.activeCooldowns.get(trackedEntry)
                            - System.currentTimeMillis()) / 1000);
                    String text = String.format("%s: %ds (%.0f%%)", trackedEntry, secondsLeft, progress * 100);

                    Optional<ItemStack> sourceStack = findSourceStack(equippedEnchantments, trackedEntry);
                    if (sourceStack.isPresent()) {
                        drawContext.drawItem(sourceStack.get(), x, y - 4);
                        drawContext.drawText(client.textRenderer, text, x + 20, y, 0xFFFFFFFF, false);
                        y += 16;
                    } else {
                        drawContext.drawText(client.textRenderer, text, x, y, 0xFFFFFFFF, false);
                        y += 10;
                    }
                }
            }
        });
    }

    private static Optional<ItemStack> findSourceStack(List<EquippedEnchant> equippedEnchantments, String displayName) {
        return equippedEnchantments.stream()
                .filter(enchantment -> enchantment.getDisplayName().equals(displayName))
                .map(EquippedEnchant::getSourceStack)
                .filter(stack -> stack != null && !stack.isEmpty())
                .findFirst();
    }
}
