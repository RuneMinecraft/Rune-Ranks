package net.runemc.plugin.ranks

import net.runemc.plugin.Main
import net.runemc.utils.Config
import java.io.File
import java.io.IOException

data class Track(
    private val file: File,
    val name: String,
    val groups: MutableList<Group> = mutableListOf()
) {
    companion object {
        @Throws(IOException::class) fun create(name: String): Track {
            val file = File("config/tracks/${name.lowercase()}.yml")
            if (file.exists()) {
                throw IllegalStateException("Track already exists: $name")
            }

            val track = Track(file, name)
            track.save()
            return track
        }

        @Throws(IOException::class) fun get(name: String): Track {
            val file = File(Main.get().dataFolder, "tracks/${name.lowercase()}.yml")
            return get(file)
        }
        @Throws(IOException::class) fun get(file: File): Track {
            if (!file.exists()) {
                throw IOException("File does not exist: ${file.path}")
            }

            val yamlData = Config.load(file, Any::class.java) as? Map<*, *>
                ?: throw IOException("Invalid YAML structure in: ${file.name}")

            val name = yamlData["name"] as String
            val track = Track(file, name)

            val groupNames = yamlData["groups"] as? List<String> ?: emptyList()
            for (groupName in groupNames) {
                track.groups.add(Group.get(groupName))
            }

            return track
        }
    }

    @Throws(IOException::class) fun addGroup(group: Group) {
        if (!groups.contains(group)) {
            groups.add(group)
            save()
        }
    }
    @Throws(IOException::class) fun removeGroup(group: Group) {
        if (groups.remove(group)) {
            save()
        }
    }
    @Throws(IOException::class) fun clearGroups() {
        groups.clear()
        save()
    }

    @Throws(IOException::class) private fun save() {
        val yamlData = linkedMapOf(
            "name" to name,
            "groups" to groups.map { it.name }
        )

        Config.save(file, yamlData)
    }
}