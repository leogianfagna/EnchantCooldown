package com.leogianfagna.client;

import com.leogianfagna.CooldownEntry;
import com.leogianfagna.EnchantCooldown;
import com.leogianfagna.EquippedEnchant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CooldownMenuScreen extends Screen {
    private static final int ENABLED_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DISABLED_TEXT_COLOR = 0xFF777777;
    private static final int LABEL_TEXT_COLOR = 0xFFA0A0A0;

    private final Screen parent;
    private final int initialScrollOffset;
    private final String initialEditingEntry;
    private TextFieldWidget nameField;
    private TextFieldWidget secondsField;
    private String editingEntry;
    private int scrollOffset;

    public CooldownMenuScreen(Screen parent) {
        this(parent, 0, null);
    }

    private CooldownMenuScreen(Screen parent, int scrollOffset) {
        this(parent, scrollOffset, null);
    }

    private CooldownMenuScreen(Screen parent, int scrollOffset, String editingEntry) {
        super(Text.literal("EnchantCooldown"));
        this.parent = parent;
        this.initialScrollOffset = scrollOffset;
        this.initialEditingEntry = editingEntry;
    }

    @Override
    protected void init() {
        scrollOffset = initialScrollOffset;
        editingEntry = initialEditingEntry;

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

        if (editingEntry != null) {
            CooldownEntry entry = EnchantCooldown.COOLDOWN_MANAGER.getEntries().get(editingEntry);
            if (entry != null) {
                nameField.setText(editingEntry);
                secondsField.setText(String.valueOf(entry.getSeconds()));
            } else {
                nameField.setText(editingEntry);
                secondsField.setText("");
                editingEntry = null;
            }
        }

        addDrawableChild(ButtonWidget.builder(Text.literal(editingEntry == null ? "+" : "✓"), button -> saveEntry())
                .dimensions(centerX + 80, top, 34, 20)
                .build());

        if (editingEntry != null) {
            addDrawableChild(ButtonWidget.builder(Text.literal("×"), button -> reload(scrollOffset))
                    .dimensions(centerX + 122, top, 34, 20)
                    .build());
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Fechar"), button -> close())
                .dimensions(centerX - 50, height - 30, 100, 20)
                .build());

        addEntryButtons();
    }

    private void addEntryButtons() {
        List<Map.Entry<String, CooldownEntry>> entries = getSortedEntries();
        List<EquippedEnchant> equippedEnchantments = EquippedEnchantScanner.scan();
        int configuredVisibleRows = getConfiguredVisibleRows(equippedEnchantments);
        int maxOffset = Math.max(0, entries.size() - configuredVisibleRows);
        scrollOffset = Math.min(scrollOffset, maxOffset);

        int rowY = 72;
        int centerX = width / 2;
        for (int i = scrollOffset; i < entries.size() && i < scrollOffset + configuredVisibleRows; i++) {
            Map.Entry<String, CooldownEntry> entry = entries.get(i);
            String name = entry.getKey();
            CooldownEntry cooldownEntry = entry.getValue();
            int y = rowY + ((i - scrollOffset) * 26);

            addDrawableChild(ButtonWidget.builder(getToggleText(cooldownEntry), button -> {
                EnchantCooldown.COOLDOWN_MANAGER.setEntryEnabled(name, !cooldownEntry.isEnabled());
                reload(scrollOffset, editingEntry);
            }).dimensions(centerX + 76, y, 30, 20).build());

            addDrawableChild(ButtonWidget.builder(Text.literal("✎"), button -> reload(scrollOffset, name))
                    .dimensions(centerX + 114, y, 30, 20)
                    .build());

            addDrawableChild(ButtonWidget.builder(Text.literal("×"), button -> {
                EnchantCooldown.COOLDOWN_MANAGER.removeEntry(name);
                reload(scrollOffset);
            }).dimensions(centerX + 152, y, 30, 20).build());
        }

        int equippedStartY = getEquippedStartY(equippedEnchantments);
        int equippedRows = getEquippedVisibleRows(equippedEnchantments);
        for (int i = 0; i < equippedEnchantments.size() && i < equippedRows; i++) {
            EquippedEnchant enchantment = equippedEnchantments.get(i);
            int y = equippedStartY + 18 + (i * 22);

            addDrawableChild(ButtonWidget.builder(Text.literal("✎"), button -> reload(scrollOffset, enchantment.getDisplayName()))
                    .dimensions(centerX + 152, y, 30, 20)
                    .build());
        }
    }

    private Text getToggleText(CooldownEntry entry) {
        return Text.literal(entry.isEnabled() ? "✓" : "−");
    }

    private List<Map.Entry<String, CooldownEntry>> getSortedEntries() {
        List<Map.Entry<String, CooldownEntry>> entries = new ArrayList<>(EnchantCooldown.COOLDOWN_MANAGER.getEntries().entrySet());
        entries.sort(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER));
        return entries;
    }

    private int getConfiguredVisibleRows(List<EquippedEnchant> equippedEnchantments) {
        if (equippedEnchantments.isEmpty()) {
            return Math.max(1, (height - 122) / 26);
        }

        return Math.max(1, ((height - 122) / 26) / 2);
    }

    private int getEquippedVisibleRows(List<EquippedEnchant> equippedEnchantments) {
        if (equippedEnchantments.isEmpty()) {
            return 0;
        }

        int availableHeight = height - getEquippedStartY(equippedEnchantments) - 56;
        return Math.max(1, availableHeight / 22);
    }

    private int getEquippedStartY(List<EquippedEnchant> equippedEnchantments) {
        if (equippedEnchantments.isEmpty()) {
            return height;
        }

        int configuredRows = getConfiguredVisibleRows(equippedEnchantments);
        return 76 + (configuredRows * 26) + 14;
    }

    private void saveEntry() {
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

        if (editingEntry == null) {
            EnchantCooldown.COOLDOWN_MANAGER.setEntry(name, seconds);
        } else {
            EnchantCooldown.COOLDOWN_MANAGER.updateEntry(editingEntry, name, seconds);
        }
        reload(scrollOffset);
    }

    private void reload(int nextScrollOffset) {
        reload(nextScrollOffset, null);
    }

    private void reload(int nextScrollOffset, String nextEditingEntry) {
        MinecraftClient.getInstance().setScreen(new CooldownMenuScreen(parent, nextScrollOffset, nextEditingEntry));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        List<Map.Entry<String, CooldownEntry>> entries = getSortedEntries();
        int maxOffset = Math.max(0, entries.size() - getConfiguredVisibleRows(EquippedEnchantScanner.scan()));
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
        List<EquippedEnchant> equippedEnchantments = EquippedEnchantScanner.scan();
        int rowY = 76;
        int centerX = width / 2;
        int configuredVisibleRows = getConfiguredVisibleRows(equippedEnchantments);
        for (int i = scrollOffset; i < entries.size() && i < scrollOffset + configuredVisibleRows; i++) {
            Map.Entry<String, CooldownEntry> entry = entries.get(i);
            CooldownEntry cooldownEntry = entry.getValue();
            int y = rowY + ((i - scrollOffset) * 26);
            int color = cooldownEntry.isEnabled() ? ENABLED_TEXT_COLOR : DISABLED_TEXT_COLOR;

            Optional<EquippedEnchant> equippedEnchant = findEquippedEnchant(equippedEnchantments, entry.getKey());
            if (equippedEnchant.isPresent()) {
                drawEnchantName(context, equippedEnchant.get(), centerX - 208, y + 2, color);
            } else {
                context.drawTextWithShadow(textRenderer, entry.getKey(), centerX - 208, y + 5, color);
            }
            context.drawTextWithShadow(textRenderer, cooldownEntry.getSeconds() + "s", centerX - 8, y + 5, color);
        }

        if (!equippedEnchantments.isEmpty()) {
            int equippedStartY = getEquippedStartY(equippedEnchantments);
            context.drawTextWithShadow(textRenderer, Text.literal("Equipados"), centerX - 208, equippedStartY, LABEL_TEXT_COLOR);

            int equippedRows = getEquippedVisibleRows(equippedEnchantments);
            for (int i = 0; i < equippedEnchantments.size() && i < equippedRows; i++) {
                EquippedEnchant enchantment = equippedEnchantments.get(i);
                CooldownEntry configuredEntry = EnchantCooldown.COOLDOWN_MANAGER.getEntries().get(enchantment.getDisplayName());
                int y = equippedStartY + 18 + (i * 22);
                int color = configuredEntry == null ? LABEL_TEXT_COLOR : ENABLED_TEXT_COLOR;
                String cooldownText = configuredEntry == null ? "!" : configuredEntry.getSeconds() + "s";

                drawEnchantName(context, enchantment, centerX - 208, y + 2, color);
                context.drawTextWithShadow(textRenderer, cooldownText, centerX - 8, y + 5, color);
            }
        }
    }

    private void drawEnchantName(DrawContext context, EquippedEnchant enchantment, int x, int y, int color) {
        ItemStack sourceStack = enchantment.getSourceStack();
        if (sourceStack != null) {
            context.drawItem(sourceStack, x, y);
            context.drawTextWithShadow(textRenderer, enchantment.getDisplayName(), x + 20, y + 3, color);
            return;
        }

        context.drawTextWithShadow(textRenderer, enchantment.getDisplayName(), x, y + 3, color);
    }

    private Optional<EquippedEnchant> findEquippedEnchant(List<EquippedEnchant> equippedEnchantments, String displayName) {
        return equippedEnchantments.stream()
                .filter(enchantment -> enchantment.getDisplayName().equals(displayName))
                .findFirst();
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
}
