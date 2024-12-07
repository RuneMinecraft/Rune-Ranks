package net.runemc.plugin.command.subcommands;

import net.runemc.plugin.ranks.Group;
import net.runemc.plugin.ranks.Track;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class TrackCommand {
    public static void execute(CommandSender sender, String[] args) throws IOException {
        if (args.length < 5) {
            sender.sendMessage("Invalid track command usage. Type '/ranks help' for help.");
            return;
        }

        String trackName = args[1];
        String action = args[2].toLowerCase();
        String target = args[3].toLowerCase();
        String value = args[4];

        Track track = Track.Companion.get(trackName);

        if (action.equals("group")) {
            switch (target) {
                case "add" -> {
                    track.addGroup(Group.Companion.get(value));
                    sender.sendMessage("Added group " + value + " to track " + trackName);
                }
                case "remove" -> {
                    track.removeGroup(Group.Companion.get(value));
                    sender.sendMessage("Removed group " + value + " from track " + trackName);
                }
                default -> sender.sendMessage("Unknown group action. Use add/remove.");
            }
        } else {
            sender.sendMessage("Unknown track action. Use group.");
        }
    }
}
