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
            this.javaPlugin.getMessages().get("no-permission").sendMessage(sender);
            return true;
        }
        if (args.length != 2) {
            this.javaPlugin.getMessages().get("incorrect-args").sendMessage(sender);
            return true;
        }
        var player = Bukkit.getPlayer(args[1]);
        var customArrow = this.javaPlugin.getArrowsManager().getArrow(args[0]);

        if (player == null) {
            this.javaPlugin.getMessages().get("null-args.player").sendMessage(sender);
            return true;
        }
        if (customArrow == null) {
            this.javaPlugin.getMessages().get("null-args.custom-arrow").sendMessage(sender);
            return true;
        }
        this.giveItem(player, customArrow.getItemStack());
        this.javaPlugin.getMessages().get("successfully").sendMessage(sender, string -> string.replace("{player}", player.getName()));
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
