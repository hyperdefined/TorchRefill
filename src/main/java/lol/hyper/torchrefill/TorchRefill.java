package lol.hyper.torchrefill;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TorchRefill extends JavaPlugin implements Listener {

    public FileConfiguration config;
    public final File configFile = new File(getDataFolder(), "config.yml");

    @Override
    public void onEnable() {
        loadConfig(configFile);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig(File file) {
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block placed = event.getBlockPlaced();

        // Makes sure the player is placing a torch.
        if (placed.getType() == Material.TORCH) {
            // Cool, they are placing a torch. Get their current hotbar slot.
            PlayerInventory inv = event.getPlayer().getInventory();
            int heldItemIndex = inv.getHeldItemSlot();

            // We check if you are holding a torch in your main hand.
            // This means you placed it with your offhand.
            if (inv.getItemInMainHand().getType() != Material.TORCH) {
                // Get the torches from the offhand.
                ItemStack offhandTorch = inv.getItemInOffHand();
                // If they are out, then start to replace them.
                if (offhandTorch.getAmount() == 1) {
                    // I run this task later to allow time for the player to place the torch down.
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        int torchIndex = 0;
                        // This will loop through the player's inventory and get where the next torch is.
                        for (int i = 0; i < inv.getContents().length; i++) {
                            ItemStack currentItem = inv.getContents()[i];
                            if (currentItem != null) {
                                if (currentItem.getType() == Material.TORCH) {
                                    torchIndex = i;
                                    break;
                                }
                            }
                        }
                        // Get torches from their inventory.
                        ItemStack oldTorches = inv.getItem(torchIndex);
                        // Set their hotbar selection to the torches from their inventory.
                        inv.setItemInOffHand(oldTorches);
                        // Set their old torch slow to air so it removes them.
                        // We can't do remove() because that removes ALL ItemStacks.
                        inv.setItem(torchIndex, new ItemStack(Material.AIR));
                        if (config.getBoolean("play-sound")) {
                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.7F, 1.0F);
                        }
                        String message = ChatColor.translateAlternateColorCodes('&', config.getString("hotbar-message"));
                        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }, 1);
                }
            } else {
                // This is if the torch is on their main hotbar.
                // If they are out, then start to replace them.
                ItemStack mainHandTorch = inv.getItemInMainHand();
                if (mainHandTorch.getAmount() == 1) {
                    // I run this task later to allow time for the player to place the torch down.
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        // This will loop through the player's inventory and get where the next torch is.
                        int torchIndex = 0;
                        for (int i = 0; i < inv.getContents().length; i++) {
                            ItemStack currentItem = inv.getContents()[i];
                            if (currentItem != null) {
                                if (currentItem.getType() == Material.TORCH) {
                                    torchIndex = i;
                                    break;
                                }
                            }
                        }
                        // Get torches from their inventory.
                        ItemStack oldTorches = inv.getItem(torchIndex);
                        // Set their hotbar selection to the torches from their inventory.
                        inv.setItem(heldItemIndex, oldTorches);
                        // Set their old torch slow to air so it removes them.
                        // We can't do remove() because that removes ALL ItemStacks.
                        inv.setItem(torchIndex, new ItemStack(Material.AIR));
                        if (config.getBoolean("play-sound")) {
                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.7F, 1.0F);
                        }
                        String message = ChatColor.translateAlternateColorCodes('&', config.getString("hotbar-message"));
                        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }, 1);
                }
            }
        }
    }
}
