package net.runemc.plugin.command.subcommands;

import net.runemc.plugin.ranks.Track;
import net.runemc.plugin.ranks.User;
import net.runemc.utils.UserUtils;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class DemoteCommand {
    public static void execute(CommandSender sender, String[] args) throws IOException {
        if (args.length < 3) {
            sender.sendMessage("Invalid demote command usage. Use '/ranks demote [USER] [TRACK]'.");
            return;
        }

        String username = args[1];
        String trackName = args[2];

        User user = User.Companion.get(username, UserUtils.resolveUUID(username));
        Track track = Track.Companion.get(trackName);

        user.getTracks().put(track, user.getTracks().get(track)-1);
        sender.sendMessage("Demoted user " + username + " in track " + trackName);
    }
}
