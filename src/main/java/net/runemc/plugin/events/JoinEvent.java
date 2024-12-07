package net.runemc.plugin.events;

import net.runemc.plugin.ranks.User;
import net.runemc.utils.command.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

@Event
public class JoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        User user = User.Companion.get(event.getPlayer().getName(), event.getPlayer().getUniqueId());
    }
}
