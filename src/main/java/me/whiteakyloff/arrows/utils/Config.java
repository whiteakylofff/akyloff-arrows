package me.whiteakyloff.arrows.utils;

import lombok.var;

import java.io.*;

import me.whiteakyloff.arrows.utils.protocolapi.Sphere;
import org.bukkit.plugin.java.*;
import org.bukkit.configuration.file.*;

public class Config
{
    private static JavaPlugin instance;

    static {
        Config.instance = JavaPlugin.getProvidingPlugin(Config.class);
    }

    public static FileConfiguration getFile(String fileName) {
        var file = new File(Config.instance.getDataFolder(), fileName);
        if (Config.instance.getResource(fileName) == null) {
            return save(YamlConfiguration.loadConfiguration(file), fileName);
        }
        if (!file.exists()) {
            Config.instance.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration save(FileConfiguration config, String fileName) {
        try {
            config.save(new File(Config.instance.getDataFolder(), fileName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}
