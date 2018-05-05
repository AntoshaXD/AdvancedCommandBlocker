package ru.bristol.advancedcommandblocker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.bristol.advancedcommandblocker.hooks.ChatHooks;
import ru.bristol.advancedcommandblocker.managers.BCommandManager;
import ru.bristol.advancedcommandblocker.managers.ConfigManager;

public class AdvancedCommandBlocker extends JavaPlugin {

    private ConfigManager configManager;
    private BCommandManager commandManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        commandManager = new BCommandManager(this);

        configManager.load();
        commandManager.load();

        Bukkit.getPluginManager().registerEvents(new ChatHooks(this), this);
    }

    @Override
    public void onDisable() {}

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BCommandManager getCommandManager() {
        return commandManager;
    }

}
