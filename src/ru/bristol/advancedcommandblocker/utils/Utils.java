package ru.bristol.advancedcommandblocker.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {

    public static boolean isVoid(String msg) {
        return msg.equals("");
    }

    public static boolean hasPermission(Player p, String permission) {
        if(isVoid(permission)) return false;
        return p.hasPermission(permission);
    }

    public static void sendMessage(Player p, String message) {
        if(isVoid(message)) return;
        p.sendMessage(message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        if(isVoid(message)) return;
        sender.sendMessage(message);
    }

}
