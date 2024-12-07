package net.runemc.plugin.ranks;

import net.runemc.plugin.Main;
import net.runemc.utils.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

@NotNull
public final class User {
    private final @NotNull File file;

    private final @NotNull String username;
    private final @NotNull UUID uuid;

    private final @NotNull List<Group> groups = new ArrayList<>();
    private final @NotNull Map<Track, Integer> tracks = new LinkedHashMap<>();
    private final @NotNull List<String> permissions = new ArrayList<>();

    private User(@NotNull File file, @NotNull String username, @NotNull UUID uuid) {
        this.file = file;
        this.username = username;
        this.uuid = uuid;
    }

    public static User create(@NotNull String username, @NotNull UUID uuid) throws IOException {
        File file = new File(Main.get().getDataFolder() + "/users/" + uuid + ".yml");
        if (file.exists()) {
            throw new IllegalStateException("User already exists: " + username);
        }

        User user = new User(file, username, uuid);
        user.save();
        return user;
    }

    public static User get(@NotNull String username, @NotNull UUID uuid) throws IOException {
        File file = new File(Main.get().getDataFolder() + "/users/" + uuid + ".yml");
        if (!file.exists()) {
            return create(username, uuid);
        }
        return get(file);
    }
    public static User get(@NotNull File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getPath());
        }

        var map = Config.load(file, Object.class);
        if (!(map instanceof java.util.Map<?, ?> yamlData)) {
            throw new IOException("Invalid YAML structure in: " + file.getName());
        }

        String username = (String) yamlData.get("username");
        String uuid = (String) yamlData.get("uuid");
        User user = new User(file, username, UUID.fromString(uuid));

        List<String> groupNames = (List<String>) yamlData.getOrDefault("groups", null);
        if (groupNames == null) {
            groupNames = new ArrayList<>();
        }
        for (String groupName : groupNames) {
            user.groups.add(Group.get(groupName));
        }
        List<List<String>> trackEntries = (List<List<String>>) yamlData.getOrDefault("tracks", null);
        if (trackEntries == null) {
            trackEntries = new ArrayList<>();
        }
        for (List<String> entry : trackEntries) {
            String trackName = entry.get(0);
            int position = Integer.parseInt(entry.get(1));
            user.tracks.put(Track.get(trackName), position);
        }

        user.permissions.addAll((List<String>) yamlData.getOrDefault("permissions", null));
        return user;
    }

    public void addGroup(@NotNull Group group) throws IOException {
        if (!groups.contains(group)) {
            groups.add(group);
            save();
        }
    }
    public void removeGroup(@NotNull Group group) throws IOException {
        if (groups.remove(group)) {
            save();
        }
    }
    public void clearGroups() throws IOException {
        groups.clear();
        save();
    }

    public void addTrack(@NotNull Track track) throws IOException {
        if (!tracks.keySet().contains(track)) {
            tracks.put(track, tracks.size());
            save();
        }
    }
    public void removeTrack(@NotNull Track track) throws IOException {
        tracks.remove(track);
        save();
    }
    public void clearTrack() throws IOException {
        groups.clear();
        save();
    }

    public void addPermission(@NotNull String permission) throws IOException {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
            save();
        }
    }
    public void removePermission(@NotNull String permission) throws IOException {
        if (permissions.remove(permission)) {
            save();
        }
    }
    public void clearPermissions() throws IOException {
        permissions.clear();
        save();
    }

    public @NotNull File file() {
        return file;
    }
    public @NotNull String username() {
        return username;
    }
    public @NotNull UUID uuid() {
        return uuid;
    }
    public @NotNull List<Group> groups() {
        return new ArrayList<>(groups);
    }
    public @NotNull Map<Track, Integer> tracks() {
        return new LinkedHashMap<>(tracks);
    }
    public @NotNull List<String> permissions() {
        return new ArrayList<>(permissions);
    }

    public void save() throws IOException {
        var yamlData = new LinkedHashMap<String, Object>();
        yamlData.put("username", username);
        yamlData.put("uuid", uuid.toString());
        yamlData.put("groups", groups.stream().map(Group::name).toList());
        yamlData.put("tracks", tracks.entrySet().stream()
                .map(entry -> List.of(entry.getKey().name(), entry.getValue().toString()))
                .toList());
        yamlData.put("permissions", new ArrayList<>(permissions));

        Config.save(file, yamlData);
    }
}
