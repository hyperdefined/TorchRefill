package lol.hyper.torchrefill;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.List;

public class CommandTR implements TabExecutor {

    private final TorchRefill torchRefill;

    public CommandTR (TorchRefill torchRefill) {
        this.torchRefill = torchRefill;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "TorchRefill version " + torchRefill.getDescription().getVersion() + ". Created by hyperdefined.");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(ChatColor.RED + "You must be a player for this!");
                } else {
                    torchRefill.turnedOff.removeIf(Bukkit.getPlayerExact(sender.getName()).getUniqueId()::equals);
                    sender.sendMessage(ChatColor.GREEN + "Torches will refill!");
                }
            } else if (args[0].equalsIgnoreCase("off")) {
                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(ChatColor.RED + "You must be a player for this!");
                } else {
                    if (!torchRefill.turnedOff.contains(Bukkit.getPlayerExact(sender.getName()).getUniqueId())) {
                        torchRefill.turnedOff.add(Bukkit.getPlayerExact(sender.getName()).getUniqueId());
                    }
                    sender.sendMessage(ChatColor.GREEN + "Torches will not refill!");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Arrays.asList("on", "off");
    }
}
