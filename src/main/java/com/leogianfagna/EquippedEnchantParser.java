package com.leogianfagna;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquippedEnchantParser {
    private static final String ADVANCED_ENCHANTMENTS_PREFIX = "advancedenchantments:ae_enchantment-";
    private static final Pattern ENCHANTMENT_PATTERN = Pattern.compile(
            Pattern.quote(ADVANCED_ENCHANTMENTS_PREFIX) + "([a-z0-9_-]+)\"?\\s*[:=]\\s*(\\d+)",
            Pattern.CASE_INSENSITIVE);

    public static List<EquippedEnchant> parse(String customData, List<String> loreLines) {
        Map<String, EquippedEnchant> enchantments = new LinkedHashMap<>();
        Matcher matcher = ENCHANTMENT_PATTERN.matcher(customData);

        while (matcher.find()) {
            String id = matcher.group(1).toLowerCase();
            int level = Integer.parseInt(matcher.group(2));
            String displayName = findDisplayName(id, loreLines);
            if (displayName != null) {
                enchantments.put(id, new EquippedEnchant(id, displayName, level));
            }
        }

        return new ArrayList<>(enchantments.values());
    }

    private static String findDisplayName(String id, List<String> loreLines) {
        String normalizedId = normalize(id);
        for (String loreLine : loreLines) {
            String normalizedLore = normalize(loreLine);
            if (normalizedLore.contains(normalizedId)) {
                return loreLine;
            }
        }

        return null;
    }

    private static String normalize(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
