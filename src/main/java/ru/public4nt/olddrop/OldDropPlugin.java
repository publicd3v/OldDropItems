package ru.public4nt.olddrop;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.public4nt.olddrop.command.ReloadCommand;
import ru.public4nt.olddrop.config.ConfigManager;
import ru.public4nt.olddrop.listener.DeathDropListener;

public final class OldDropPlugin extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.load();

        getServer().getPluginManager().registerEvents(
                new DeathDropListener(configManager, getLogger()),
                this
        );

        PluginCommand command = getCommand("olddrop");
        if (command != null) {
            command.setExecutor(new ReloadCommand(this));
        } else {
            getLogger().warning("Команда olddrop не зарегистрирована в plugin.yml — /olddrop недоступна.");
        }

        getLogger().info("OldDropItems включён (v" + getDescription().getVersion() + ").");
    }

    @Override
    public void onDisable() {
        getLogger().info("OldDropItems выключен.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}