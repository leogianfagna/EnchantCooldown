package com.leogianfagna;

public class ActionBarListener {

    public static void handleActionBarMessage(String message) {
        for (String entry : EnchantCooldown.COOLDOWN_MANAGER.entries.keySet()) {
            if (message.contains(entry)) {
                EnchantCooldown.COOLDOWN_MANAGER.startCooldown(entry);
            }
        }
    }
}
