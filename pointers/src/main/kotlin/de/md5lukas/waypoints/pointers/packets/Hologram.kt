package de.md5lukas.waypoints.pointers.packets

import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataValue
import java.util.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

internal class Hologram(player: Player, location: Location, var text: Component) :
    ClientSideEntity(
        player,
        location,
        EntityType.ARMOR_STAND,
    ) {

  private val wrappedText
    get() =
        Optional.of(
            WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(text)).handle)

  override fun modifyMetadataValues(spawn: Boolean, dataValues: MutableList<WrappedDataValue>) {
    dataValues += WrappedDataValue(2, optChatSerializer, wrappedText)
    if (spawn) {
      dataValues += disableGravity
      dataValues += WrappedDataValue(0, byteSerializer, (0x20).toByte()) // Make invisible
      dataValues += WrappedDataValue(3, booleanSerializer, true) // Nametag always visible
      // https://wiki.vg/Entity_metadata#Armor_Stand
      dataValues +=
          WrappedDataValue(15, byteSerializer, (0x10).toByte()) // Make ArmorStand a marker
    }
  }
}
