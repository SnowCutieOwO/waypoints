package de.md5lukas.waypoints.util

import com.google.common.math.DoubleMath
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.roundToInt

fun Plugin.runTask(block: () -> Unit) {
    server.scheduler.runTask(this, block)
}

fun Plugin.runTaskAsync(block: () -> Unit) {
    server.scheduler.runTaskAsynchronously(this, block)
}

fun Plugin.callEvent(event: Event) {
    server.pluginManager.callEvent(event)
}

fun Plugin.registerEvents(listener: Listener) {
    server.pluginManager.registerEvents(listener, this)
}

fun ConfigurationSection.getStringNotNull(path: String): String =
    getString(path) ?: throw IllegalArgumentException("The configuration key ${getFullPath(path)} is not present")

fun ConfigurationSection.getConfigurationSectionNotNull(path: String): ConfigurationSection =
    getConfigurationSection(path) ?: throw IllegalArgumentException("The configuration section ${getFullPath(path)} is not present")

@Suppress("UNCHECKED_CAST")
fun <T> ConfigurationSection.getListNotNull(path: String): List<T> =
    getList(path) as List<T>? ?: throw IllegalArgumentException("The list at ${getFullPath(path)} is not present")

private fun ConfigurationSection.getFullPath(path: String): String =
    if (currentPath!!.isEmpty()) {
        path
    } else {
        "$currentPath.$path"
    }

operator fun Vector.div(d: Int) = Vector(
    this.x / d,
    this.y / d,
    this.z / d,
)

operator fun Vector.div(d: Double) = Vector(
    this.x / d,
    this.y / d,
    this.z / d,
)


operator fun Vector.minus(other: Vector) = subtract(other)

fun Location.blockEquals(other: Location): Boolean =
    this.world == other.world && this.blockX == other.blockX && this.blockY == other.blockY && this.blockZ == other.blockZ

val Location.highestBlock: Block
    get() = world!!.getHighestBlockAt(this)

fun Player.sendActualBlock(location: Location) = this.sendBlockChange(location, location.block.blockData)

fun Player.teleportKeepOrientation(location: Location) {
    val target = location.clone()
    target.pitch = this.location.pitch
    target.yaw = this.location.yaw
    this.teleport(target)
}

fun parseLocationString(player: Player, input: String): Location? {
    val parts = input.split("/", " ")

    if (parts.size != 3) {
        return null
    }

    return try {
        Location(player.world, parts[0].toDouble(), parts[1].toDouble(), parts[2].toDouble())
    } catch (_: NumberFormatException) {
        null
    }
}

fun isLocationOutOfBounds(location: Location): Boolean {
    val world = location.world!!

    return !world.worldBorder.isInside(location) || location.y.roundToInt() !in world.minHeight until world.maxHeight
}

fun isMinecraftVersionEqualOrLaterThan(plugin: Plugin, major: Int, minor: Int = 0): Boolean {
    val version = plugin.server.bukkitVersion.substringBefore('-') // 1.16.5-R0.1-SNAPSHOT -> 1.16.5
    val parts = version.split('.')

    return parts[1].toInt() >= major && (parts.getOrNull(2)?.toInt() ?: 0) >= minor
}

fun Location.fuzzyEquals(other: Location, tolerance: Double) =
    world === other.world && DoubleMath.fuzzyEquals(x, other.x, tolerance)
            && DoubleMath.fuzzyEquals(y, other.y, tolerance) && DoubleMath.fuzzyEquals(x, other.x, tolerance)