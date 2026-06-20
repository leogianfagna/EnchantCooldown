package com.leogianfagna;

import net.minecraft.item.ItemStack;

public class EquippedEnchant {
    private final String id;
    private final String displayName;
    private final int level;
    private final ItemStack sourceStack;

    public EquippedEnchant(String id, String displayName, int level) {
        this(id, displayName, level, null);
    }

    public EquippedEnchant(String id, String displayName, int level, ItemStack sourceStack) {
        this.id = id;
        this.displayName = displayName;
        this.level = level;
        this.sourceStack = sourceStack == null || sourceStack.isEmpty() ? null : sourceStack.copy();
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public ItemStack getSourceStack() {
        return sourceStack == null ? null : sourceStack.copy();
    }

    public EquippedEnchant withSourceStack(ItemStack stack) {
        return new EquippedEnchant(id, displayName, level, stack);
    }
}
