package net.runemc.utils;

import net.runemc.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class UserUtils {
    public static UUID resolveUUID(String username) {
        Player player = Bukkit.getPlayerExact(username);
        if (player != null) {
            return player.getUniqueId();
        }

        UUID cachedUUID = Main.get().loadedUsers().entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(username))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return cachedUUID;
    }
}
