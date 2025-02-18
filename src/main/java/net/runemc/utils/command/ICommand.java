package net.runemc.utils.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ICommand {
    public abstract void execute(CommandSender sender, String[] args);

    private CommandSender sender;
    private Player player;

    private String[] names;
    private String[] permissions;
    private String[] args;

    public void sender(CommandSender sender) {
        this.sender = sender;
    }
    public CommandSender sender() {
        return this.sender;
    }

    public void player(Player player) {
        if (player != null) {
            this.player = player;
        }
    }
    public Player player() {
        return this.player;
    }

    public void names(String[] names) {
        this.names = names;
    }
    public void names(int i, String name) {
        this.names[i] = name;
    }
    public String[] names() {
        return this.names;
    }
    public String names(int i) {
        return this.names[i];
    }

    public void permissions(String[] perms) {
        this.permissions = perms;
    }
    public void permissions(int i, String perm) {
        this.permissions[i] = perm;
    }
    public String[] permissions() {
        return this.permissions;
    }
    public String permissions(int i) {
        return this.permissions[i];
    }

    public void args(String[] args) {
        this.args = args;
    }
    public void args(int i, String args) {
        this.args[i] = args;
    }
    public String[] args() {
        return this.args;
    }
    public String args(int i) {
        return this.args[i];
    }

    public boolean checkArgument(int i, String s) {
        if (i >= args.length) {
            sender.sendMessage("Invalid Arguments.");
            return false;
        }
        return args[i].equalsIgnoreCase(s);
    }

    public Player checkPlayer(Player player) {
        if (player == null) {
            sender.sendMessage("Invalid player.");
            return null;
        }
        return player;
    }
    public boolean isPlayer() {
        return player != null;
    }
}