package com.leogianfagna.client;

import com.leogianfagna.CooldownEntry;
import com.leogianfagna.EnchantCooldown;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CooldownMenuScreen extends Screen {
    private static final int ENABLED_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DISABLED_TEXT_COLOR = 0xFF777777;
    private static final int LABEL_TEXT_COLOR = 0xFFA0A0A0;

    private final Screen parent;
    private final int initialScrollOffset;
    private TextFieldWidget nameField;
    private TextFieldWidget secondsField;
    private int scrollOffset;

    public CooldownMenuScreen(Screen parent) {
        this(parent, 0);
    }

    private CooldownMenuScreen(Screen parent, int scrollOffset) {
        super(Text.literal("EnchantCooldown"));
        this.parent = parent;
        this.initialScrollOffset = scrollOffset;
    }

    @Override
    protected void init() {
        scrollOffset = initialScrollOffset;

        int centerX = width / 2;
        int top = 36;

        nameField = new TextFieldWidget(textRenderer, centerX - 170, top, 180, 20, Text.literal("Nome"));
        nameField.setMaxLength(64);
        nameField.setPlaceholder(Text.literal("Encantamento"));
        addDrawableChild(nameField);

        secondsField = new TextFieldWidget(textRenderer, centerX + 18, top, 54, 20, Text.literal("Segundos"));
        secondsField.setMaxLength(5);
        secondsField.setPlaceholder(Text.literal("s"));
        addDrawableChild(secondsField);

        addDrawableChild(ButtonWidget.builder(Text.literal("Adicionar"), button -> addEntry())
                .dimensions(centerX + 80, top, 90, 20)
                .build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Fechar"), button -> close())
                .dimensions(centerX - 50, height - 30, 100, 20)
                .build());

        addEntryButtons();
    }

    private void addEntryButtons() {
        List<Map.Entry<String, CooldownEntry>> entries = getSortedEntries();
        int visibleRows = getVisibleRows();
        int maxOffset = Math.max(0, entries.size() - visibleRows);
        scrollOffset = Math.min(scrollOffset, maxOffset);

        int rowY = 72;
        int centerX = width / 2;
        for (int i = scrollOffset; i < entries.size() && i < scrollOffset + visibleRows; i++) {
            Map.Entry<String, CooldownEntry> entry = entries.get(i);
            String name = entry.getKey();
            CooldownEntry cooldownEntry = entry.getValue();
            int y = rowY + ((i - scrollOffset) * 26);

            addDrawableChild(ButtonWidget.builder(getToggleText(cooldownEntry), button -> {
                EnchantCooldown.COOLDOWN_MANAGER.setEntryEnabled(name, !cooldownEntry.isEnabled());
                reload(scrollOffset);
            }).dimensions(centerX + 42, y, 82, 20).build());

            addDrawableChild(ButtonWidget.builder(Text.literal("Remover"), button -> {
                EnchantCooldown.COOLDOWN_MANAGER.removeEntry(name);
                reload(scrollOffset);
            }).dimensions(centerX + 132, y, 76, 20).build());
        }
    }

    private Text getToggleText(CooldownEntry entry) {
        return Text.literal(entry.isEnabled() ? "Enabled" : "Disabled");
    }

    private List<Map.Entry<String, CooldownEntry>> getSortedEntries() {
        List<Map.Entry<String, CooldownEntry>> entries = new ArrayList<>(EnchantCooldown.COOLDOWN_MANAGER.getEntries().entrySet());
        entries.sort(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER));
        return entries;
    }

    private int getVisibleRows() {
        return Math.max(1, (height - 122) / 26);
    }

    private void addEntry() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            return;
        }

        int seconds;
        try {
            seconds = Integer.parseInt(secondsField.getText().trim());
        } catch (NumberFormatException e) {
            return;
        }

        EnchantCooldown.COOLDOWN_MANAGER.setEntry(name, seconds);
        reload(scrollOffset);
    }

    private void reload(int nextScrollOffset) {
        MinecraftClient.getInstance().setScreen(new CooldownMenuScreen(parent, nextScrollOffset));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        List<Map.Entry<String, CooldownEntry>> entries = getSortedEntries();
        int maxOffset = Math.max(0, entries.size() - getVisibleRows());
        int nextOffset = scrollOffset - (int) Math.signum(verticalAmount);
        reload(Math.max(0, Math.min(maxOffset, nextOffset)));
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, ENABLED_TEXT_COLOR);
        context.drawTextWithShadow(textRenderer, Text.literal("Nome"), width / 2 - 170, 25, LABEL_TEXT_COLOR);
        context.drawTextWithShadow(textRenderer, Text.literal("Cooldown"), width / 2 + 18, 25, LABEL_TEXT_COLOR);

        List<Map.Entry<String, CooldownEntry>> entries = getSortedEntries();
        int rowY = 76;
        int centerX = width / 2;
        for (int i = scrollOffset; i < entries.size() && i < scrollOffset + getVisibleRows(); i++) {
            Map.Entry<String, CooldownEntry> entry = entries.get(i);
            CooldownEntry cooldownEntry = entry.getValue();
            int y = rowY + ((i - scrollOffset) * 26);
            int color = cooldownEntry.isEnabled() ? ENABLED_TEXT_COLOR : DISABLED_TEXT_COLOR;

            context.drawTextWithShadow(textRenderer, entry.getKey(), centerX - 208, y + 5, color);
            context.drawTextWithShadow(textRenderer, cooldownEntry.getSeconds() + "s", centerX - 8, y + 5, color);
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
}
