package net.runemc.plugin.events;

import net.runemc.plugin.ranks.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class ChatEvent implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) throws IOException {
        User user = User.Companion.get(e.getPlayer().getName(), e.getPlayer().getUniqueId());
        e.setFormat(user.getGroups().getLast().getPrefix()+" "+ e.getPlayer().getName()+": "+e.getMessage());
    }
}
