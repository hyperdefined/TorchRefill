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

package lol.hyper.torchrefill.commands;

import lol.hyper.torchrefill.TorchRefill;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CommandTR implements TabExecutor {

    private final TorchRefill torchRefill;

    public CommandTR(TorchRefill torchRefill) {
        this.torchRefill = torchRefill;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            torchRefill.getAdventure().sender(sender).sendMessage(Component.text("TorchRefill version " + torchRefill.getDescription().getVersion() + ". Created by hyperdefined.").color(NamedTextColor.GREEN));
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            torchRefill.getAdventure().sender(sender).sendMessage(torchRefill.getMessage("errors.must-be-player"));
            return true;
        }

        Player player = (Player) sender;
        switch (args[0]) {
            case "on": {
                torchRefill.turnedOff.removeIf(player.getUniqueId()::equals);
                torchRefill.getAdventure().player(player).sendMessage(torchRefill.getMessage("messages.refill-on"));
                return true;
            }
            case "off": {
                if (!torchRefill.turnedOff.contains(player.getUniqueId())) {
                    torchRefill.turnedOff.add(player.getUniqueId());
                }
                torchRefill.getAdventure().player(player).sendMessage(torchRefill.getMessage("messages.refill-off"));
                return true;
            }
            default: {
                torchRefill.getAdventure().player(player).sendMessage(Component.text("Use /tr on/off to enable/disable torch refilling.").color(NamedTextColor.GREEN));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Arrays.asList("on", "off");
    }
}
