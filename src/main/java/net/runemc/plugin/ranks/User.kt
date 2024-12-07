package net.runemc.plugin.ranks

import net.runemc.plugin.Main
import net.runemc.utils.Config
import org.jetbrains.annotations.NotNull
import java.io.File
import java.io.IOException
import java.util.*

data class User(
    private val file: File,
    val username: String,
    val uuid: UUID,
    val groups: MutableList<Group> = mutableListOf(),
    val tracks: MutableMap<Track, Int> = mutableMapOf(),
    val permissions: MutableList<String> = mutableListOf()
) {
    companion object {
        @Throws(IOException::class) fun create(username: String, uuid: UUID): User {
            val file = File(Main.get().dataFolder, "users/$uuid.yml")
            if (file.exists()) {
                throw IllegalStateException("User already exists: $username")
            }

            val user = User(file, username, uuid)
            user.save()
            return user
        }
        @Throws(IOException::class) fun get(username: String, uuid: UUID): User {
            val file = File(Main.get().dataFolder, "users/$uuid.yml")
            return if (!file.exists()) {
                create(username, uuid)
            } else {
                get(file)
            }
        }
        @Throws(IOException::class) fun get(file: File): User {
            if (!file.exists()) {
                throw IOException("File does not exist: ${file.path}")
            }

            val yamlData = Config.load(file, Any::class.java) as? Map<*, *>
                ?: throw IOException("Invalid YAML structure in: ${file.name}")

            val username = yamlData["username"] as String
            val uuid = UUID.fromString(yamlData["uuid"] as String)
            val user = User(file, username, uuid)

            val groupNames = yamlData["groups"] as? List<String> ?: emptyList()
            for (groupName in groupNames) {
                user.groups.add(Group.get(groupName))
            }

            val trackEntries = yamlData["tracks"] as? List<List<String>> ?: emptyList()
            for (entry in trackEntries) {
                val trackName = entry[0]
                val position = entry[1].toInt()
                user.tracks[Track.get(trackName)] = position
            }

            user.permissions.addAll(yamlData["permissions"] as? List<String> ?: emptyList())
            return user
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

    @Throws(IOException::class) fun addTrack(track: Track) {
        if (!tracks.containsKey(track)) {
            tracks[track] = tracks.size
            save()
        }
    }
    @Throws(IOException::class) fun removeTrack(track: Track) {
        tracks.remove(track)
        save()
    }
    @Throws(IOException::class) fun clearTracks() {
        tracks.clear()
        save()
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

    @Throws(IOException::class) fun save() {
        val yamlData = linkedMapOf(
            "username" to username,
            "uuid" to uuid.toString(),
            "groups" to groups.map { it.name },
            "tracks" to tracks.entries.map { listOf(it.key.name, it.value.toString()) },
            "permissions" to permissions
        )

        Config.save(file, yamlData)
    }
}