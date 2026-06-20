package com.leogianfagna.client;

import com.leogianfagna.EquippedEnchant;
import com.leogianfagna.EquippedEnchantParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EquippedEnchantScanner {
    public static List<EquippedEnchant> scan() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return List.of();
        }

        Map<String, EquippedEnchant> enchantments = new LinkedHashMap<>();
        PlayerInventory inventory = client.player.getInventory();
        for (int slot = 0; slot < inventory.size(); slot++) {
            for (EquippedEnchant enchantment : scanStack(inventory.getStack(slot))) {
                enchantments.put(enchantment.getId(), enchantment);
            }
        }

        return new ArrayList<>(enchantments.values());
    }

    private static List<EquippedEnchant> scanStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return List.of();
        }

        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) {
            return List.of();
        }

        List<EquippedEnchant> enchantments = EquippedEnchantParser.parse(customData.copyNbt().toString(), getLoreLines(stack));
        return enchantments.stream()
                .map(enchantment -> enchantment.withSourceStack(stack))
                .toList();
    }

    private static List<String> getLoreLines(ItemStack stack) {
        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) {
            return List.of();
        }

        List<String> lines = new ArrayList<>();
        for (Text line : lore.lines()) {
            String text = line.getString().trim();
            if (!text.isEmpty()) {
                lines.add(text);
            }
        }
        return lines;
    }
}
