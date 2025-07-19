package com.leogianfagna.mixin;

import com.leogianfagna.ActionBarListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "method_1758", at = @At("HEAD"))
    public void onSetOverlayMessage(Text message, boolean tintedBackground, CallbackInfo ci) {
        ActionBarListener.handleActionBarMessage(message.getString());
    }
}
