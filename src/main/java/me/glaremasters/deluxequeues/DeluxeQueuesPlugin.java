package me.glaremasters.deluxequeues;

import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public final class DeluxeQueuesPlugin extends Plugin {

    private DeluxeQueues deluxeQueues;

    @Override
    public void onEnable() {
        this.deluxeQueues = new DeluxeQueues(this);
        this.deluxeQueues.load();
    }

    @Override
    public void onDisable() {
        if (this.deluxeQueues != null) {
            this.deluxeQueues.kill();
        }

        this.deluxeQueues = null;
    }

    @Nullable
    public DeluxeQueues getDeluxeQueues() {
        return this.deluxeQueues;
    }

}
