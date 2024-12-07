package net.runemc.plugin.command.subcommands;

import net.runemc.plugin.ranks.*;
import net.runemc.utils.UserUtils;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.UUID;

public class UserCommand {
    public static void execute(CommandSender sender, String[] args) throws IOException {
        if (args.length < 5) {
            sender.sendMessage("Invalid user command usage. Type '/ranks help' for help.");
            return;
        }

        String username = args[1];
        String action = args[2].toLowerCase();
        String target = args[3].toLowerCase();
        String value = args[4];

        UUID uuid = UserUtils.resolveUUID(username);
        if (uuid == null) {
            sender.sendMessage("Player '" + username + "' could not be found.");
            return;
        }

        User user;
        try {
            user = User.get(username, uuid);
        } catch (IOException e) {
            sender.sendMessage("An error occurred while fetching the user data for '" + username + "'.");
            e.printStackTrace();
            return;
        }

        switch (action) {
            case "group" -> {
                switch (target) {
                    case "set" -> {
                        user.clearGroups();
                        user.addGroup(Group.get(value));
                        sender.sendMessage("Set group for user " + username + " to " + value);
                    }
                    case "add" -> {
                        user.addGroup(Group.get(value));
                        sender.sendMessage("Added group " + value + " to user " + username);
                    }
                    case "remove" -> {
                        user.removeGroup(Group.get(value));
                        sender.sendMessage("Removed group " + value + " from user " + username);
                    }
                    default -> sender.sendMessage("Unknown group action. Use set/add/remove.");
                }
            }
            case "track" -> {
                switch (target) {
                    case "set" -> {
                        user.addTrack(Track.get(value));
                        sender.sendMessage("Set track for user " + username + " to " + value);
                    }
                    case "remove" -> {
                        user.removeTrack(Track.get(value));
                        sender.sendMessage("Removed track for user " + username);
                    }
                    default -> sender.sendMessage("Unknown track action. Use set/remove.");
                }
            }
            case "permissions" -> {
                switch (target) {
                    case "add" -> {
                        user.addPermission(value);
                        sender.sendMessage("Added permission " + value + " to user " + username);
                    }
                    case "remove" -> {
                        user.removePermission(value);
                        sender.sendMessage("Removed permission " + value + " from user " + username);
                    }
                    default -> sender.sendMessage("Unknown permission action. Use add/remove.");
                }
            }
            default -> sender.sendMessage("Unknown user action. Use group/track/permissions.");
        }
    }
}
