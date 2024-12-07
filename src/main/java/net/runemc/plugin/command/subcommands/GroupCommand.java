package net.runemc.plugin.command.subcommands;

import net.runemc.plugin.ranks.Group;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class GroupCommand {
    public static void execute(CommandSender sender, String[] args) throws IOException {
        if (args.length < 5) {
            sender.sendMessage("Invalid group command usage. Type '/ranks help' for help.");
            return;
        }

        String groupName = args[1];
        String action = args[2].toLowerCase();
        String target = args[3].toLowerCase();
        String value = args[4];

        Group group = Group.Companion.get(groupName);

        switch (action) {
            case "name" -> {
                group.setName(value);
                sender.sendMessage("Set name of group " + groupName + " to " + value);
            }
            case "prefix" -> {
                group.setPrefix(value);
                sender.sendMessage("Set prefix of group " + groupName + " to " + value);
            }
            case "suffix" -> {
                group.setSuffix(value);
                sender.sendMessage("Set suffix of group " + groupName + " to " + value);
            }
            case "inherited-groups" -> {
                switch (target) {
                    case "add" -> {
                        group.addInheritedGroup(value);
                        sender.sendMessage("Added inherited group " + value + " to group " + groupName);
                    }
                    case "remove" -> {
                        group.removeInheritedGroup(value);
                        sender.sendMessage("Removed inherited group " + value + " from group " + groupName);
                    }
                    default -> sender.sendMessage("Unknown inherited-groups action. Use add/remove.");
                }
            }
            case "permissions" -> {
                switch (target) {
                    case "add" -> {
                        group.addPermission(value);
                        sender.sendMessage("Added permission " + value + " to group " + groupName);
                    }
                    case "remove" -> {
                        group.removePermission(value);
                        sender.sendMessage("Removed permission " + value + " from group " + groupName);
                    }
                    default -> sender.sendMessage("Unknown permissions action. Use add/remove.");
                }
            }
            default -> sender.sendMessage("Unknown group action. Use name/prefix/suffix/inherited-groups/permissions.");
        }
    }
}
