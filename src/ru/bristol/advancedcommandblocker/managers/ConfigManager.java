package ru.bristol.advancedcommandblocker.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import ru.bristol.advancedcommandblocker.AdvancedCommandBlocker;

import java.util.Map;
import java.util.HashMap;

public class ConfigManager {

    private AdvancedCommandBlocker plugin;
    private FileConfiguration config;
    private Map<String, Object> settings = new HashMap<>();

    public ConfigManager(AdvancedCommandBlocker plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        for(String setting : config.getConfigurationSection("settings").getKeys(false)) {
            if(!setting.equals("message")) {
                settings.put(setting, config.get("settings." + setting));
            } else {
                settings.put(setting, ChatColor.translateAlternateColorCodes('&', config.getString("settings." + setting)));
            }
        }
    }

    public boolean isValue(String path) {
        return Boolean.valueOf(settings.get(path).toString());
    }

    public String getString(String path) {
        return String.valueOf(settings.get(path));
    }

    public FileConfiguration getConfig() {
        return config;
    }

}
