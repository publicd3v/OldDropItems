package ru.public4nt.olddrop.config;

import ru.public4nt.olddrop.OldDropPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConfigManager {

    private static final double SCATTER_MIN = 0.0;
    private static final double SCATTER_MAX = 1.0;
    private static final double THROW_MIN = 0.0;
    private static final double THROW_MAX = 2.0;

    private final OldDropPlugin plugin;
    private FileConfiguration config;

    private boolean enabled;
    private double scatter;
    private double throwHeight;
    private int pickupDelay;

    public ConfigManager(OldDropPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", true);
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultsStream = plugin.getResource("config.yml");
        if (defaultsStream != null) {
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultsStream, StandardCharsets.UTF_8));
            config.setDefaults(defaults);
        }

        cacheValues();
    }

    private void cacheValues() {
        enabled = config.getBoolean("enabled", true);
        scatter = clamp(config.getDouble("scatter", 0.3), SCATTER_MIN, SCATTER_MAX);
        throwHeight = clamp(config.getDouble("throw-height", 0.5), THROW_MIN, THROW_MAX);
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
