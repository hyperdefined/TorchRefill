/*
 * This file is part of TorchRefill.
 *
 * TorchRefill is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TorchRefill is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TorchRefill.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    public CommandTR(TorchRefill torchRefill) {
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
    public List < String > onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Arrays.asList("on", "off");
    }
}