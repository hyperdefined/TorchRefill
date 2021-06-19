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

import lol.hyper.torchrefill.commands.CommandTR;
import lol.hyper.torchrefill.events.BlockPlace;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public final class TorchRefill extends JavaPlugin implements Listener {

    public final File configFile = new File(getDataFolder(), "config.yml");
    public final ArrayList<UUID> turnedOff = new ArrayList<>();
    public FileConfiguration config;
    public CommandTR commandTR;
    public BlockPlace blockPlace;

    @Override
    public void onEnable() {
        commandTR = new CommandTR(this);
        blockPlace = new BlockPlace(this);
        this.getCommand("tr").setExecutor(commandTR);
        loadConfig(configFile);
        Bukkit.getServer().getPluginManager().registerEvents(blockPlace, this);
        Metrics metrics = new Metrics(this, 9391);
    }

    public void loadConfig(File file) {
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
}
