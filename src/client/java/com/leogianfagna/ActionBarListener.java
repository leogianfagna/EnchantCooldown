package com.leogianfagna;

public class ActionBarListener {

    public static void handleActionBarMessage(String message) {
        for (String entry : EnchantCooldownClient.COOLDOWN_MANAGER.entries.keySet()) {
            if (message.contains(entry)) {
                EnchantCooldownClient.COOLDOWN_MANAGER.startCooldown(entry);
            }
        }
    }
}
