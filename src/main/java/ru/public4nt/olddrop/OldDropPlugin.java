package ru.public4nt.olddrop;

import ru.public4nt.olddrop.config.ConfigManager;
import ru.public4nt.olddrop.listener.DeathDropListener;
import ru.public4nt.olddrop.command.ReloadCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class OldDropPlugin extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.load();

        getServer().getPluginManager().registerEvents(new DeathDropListener(configManager), this);

        PluginCommand cmd = getCommand("olddrop");
        if (cmd == null) {
            getLogger().severe("Команда olddrop не найдена в plugin.yml, плагин выключается.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        cmd.setExecutor(new ReloadCommand(this));

        getLogger().info("OldDropItems включён! (версия " + getDescription().getVersion() + ")");
    }

    @Override
    public void onDisable() {
        getLogger().info("OldDropItems выключен.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
