package com.leogianfagna;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EquippedEnchantParserTest {
    @Test
    void parsesAdvancedEnchantmentsFromCustomDataAndLore() {
        String customData = "{PublicBukkitValues:{\"advancedenchantments:ae_enchantment-sobrecarga\":2,\"advancedenchantments:slots\":1}}";
        List<String> lore = List.of("Sobrecarga II");

        List<EquippedEnchant> enchantments = EquippedEnchantParser.parse(customData, lore);

        assertEquals(1, enchantments.size());
        assertEquals("sobrecarga", enchantments.get(0).getId());
        assertEquals("Sobrecarga II", enchantments.get(0).getDisplayName());
        assertEquals(2, enchantments.get(0).getLevel());
    }

    @Test
    void validatesAccentlessIdAgainstAccentedLoreName() {
        String customData = "{PublicBukkitValues:{\"advancedenchantments:ae_enchantment-sumidao\":4}}";
        List<String> lore = List.of("Sumidão IV");

        List<EquippedEnchant> enchantments = EquippedEnchantParser.parse(customData, lore);

        assertEquals(1, enchantments.size());
        assertEquals("Sumidão IV", enchantments.get(0).getDisplayName());
    }

    @Test
    void ignoresAdvancedEnchantmentsDataWhenLoreDoesNotMatchId() {
        String customData = "{PublicBukkitValues:{\"advancedenchantments:ae_enchantment-sobrecarga\":2}}";
        List<String> lore = List.of("Outro Encantamento II");

        List<EquippedEnchant> enchantments = EquippedEnchantParser.parse(customData, lore);

        assertEquals(0, enchantments.size());
    }
}
