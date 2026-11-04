package ru.public4nt.olddrop.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {

    private static final int MAX_PICKUP_DELAY = 200;

    public record Settings(boolean enabled,
                           double scatter,
                           double throwHeight,
                           int pickupDelay) {
    }

    private final JavaPlugin plugin;

    private volatile Settings settings = new Settings(true, 0.3D, 0.5D, 5);

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        FileConfiguration cfg = plugin.getConfig();

        boolean enabled = cfg.getBoolean("enabled", true);
        double scatter = clamp(cfg.getDouble("scatter", 0.3D), 0.0D, 1.0D);
        double throwHeight = clamp(cfg.getDouble("throw-height", 0.5D), 0.0D, 2.0D);
        int pickupDelay = (int) clamp(cfg.getInt("pickup-delay", 5), 0.0D, MAX_PICKUP_DELAY);

        settings = new Settings(enabled, scatter, throwHeight, pickupDelay);
    }

    private static double clamp(double value, double min, double max) {
        if (Double.isNaN(value) || value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public Settings settings() {
        return settings;
    }
}