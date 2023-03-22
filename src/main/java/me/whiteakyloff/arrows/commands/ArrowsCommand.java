package me.whiteakyloff.arrows.commands;

import lombok.var;
import lombok.AllArgsConstructor;

import me.whiteakyloff.arrows.AkyloffArrows;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ArrowsCommand implements CommandExecutor
{
    private AkyloffArrows javaPlugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("arrows.give")) {
            sender.sendMessage("no permission, clown.");
            return true;
        }
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage("incorrect args.");
            return true;
        }
        var player = Bukkit.getPlayer(args[0]);
        var customArrow = this.javaPlugin.getArrowsManager().getArrow(args[1]);

        if (customArrow == null) {
            sender.sendMessage("arrow is null.");
            return true;
        }
        this.giveItem(player, customArrow.getItemStack());
        sender.sendMessage("arrow was gave to " + player.getName());
        return true;
    }

    private void giveItem(Player player, ItemStack itemStack) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }
}
