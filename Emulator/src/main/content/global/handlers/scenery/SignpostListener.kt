package content.global.handlers.scenery

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Scenery

class SignpostListener : InteractionListener {
    override fun defineListeners() {
        registerSignpost(
            Scenery.SIGNPOST_18493,
            listOf(
                Location(3235, 3228) to
                    listOf(
                        "Head north towards Fred's farm, and the windmill.",
                        "South to the swamps of Lumbridge.",
                        "Cross the bridge and head east to Al Kharid or north to Varrock.",
                        "West to the Lumbridge Castle and Draynor Village. Beware the goblins!",
                    ),
                Location(3261, 3230) to
                    listOf(
                        "North to farms and Varrock.",
                        "The River Lum lies to the south.",
                        "East to Al Kharid - toll gate; bring some money.",
                        "West to Lumbridge.",
                    ),
                Location(2983, 3278) to
                    listOf(
                        "North to the glorious White Knights' city of Falador.",
                        "South to Rimmington.",
                        "East to Port Sarim and Draynor Village.",
                        "West to the Crafting Guild.",
                    ),
                Location(3107, 3296) to
                    listOf(
                        "North to Draynor Manor.",
                        "South to Draynor Village and the Wizards' Tower.",
                        "East to Lumbridge.",
                        "West to Port Sarim.",
                    ),
            ),
        )

        registerSignpost(
            Scenery.SIGNPOST_24263,
            listOf(
                Location(3268, 3332) to
                    listOf(
                        "Sheep lay this way.",
                        "South through farms to Al Kharid and Lumbridge.",
                        "East to Al Kharid mine and follow the path north to Varrock east gate.",
                        "West to Champion's Guild and Varrock south gate.",
                    ),
                Location(3283, 3333) to
                    listOf(
                        "North to Varrock mine and Varrock east gate.",
                        "South to large Mining area and Al Kharid.",
                        "Follow the path east to the Dig Site.",
                        "West to Champion's Guild and Varrock south gate.",
                    ),
            ),
        )

        registerSignpost(
            Scenery.SIGNPOST_4132,
            listOf(
                Location(3223, 3427) to
                    listOf(
                        "North to Varrock Palace.",
                        "South to the Champion's Guild.",
                        "East to the Dig Site.",
                        "West to Barbarian Village and Falador.",
                    ),
                Location(3166, 3286) to
                    listOf(
                        "North to the windmill.",
                        "South to a fishing pond next to Fred's farm.",
                        "East to Lumbridge.",
                        "West to Port Sarim and Draynor Village.",
                    ),
                Location(3285, 3430) to
                    listOf(
                        "North to the Lumber Yard.",
                        "South to Al Kharid and Lumbridge.",
                        "East to the Dig Site.",
                        "West to Varrock.",
                    ),
                Location(2734, 3485) to
                        listOf(
                            "North to Sinclair Mansion.",
                            "South to Keep Le Faye.",
                            "East to Catherby.",
                            "West to Hemenster.",
                        ),
            ),
        )

        registerSignpost(
            Scenery.SIGNPOST_4134,
            listOf(
                Location(2651, 3606) to
                    listOf(
                        "North to Rellekka.",
                        "South to Seers' Village.",
                        "East to Death Plateau.",
                        "West to the Lighthouse.",
                    ),
                Location(3100, 3418) to
                    listOf(
                        "North to Edgeville.",
                        "South to Draynor Manor.",
                        "East to Varrock west gate.",
                        "West to Barbarian Village and Falador.",
                    ),
            ),
        )

        on(Scenery.SIGNPOST_25397, IntType.SCENERY, "read") { player, _ ->
            sendSignpostDirections(
                player,
                listOf(
                    "Grave of Scorpius.",
                    "Tree Gnome Village.",
                    "Castle Wars.",
                    "Observatory reception.",
                ),
            )
            return@on true
        }

        on(Scenery.SIGNPOST_31296, IntType.SCENERY, "read") { player, node ->
            if (node.asScenery().location == Location(3304, 3109)) {
                sendSignpostDirections(
                    player,
                    listOf(
                        "North to Al Kharid.",
                        "South to the Desert Mining Camp and Pollnivneach.",
                        "East and across the river to the Ruins of Uzer.",
                        "West to the Kalphite Lair.",
                    ),
                )
            } else {
                sendUnknownDirections(player)
            }
            return@on true
        }
    }

    private fun registerSignpost(
        sceneryId: Int,
        locations: List<Pair<Location, List<String>>>,
    ) {
        on(sceneryId, IntType.SCENERY, "read") { player, node ->
            val location = node.asScenery().location
            val directions =
                locations.firstOrNull { it.first == location }?.second ?: listOf(
                    "North to unknown.",
                    "South to unknown.",
                    "East to unknown.",
                    "West to unknown.",
                )
            sendSignpostDirections(player, directions)
            return@on true
        }
    }

    private fun sendSignpostDirections(
        player: Player,
        directions: List<String>,
    ) {
        sendString(player, directions[0], 135, 3)
        sendString(player, directions[1], 135, 9)
        sendString(player, directions[2], 135, 8)
        sendString(player, directions[3], 135, 12)
        openInterface(player, Components.AIDE_COMPASS_135)
    }

    private fun sendUnknownDirections(player: Player) {
        sendSignpostDirections(
            player,
            listOf(
                "North to unknown.",
                "South to unknown.",
                "East to unknown.",
                "West to unknown.",
            ),
        )
    }
}
