package com.leogianfagna;

public class CooldownEntry {
    private int seconds;
    private boolean enabled;

    public CooldownEntry(int seconds, boolean enabled) {
        this.seconds = seconds;
        this.enabled = enabled;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
