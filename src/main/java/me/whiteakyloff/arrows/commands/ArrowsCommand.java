package me.whiteakyloff.arrows.commands;

import lombok.var;
import lombok.AllArgsConstructor;

import me.whiteakyloff.arrows.AkyloffArrows;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ArrowsCommand implements CommandExecutor
{
    private AkyloffArrows javaPlugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("null args");
            return true;
        }
        var arrow = this.javaPlugin.getArrowsManager().getArrow(args[0]);

        if (arrow == null) {
            sender.sendMessage("arrow is null");
            return true;
        }
        ((Player) sender).getInventory().addItem(arrow.getItemStack());
        sender.sendMessage("gave to you.");
        return true;
    }
}
