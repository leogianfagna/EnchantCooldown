package com.leogianfagna.command;

import com.leogianfagna.EnchantCooldownClient;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class TrackerCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("ecd")
                    .then(ClientCommandManager.literal("list")
                            .executes(context -> {
                                context.getSource().sendFeedback(Text.of("§6Habilidades configuradas:"));
                                EnchantCooldownClient.COOLDOWN_MANAGER.entries.forEach((entry, seconds) -> {
                                    context.getSource()
                                            .sendFeedback(Text.of("§7- §f" + entry + "§7: §e" + seconds + "s"));
                                });
                                return Command.SINGLE_SUCCESS;
                            }))
                    .then(ClientCommandManager.literal("set")
                            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                    .then(ClientCommandManager.argument("seconds", IntegerArgumentType.integer(1))
                                            .executes(context -> {
                                                String entry = StringArgumentType.getString(context, "name");
                                                int seconds = IntegerArgumentType.getInteger(context, "seconds");
                                                EnchantCooldownClient.COOLDOWN_MANAGER.setEntry(entry, seconds);
                                                context.getSource().sendFeedback(Text.of("§aCooldown de " + entry
                                                        + " definido para " + seconds + " segundos"));
                                                return Command.SINGLE_SUCCESS;
                                            })))

                            .then(ClientCommandManager.literal("remove")
                                    .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                            .executes(context -> {
                                                String entry = StringArgumentType.getString(context, "name");
                                                if (EnchantCooldownClient.COOLDOWN_MANAGER.removeEntry(entry)) {
                                                    context.getSource().sendFeedback(
                                                            Text.of("§aHabilidade " + entry + " removida"));
                                                } else {
                                                    context.getSource()
                                                            .sendFeedback(Text.of("§cHabilidade não encontrada"));
                                                }
                                                return Command.SINGLE_SUCCESS;
                                            })))));
        });
    }
}