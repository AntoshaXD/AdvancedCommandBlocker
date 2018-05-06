package ru.bristol.advancedcommandblocker.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import ru.bristol.advancedcommandblocker.AdvancedCommandBlocker;
import ru.bristol.advancedcommandblocker.types.BType;
import ru.bristol.advancedcommandblocker.variables.BCommand;

import java.util.Map;
import java.util.HashMap;

public class BCommandManager {

    private AdvancedCommandBlocker plugin;
    private ConfigManager configManager;
    private Map<String, BCommand> commands = new HashMap<>();

    public BCommandManager(AdvancedCommandBlocker plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public void load() {
        FileConfiguration config = configManager.getConfig();
        for(String cmd : config.getConfigurationSection("commands").getKeys(false)) {
            String startPath = "commands." + cmd + ".";
            BCommand command = new BCommand(cmd, BType.valueOf(config.getString(startPath + "type")), config.getStringList(startPath + "aliases"), config.getBoolean(startPath + "remove-complete"), config.getBoolean(startPath + "block-multiple-args"), ChatColor.translateAlternateColorCodes('&', config.getString(startPath + "message")), config.getString(startPath + "permission"));
            commands.put(cmd, command);
        }
    }

    public boolean isBlocked(String cmd) {
        for(BCommand command : commands.values()) {
            if(command.getCommand().equals(cmd) || command.getAliases().contains(cmd)) return true;
        }
        return false;
    }

    public BCommand getCommand(String cmd) {
        for(BCommand command : commands.values()) {
            if(command.getCommand().equals(cmd) || command.getAliases().contains(cmd)) return command;
        }
        return null;
    }

    public Map<String, BCommand> getCommands() {
        return commands;
    }

}
