package me.whiteakyloff.arrows;

import lombok.Getter;

import me.whiteakyloff.arrows.commands.ArrowsCommand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AkyloffArrows extends JavaPlugin
{
    @Getter
    private ArrowsManager arrowsManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.arrowsManager = new ArrowsManager(this);
        this.getCommand("arrows").setExecutor(new ArrowsCommand(this));

        Bukkit.getPluginManager().registerEvents(new ArrowsListener(this), this);
    }

    @Override
    public void onDisable() {
        this.arrowsManager.disableManager();
    }
}
