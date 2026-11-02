package ru.public4nt.olddrop.command;

import ru.public4nt.olddrop.OldDropPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private static final String PERMISSION = "olddrop.reload";

    private final OldDropPlugin plugin;

    public ReloadCommand(OldDropPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage(Component.text("У вас нет прав для выполнения этой команды.", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(Component.text("Использование: ", NamedTextColor.GRAY)
                    .append(Component.text("/olddrop reload", NamedTextColor.WHITE)));
            return true;
        }

        plugin.getConfigManager().reload();

        sender.sendMessage(Component.text("Конфигурация OldDropItems успешно перезагружена.", NamedTextColor.GREEN));
        return true;
    }
}
