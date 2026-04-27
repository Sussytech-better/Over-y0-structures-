package com.sussytech.overy0structures;

import java.lang.reflect.Method;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class OverY0StructuresPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        registerPaperStructureSpawnListener();
    }

    @SuppressWarnings("unchecked")
    private void registerPaperStructureSpawnListener() {
        try {
            Class<? extends Event> structureSpawnEventClass =
                    (Class<? extends Event>) Class.forName("io.papermc.paper.event.structure.StructureSpawnEvent");

            PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvent(
                    structureSpawnEventClass,
                    new Listener() {},
                    EventPriority.NORMAL,
                    (EventExecutor) (listener, event) -> handleStructureSpawn(event),
                    this
            );

            getLogger().info("Paper StructureSpawnEvent hook enabled.");
        } catch (ClassNotFoundException cause) {
            getLogger().warning("Paper structure event class not found. This plugin requires Paper to adjust structure spawn locations.");
        } catch (Throwable cause) {
            getLogger().severe("Failed to register Paper structure spawn hook: " + cause.getMessage());
        }
    }

    private void handleStructureSpawn(Event event) {
        try {
            Method getLocationMethod = event.getClass().getMethod("getLocation");
            Location location = (Location) getLocationMethod.invoke(event);

            if (location.getY() < 0) {
                Location moved = location.clone();
                moved.setY(0);

                Method setLocationMethod = event.getClass().getMethod("setLocation", Location.class);
                setLocationMethod.invoke(event, moved);

                getLogger().info("Moved structure spawn from y=" + location.getY() + " to y=0 at " +
                        location.getBlockX() + "," + location.getBlockZ());
            }
        } catch (ReflectiveOperationException ex) {
            getLogger().warning("Unable to adjust structure spawn location: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }
    }
}
