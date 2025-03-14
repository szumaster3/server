package core.game.system.command.sets

import core.ServerConstants
import core.game.node.entity.player.link.TeleportManager
import core.game.system.command.Privilege
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.repository.Repository
import core.plugin.Initializable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Initializable
class TeleportCommandSet : CommandSet(Privilege.ADMIN) {
    override fun defineCommands() {
        define(
            name = "to",
            privilege = Privilege.ADMIN,
            usage = "::to <lt>String<gt>",
            description = "See ServerConstants.TELEPORT_DESTINATIONS",
        ) { player, args ->
            var destination: Location? = null
            val place = args.slice(1 until args.size).joinToString(" ")
            for (destinations in ServerConstants.TELEPORT_DESTINATIONS) {
                var i = 1
                while (i < destinations.size) {
                    if (place == destinations[i]) {
                        destination = destinations[0] as Location
                        break
                    }
                    i++
                }
            }
            if (destination != null) {
                player.teleporter.send(destination, TeleportManager.TeleportType.INSTANT)
            } else {
                reject(player, "Could not locate teleport destination [name=$place]!")
            }
        }

        define(
            name = "tele",
            privilege = Privilege.ADMIN,
            usage = "::tele <lt>X<gt> <lt>Y<gt> <lt>Z<gt> OR <lt>JAGCOORD<gt>",
            description = "JAGCOORD is Z_REGIONX_REGIONY_LOCALX_LOCALY",
        ) { player, args ->
            if (args.size == 2 && args[1].contains(",")) {
                val args2 = args[1].split(",".toRegex()).toTypedArray()
                val x = args2[1].toInt() shl 6 or args2[3].toInt()
                val y = args2[2].toInt() shl 6 or args2[4].toInt()
                val z = args2[0].toInt()
                player.properties.teleportLocation = Location.create(x, y, z)
                return@define
            }
            if (args.size == 2 && args[1].contains("_")) {
                val tokens = args[1].split("_")
                when (tokens.size) {
                    2 -> {
                        val regionX = tokens[0].toInt()
                        val regionY = tokens[1].toInt()

                        player.properties.teleportLocation =
                            Location.create((regionX shl 6) or 15, (regionY shl 6) or 15, 0)
                    }

                    3 -> {
                        val plane = tokens[0].toInt()
                        val regionX = tokens[1].toInt()
                        val regionY = tokens[2].toInt()

                        player.properties.teleportLocation =
                            Location.create((regionX shl 6) or 15, (regionY shl 6) or 15, plane)
                    }

                    4 -> {
                        val regionX = tokens[0].toInt()
                        val regionY = tokens[1].toInt()
                        val offsetX = tokens[2].toInt()
                        val offsetY = tokens[3].toInt()

                        val xCoord = (regionX shl 6) or offsetX
                        val yCoord = (regionY shl 6) or offsetY

                        player.properties.teleportLocation = Location.create(xCoord, yCoord, 0)
                    }

                    5 -> {
                        val plane = tokens[0].toInt()
                        val regionX = tokens[1].toInt()
                        val regionY = tokens[2].toInt()
                        val offsetX = tokens[3].toInt()
                        val offsetY = tokens[4].toInt()

                        val xCoord = (regionX shl 6) or offsetX
                        val yCoord = (regionY shl 6) or offsetY

                        player.properties.teleportLocation = Location.create(xCoord, yCoord, plane)
                    }

                    else -> reject(player, "Usage: regionX_regionY OR regionX_regionY_offsetX_offsetY")
                }
                return@define
            }
            if (args.size < 2) {
                reject(player, "Usage: x,y,(optional)z OR regionX_regionY OR regionX_regionY_offsetX_offsetY")
            }
            player.properties.teleportLocation =
                Location.create(args[1].toInt(), args[2].toInt(), if (args.size > 3) args[3].toInt() else 0)
        }

        define(
            name = "teleobj",
            privilege = Privilege.ADMIN,
            usage = "::teleobj <lt>RX_RY<gt> <lt>OBJ NAME<gt>",
            description = "Teleports to the first object with the given name.",
        ) { player, args ->
            if (args.size < 3) reject(player, "Usage: regionX_regionY Object Name")
            var objName = ""
            for (i in 2 until args.size) objName += (args[i] + if (i + 1 == args.size) "" else " ")

            val tokens = args[1].split("_")
            if (tokens.size != 2) reject(player, "Usage: regionX_regionY Object Name")
            val regionX = tokens[0].toInt()
            val regionY = tokens[1].toInt()

            val regionId = (regionX shl 8) or regionY
            val region = RegionManager.forId(regionId)

            GlobalScope.launch {
                for (plane in region.planes) {
                    for (objects in plane.objects.filterNotNull()) {
                        for (parent in objects.filterNotNull()) {
                            if (parent.childs != null) {
                                for (obj in parent.childs.filterNotNull()) {
                                    if (obj.name.equals(objName, ignoreCase = true)) {
                                        player.properties.teleportLocation = obj.location
                                        return@launch
                                    }
                                }
                            } else {
                                if (parent.name.equals(objName, ignoreCase = true)) {
                                    player.properties.teleportLocation = parent.location
                                    return@launch
                                }
                            }
                        }
                    }
                }
            }
        }

        define(
            name = "teleto",
            privilege = Privilege.ADMIN,
            usage = "::teleto <lt>USERNAME<gt>",
            description = "Teleports to the named player.",
        ) { player, args ->
            if (args.size < 1) {
                reject(player, "syntax error: name")
            }
            val n = args.slice(1 until args.size).joinToString("_")
            val target = Repository.getPlayerByName(n)
            if (target == null) {
                reject(player, "syntax error: name")
            }
            if (target!!.getAttribute<Any?>("fc_wave") != null) {
                reject(player, "You cannot teleport to a player who is in the Fight Caves.")
            }
            player.properties.teleportLocation = target.location
        }

        define(
            name = "teletome",
            privilege = Privilege.ADMIN,
            usage = "::teletome <lt>USERNAME<gt>",
            description = "Teleports the given user to you.",
        ) { player, args ->
            if (args.size < 1) {
                reject(player, "syntax error: name")
            }
            val n = args.slice(1 until args.size).joinToString("_")
            val target = Repository.getPlayerByName(n)
            if (target == null) {
                reject(player, "syntax error: name")
            }
            if (target!!.getAttribute<Any?>("fc_wave") != null) {
                reject(player, "You cannot teleport to a player who is in the Fight Caves.")
            }
            target.properties.teleportLocation = player.location
        }

        define(
            name = "home",
            privilege = Privilege.ADMIN,
            usage = "",
            description = "Teleports to HOME_LOCATION",
        ) { player, _ ->
            player.properties.teleportLocation = ServerConstants.HOME_LOCATION
        }

        define(
            name = "findobjs",
            privilege = Privilege.ADMIN,
            usage = "::findobjs <lt>REGION ID<gt> <lt>SCENERY ID<gt>",
            description = "Finds all locations of scenery objects of id.",
        ) { player, args ->
            if (args.size < 4) reject(player, "Usage: region_id scenery_id")
            val regionId = args[1].toInt()
            val sceneryId = args[2].toInt()
            val sceneryIdEnd = args[3].toInt()

            val region = RegionManager.forId(regionId)

            GlobalScope.launch {
                for (plane in region.planes) {
                    for (objects in plane.objects.filterNotNull()) {
                        for (parent in objects.filterNotNull()) {
                            if (parent.id in sceneryId..sceneryIdEnd) {
                                println(parent.location)
                            }
                        }
                    }
                }
            }
        }
    }
}
