package net.runemc.plugin.ranks

import net.runemc.plugin.Main
import net.runemc.utils.Config
import java.io.File
import java.io.IOException

data class Group(
    private val file: File,
    var name: String,
    var prefix: String,
    var suffix: String?,
    var weight: Int,
    val inheritedGroups: MutableList<Group> = mutableListOf(),
    val permissions: MutableList<String> = mutableListOf()
) {
    companion object {
        @Throws(IOException::class) fun create(name: String, prefix: String, suffix: String? = "", weight: Int): Group {
            val file = File("config/groups/${name.lowercase()}.yml")
            if (file.exists()) {
                throw IllegalStateException("Group already exists: $name")
            }

            val group = Group(file, name, prefix, suffix, weight)
            group.save()
            return group
        }

        @Throws(IOException::class) fun get(name: String): Group {
            val file = File(Main.get().dataFolder, "groups/${name.lowercase()}.yml")
            return get(file)
        }
        @Throws(IOException::class) fun get(file: File): Group {
            if (!file.exists()) {
                throw IOException("File does not exist: ${file.path}")
            }

            val yamlData = Config.load(file, Any::class.java) as? Map<*, *>
                ?: throw IOException("Invalid YAML structure in: ${file.name}")

            val name = yamlData["name"] as String
            val prefix = yamlData["prefix"] as String
            val suffix = yamlData["suffix"] as? String
            val weight = yamlData["weight"] as Int

            val group = Group(file, name, prefix, suffix, weight)

            val inheritedGroupNames = yamlData["inherited-groups"] as? List<String> ?: emptyList()
            for (groupName in inheritedGroupNames) {
                group.inheritedGroups.add(Group.get(groupName))
            }

            group.permissions.addAll(yamlData["permissions"] as? List<String> ?: emptyList())
            return group
        }
    }

    @Throws(IOException::class) fun addPermission(permission: String) {
        if (!permissions.contains(permission)) {
            permissions.add(permission)
            save()
        }
    }
    @Throws(IOException::class) fun removePermission(permission: String) {
        if (permissions.remove(permission)) {
            save()
        }
    }
    @Throws(IOException::class) fun clearPermissions() {
        permissions.clear()
        save()
    }

    @Throws(IOException::class) fun addInheritedGroup(name: String) {
        val inherited = Group.get(name)
        if (!inheritedGroups.contains(inherited)) {
            inheritedGroups.add(inherited)
            save()
        }
    }
    @Throws(IOException::class) fun removeInheritedGroup(name: String) {
        inheritedGroups.removeIf { it.name.equals(name, ignoreCase = true) }
        save()
    }
    @Throws(IOException::class) fun clearInheritedGroups() {
        inheritedGroups.clear()
        save()
    }

    @Throws(IOException::class) private fun save() {
        val yamlData = linkedMapOf(
            "name" to name,
            "prefix" to prefix,
            "suffix" to suffix,
            "weight" to weight,
            "inherited-groups" to inheritedGroups.map { it.name },
            "permissions" to permissions
        )

        Config.save(file, yamlData)
    }
}