package net.runemc.plugin;

import net.runemc.plugin.command.RanksCommand;
import net.runemc.plugin.events.ChatEvent;
import net.runemc.plugin.events.JoinEvent;
import net.runemc.plugin.ranks.*;
import net.runemc.utils.Config;
import net.runemc.utils.command.Register;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private final Map<UUID, String> loadedUsers = new LinkedHashMap<>();
    public Map<UUID, String> loadedUsers() {
        return this.loadedUsers;
    }

    private static Main INSTANCE;
    public static Main get() {
        return Main.INSTANCE;
    }

    @Override
    public void onEnable() {
        this.setNaggable(false);
        Main.INSTANCE = this;

        File usersFolder = new File(this.getDataFolder(), "users");
        if (!usersFolder.exists() && !usersFolder.mkdirs()) {
            getLogger().severe("Failed to create users folder!");
            return;
        }

        for (File userFile : Objects.requireNonNull(usersFolder.listFiles((dir, name) -> name.endsWith(".yml")))) {
            try {
                User user = User.Companion.get(userFile);
                loadedUsers.put(user.getUuid(), user.getUsername());
                getLogger().info("Loaded user: " + user.getUsername());
            } catch (IOException e) {
                getLogger().severe("Failed to load user file: " + userFile.getName());
                e.printStackTrace();
            }
        }

        Register register = Register.get();
        register.autoRegisterCommands();
        register.autoRegisterListeners();

        Objects.requireNonNull(getCommand("ranks")).setExecutor(new RanksCommand());
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
    }

    @Override
    public void onDisable() {
        for (UUID uuid : loadedUsers.keySet()) {
            try {
                String username = loadedUsers.get(uuid);
                User user = User.Companion.get(username, uuid);
                user.save();
                getLogger().info("Saved user: " + username);
            } catch (IOException e) {
                getLogger().severe("Failed to save user with UUID: " + uuid);
                e.printStackTrace();
            }
        }

        try {
            Config.save(new File(this.getDataFolder(), "players.yml"), loadedUsers);
        } catch (IOException e) {
            getLogger().severe("Failed to save players.yml!");
            throw new RuntimeException(e);
        }
    }
}
