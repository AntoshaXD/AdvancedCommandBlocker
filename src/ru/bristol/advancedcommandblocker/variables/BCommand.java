package ru.bristol.advancedcommandblocker.variables;

import ru.bristol.advancedcommandblocker.types.BType;

import java.util.ArrayList;
import java.util.List;

public class BCommand {

    private String command;
    private BType type;
    private List<String> aliases = new ArrayList<>();
    private boolean complete;
    private boolean block_multiple_args;
    private String message;
    private String permission;

    public BCommand(String command, BType type, List<String> aliases, boolean complete, boolean block_multiple_args, String message, String permission) {
        this.command = command;
        this.type = type;
        this.aliases = aliases;
        this.complete = complete;
        this.block_multiple_args = block_multiple_args;
        this.message = message;
        this.permission = permission;
    }

    public String getCommand() {
        return command;
    }

    public BType getType() {
        return type;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isRemoveTabComplete() {
        return complete;
    }

    public boolean isBlockMultipleArgs() {
        return block_multiple_args;
    }

    public String getMessage() {
        return message;
    }

    public String getPermission() {
        return permission;
    }

}
