package ru.public4nt.olddrop.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.public4nt.olddrop.OldDropPlugin;

public final class ReloadCommand implements CommandExecutor {

    private final OldDropPlugin plugin;

    public ReloadCommand(OldDropPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String[] args) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(Component.text("Использование: /" + label + " reload"));
            return true;
        }

        plugin.getConfigManager().load();
        sender.sendMessage(Component.text("OldDropItems: конфигурация перезагружена."));
        return true;
    }
}