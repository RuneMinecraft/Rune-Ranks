package net.runemc.plugin.ranks;

import net.runemc.plugin.Main;
import net.runemc.utils.Config;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.io.File;

@NotNull
public final class Track {
    private final @NotNull File file;

    private final @NotNull String name;
    private final @NotNull List<Group> groups = new ArrayList<>();

    private Track(@NotNull File file, @NotNull String name) {
        this.file = file;
        this.name = name;
    }

    public static Track create(@NotNull String name) throws IOException {
        File file = new File("config/tracks/" + name.toLowerCase() + ".yml");
        if (file.exists()) {
            throw new IllegalStateException("Track already exists: " + name);
        }

        Track track = new Track(file, name);
        track.save();
        return track;
    }

    public static Track get(@NotNull String name) throws IOException {
        File file = new File(Main.get().getDataFolder() + "/tracks/" + name.toLowerCase() + ".yml");
        return get(file);
    }
    public static Track get(@NotNull File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getPath());
        }

        var map = Config.load(file, Object.class);
        if (!(map instanceof java.util.Map<?, ?> yamlData)) {
            throw new IOException("Invalid YAML structure in: " + file.getName());
        }

        String name = (String) yamlData.get("name");
        Track track = new Track(file, name);
        List<String> groups = (List<String>) yamlData.getOrDefault("groups", null);
        if (groups == null) {
            groups = new ArrayList<>();
        }
        for (String groupName : groups) {
            track.groups.add(Group.get(groupName));
        }
        return track;
    }

    public void addGroup(@NotNull Group group) throws IOException {
        if (!groups.contains(group)) {
            groups.add(group);
            save();
        }
    }
    public void removeGroup(@NotNull String group) throws IOException {
        if (groups.remove(group)) {
            save();
        }
    }
    public void clearGroups() throws IOException {
        groups.clear();
        save();
    }

    public @NotNull File file() {
        return file;
    }
    public @NotNull String name() {
        return name;
    }
    public @NotNull List<Group> groups() {
        return new ArrayList<>(groups);
    }

    private void save() throws IOException {
        var yamlData = new java.util.LinkedHashMap<String, Object>();
        yamlData.put("name", name);
        yamlData.put("groups", new ArrayList<>(groups));

        Config.save(file, yamlData);
    }
}