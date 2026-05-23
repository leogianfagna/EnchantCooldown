package com.leogianfagna.client;

import net.minecraft.client.gui.screen.Screen;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CooldownMenuScreenTest {
    @Test
    void exposesMenuScreenConstructorUsedByCommandOpeningFlow() throws Exception {
        Constructor<CooldownMenuScreen> constructor = CooldownMenuScreen.class.getConstructor(Screen.class);

        assertEquals(1, constructor.getParameterCount());
        assertEquals(Screen.class, constructor.getParameterTypes()[0]);
        assertTrue(Screen.class.isAssignableFrom(CooldownMenuScreen.class));
    }
}
