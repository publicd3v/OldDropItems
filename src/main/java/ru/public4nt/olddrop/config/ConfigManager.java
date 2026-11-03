package ru.public4nt.olddrop.config;

import ru.public4nt.olddrop.OldDropPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final OldDropPlugin plugin;

    private boolean enabled;
    private double scatter;
    private double throwHeight;
    private int pickupDelay;

    public ConfigManager(OldDropPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig();
        enabled = config.getBoolean("enabled", true);
        scatter = clamp(config.getDouble("scatter", 0.3), 0.0, 1.0);
        throwHeight = clamp(config.getDouble("throw-height", 0.5), 0.0, 2.0);
        pickupDelay = Math.max(0, config.getInt("pickup-delay", 5));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public void reload() {
        load();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public double getScatter() {
        return scatter;
    }

    public double getThrowHeight() {
        return throwHeight;
    }

    public int getPickupDelay() {
        return pickupDelay;
    }
}
