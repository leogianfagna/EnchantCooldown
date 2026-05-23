package com.leogianfagna.command;

import com.leogianfagna.EnchantCooldown;
import com.leogianfagna.client.CooldownMenuScreen;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TrackerCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("ecd")
                    .then(ClientCommandManager.literal("list")
                            .executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                client.execute(() -> client.setScreen(new CooldownMenuScreen(client.currentScreen)));
                                return Command.SINGLE_SUCCESS;
                            }))
                    .then(ClientCommandManager.literal("set")
                            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                    .then(ClientCommandManager.argument("seconds", IntegerArgumentType.integer(1))
                                            .executes(context -> {
                                                String entry = StringArgumentType.getString(context, "name");
                                                int seconds = IntegerArgumentType.getInteger(context, "seconds");
                                                EnchantCooldown.COOLDOWN_MANAGER.setEntry(entry, seconds);
                                                context.getSource().sendFeedback(Text.literal("Cooldown de " + entry
                                                        + " definido para " + seconds + " segundos"));
                                                return Command.SINGLE_SUCCESS;
                                            })))

                            .then(ClientCommandManager.literal("remove")
                                    .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                            .executes(context -> {
                                                String entry = StringArgumentType.getString(context, "name");
                                                if (EnchantCooldown.COOLDOWN_MANAGER.removeEntry(entry)) {
                                                    context.getSource().sendFeedback(Text.literal("Habilidade " + entry + " removida"));
                                                } else {
                                                    context.getSource().sendFeedback(Text.literal("Habilidade nao encontrada"));
                                                }
                                                return Command.SINGLE_SUCCESS;
                                            })))));
        });
    }
}
