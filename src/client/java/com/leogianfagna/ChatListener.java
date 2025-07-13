package com.leogianfagna;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatListener {
    public static void register() {
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            // Listener de chat genérico: ainda nenhuma funcionalidade foi implementada com esse listener
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                    Text.of("ChatListener registrado.")
            );
        });
    }
}
