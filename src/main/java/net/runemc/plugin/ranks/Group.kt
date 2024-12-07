package net.runemc.plugin.ranks;

import net.runemc.plugin.Main;
import net.runemc.utils.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@NotNull
public final class Group {
    private final @NotNull File file;

    private @NotNull String name;
    private @NotNull String prefix;
    private @Nullable String suffix;
    private int weight;

    private final @NotNull List<Group> inheritedGroups = new ArrayList<>();
    private final @NotNull List<String> permissions = new ArrayList<>();

    private Group(@NotNull File file, @NotNull String name, @NotNull String prefix, @Nullable String suffix, int weight) {
        this.file = file;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.weight = weight;
    }

    public static Group create(@NotNull String name, @NotNull String prefix, @Nullable String suffix, int weight) throws IOException {
        File file = new File("config/groups/" + name.toLowerCase() + ".yml");
        if (file.exists()) {
            throw new IllegalStateException("Group already exists: " + name);
        }

        Group group = new Group(file, name, prefix, suffix, weight);
        group.save();
        return group;
    }
    public static Group create(@NotNull String name, @NotNull String prefix, int weight) throws IOException {
        return create(name, prefix, "", weight);
    }

    public static Group get(@NotNull String name) throws IOException {
        File file = new File(Main.get().getDataFolder()+"/groups/" + name.toLowerCase() + ".yml");
        return get(file);
    }
    public static Group get(@NotNull File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getPath());
        }

        var map = Config.load(file, Object.class);
        if (!(map instanceof java.util.Map<?, ?> yamlData)) {
            throw new IOException("Invalid YAML structure in: " + file.getName());
        }

        String name = (String) yamlData.get("name");
        String prefix = (String) yamlData.get("prefix");
        String suffix = (String) yamlData.get("suffix");
        int weight = (int) yamlData.get("weight");

        Group group = new Group(file, name, prefix, suffix, weight);
        List<String> groups = (List<String>) yamlData.getOrDefault("inherited-groups", null);
        if (groups == null) {
            groups = new ArrayList<>();
        }
        for (String groupName : groups) {
            group.inheritedGroups.add(Group.get(groupName));
        }

        group.permissions.addAll((List<String>) yamlData.getOrDefault("permissions", null));

        return group;
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

    public void addInheritedGroup(@NotNull String name) throws IOException {
        Group inherited = Group.get(name);
        if (!inheritedGroups.contains(inherited)) {
            inheritedGroups.add(inherited);
            save();
        }
    }
    public void removeInheritedGroup(@NotNull String name) throws IOException {
        inheritedGroups.removeIf(group -> group.name().equalsIgnoreCase(name));
        save();
    }
    public void clearInheritedGroups() throws IOException {
        inheritedGroups.clear();
        save();
    }

    public void setName(@NotNull String name) throws IOException {
        this.name = name;
        save();
    }
    public void setPrefix(@NotNull String prefix) throws IOException {
        this.prefix = prefix;
        save();
    }
    public void setSuffix(@Nullable String suffix) throws IOException {
        this.suffix = suffix;
        save();
    }
    public void setWeight(int weight) throws IOException {
        this.weight = weight;
        save();
    }

    public @NotNull File file() {
        return this.file;
    }
    public @NotNull String name() {
        return this.name;
    }
    public @NotNull String prefix() {
        return this.prefix;
    }
    public @Nullable String suffix() {
        return this.suffix;
    }
    public int weight() {
        return this.weight;
    }
    public @NotNull List<Group> inheritedGroups() {
        return this.inheritedGroups;
    }
    public @NotNull List<String> permissions() {
        return this.permissions;
    }

    private void save() throws IOException {
        var yamlData = new java.util.LinkedHashMap<String, Object>();
        yamlData.put("name", name);
        yamlData.put("prefix", prefix);
        yamlData.put("suffix", suffix);
        yamlData.put("weight", weight);
        yamlData.put("inherit-groups", new ArrayList<>(inheritedGroups));
        yamlData.put("permissions", new ArrayList<>(permissions));

        Config.save(file, yamlData);
    }
}