package me.whiteakyloff.arrows;

import lombok.Getter;

import me.whiteakyloff.arrows.commands.ArrowsCommand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.winlocker.utils.messages.Messages;
import ru.winlocker.utils.config.annotations.*;
import ru.winlocker.utils.config.ConfigManager;

@ConfigMappable
public class AkyloffArrows extends JavaPlugin
{
    @Getter
    @ConfigName("messages")
    private Messages messages;

    @Getter
    private ArrowsManager arrowsManager;

    @Override
    public void onLoad() {
        ConfigManager.create(this, "config.yml").target(this).load();
    }

    @Override
    public void onEnable() {
        this.arrowsManager = new ArrowsManager(this);
        this.getCommand("arrows").setExecutor(new ArrowsCommand(this));

        Bukkit.getPluginManager().registerEvents(new ArrowsListener(this), this);
    }

    @Override
    public void onDisable() {
        this.arrowsManager.disableManager();
    }
}
