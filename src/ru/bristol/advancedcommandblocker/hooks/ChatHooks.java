package ru.bristol.advancedcommandblocker.hooks;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;
import ru.bristol.advancedcommandblocker.AdvancedCommandBlocker;
import ru.bristol.advancedcommandblocker.managers.BCommandManager;
import ru.bristol.advancedcommandblocker.managers.ConfigManager;
import ru.bristol.advancedcommandblocker.types.BType;
import ru.bristol.advancedcommandblocker.utils.Utils;
import ru.bristol.advancedcommandblocker.variables.BCommand;

import java.util.stream.Collectors;

public class ChatHooks implements Listener {

    private AdvancedCommandBlocker plugin;
    private ConfigManager configManager;
    private BCommandManager commandManager;

    public ChatHooks(AdvancedCommandBlocker plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.commandManager = plugin.getCommandManager();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String[] args = (String[]) ArrayUtils.remove(e.getMessage().toLowerCase().split(" "), 0);
        String cmd = e.getMessage().toLowerCase().split(" ")[0].replaceFirst("/", "");
        if(commandManager.isBlocked(cmd)) {
            Player p = e.getPlayer();
            BCommand command = commandManager.getCommand(cmd);
            if(command.getType().equals(BType.CONSOLE) || Utils.hasPermission(p, command.getPermission()) || (args.length >= 1 && !command.isBlockMultipleArgs())) return;
            e.setCancelled(true);
            Utils.sendMessage(p, command.getMessage());
            return;
        }

        if(cmd.contains(":") && configManager.isValue("block-commands-colon") && Utils.hasPermission(e.getPlayer(), configManager.getString("permission"))) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            Utils.sendMessage(p, configManager.getString("message"));
            return;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onConsoleCommand(ServerCommandEvent e) {
        String[] args = (String[]) ArrayUtils.remove(e.getCommand().split(" "), 0);
        String cmd = e.getCommand().split(" ")[0].replaceFirst("/", "");
        if(commandManager.isBlocked(cmd)) {
            BCommand command = commandManager.getCommand(cmd);
            if(command.getType().equals(BType.PLAYER) || !Utils.isVoid(command.getPermission()) || (args.length >= 1 && !command.isBlockMultipleArgs())) return;
            e.setCancelled(true);
            Utils.sendMessage(e.getSender(), command.getMessage());
            return;
        }

        if(cmd.contains(":") && configManager.isValue("block-commands-colon") && Utils.isVoid(configManager.getString("permission"))) {
            e.setCancelled(true);
            Utils.sendMessage(e.getSender(), configManager.getString("message"));
            return;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTabComplete(TabCompleteEvent e) {
        if(!configManager.isValue("enabled-tab-complete")) {
            e.setCancelled(true);
            return;
        }

        if(e.getSender() instanceof Player) {
            Player p = (Player) e.getSender();
            if(configManager.isValue("remove-complete-commands-colon") && !Utils.hasPermission(p, configManager.getString("permission"))) {
                if(e.getBuffer().contains(":")) e.setCancelled(true);
                e.setCompletions(e.getCompletions().stream()
                        .filter(str -> !str.contains(":"))
                        .collect(Collectors.toList()));
            }

            for(BCommand command : commandManager.getCommands().values()) {
               if(!command.getType().equals(BType.CONSOLE) && command.isRemoveTabComplete() && !Utils.hasPermission(p, command.getPermission())) {
                   if(e.getBuffer().startsWith("/" + command.getCommand())) e.setCancelled(true);
                   e.setCompletions(e.getCompletions().stream()
                           .filter(str -> !str.contains("/" + command.getCommand()))
                           .collect(Collectors.toList()));
               }
            }
            return;
        } else {
            if(configManager.isValue("remove-complete-commands-colon") && Utils.isVoid(configManager.getString("permission"))) {
                if(e.getBuffer().contains(":")) e.setCancelled(true);
                e.setCompletions(e.getCompletions().stream()
                        .filter(str -> !str.contains(":"))
                        .collect(Collectors.toList()));
            }

            for(BCommand command : commandManager.getCommands().values()) {
                if(!command.getType().equals(BType.PLAYER) && command.isRemoveTabComplete() && Utils.isVoid(command.getPermission())) {
                    if(e.getBuffer().startsWith(command.getCommand())) e.setCancelled(true);
                    e.setCompletions(e.getCompletions().stream()
                            .filter(str -> !str.startsWith(command.getCommand()))
                            .collect(Collectors.toList()));
                }
            }
            return;
        }
    }

}
