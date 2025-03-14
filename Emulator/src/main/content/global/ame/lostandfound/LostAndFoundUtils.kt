package content.global.ame.lostandfound

import content.data.RandomEvent
import core.api.*
import core.api.ui.setMinimapState
import core.cache.def.impl.SceneryDefinition
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Items

object LostAndFoundUtils {
    val eventLocation = Location(2338, 4747, 0)
    const val essenceMineKey = "laf:essence-mine"

    fun cleanup(player: Player) {
        setMinimapState(player, 0)
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        removeAttributes(player, essenceMineKey, RandomEvent.save())
    }

    fun isOddAppendage(
        player: Player,
        scenery: Scenery,
    ): Boolean {
        val index = scenery.getWrapper().id - 8994
        val current = scenery.getWrapper().getChild(player).id
        return (0..3).none { i ->
            index != i && SceneryDefinition.forId(8994 + i).getChildObject(player)?.id == current
        }
    }

    fun setRandomAppendage(player: Player) {
        var value = 0
        val oddIndex = RandomFunction.random(4)
        val mod = if (RandomFunction.RANDOM.nextBoolean()) 0 else 1
        val offset = RandomFunction.random(4) * 2

        for (i in 0..3) {
            value = value or ((offset + if (i == oddIndex) (1 - mod) else mod) shl (i * 5))
        }
        setVarp(player, 531, value)
    }

    fun reward(player: Player) {
        val runes = player.getAttribute("teleport:items", emptyArray<Item>())
        val roll = (8..14).random()

        if (!getAttribute(player, essenceMineKey, false)) {
            if (runes.isNotEmpty()) {
                runes.forEach { rune ->
                    addItem(player, rune.id, rune.amount)
                }
                sendDoubleItemDialogue(
                    player,
                    runes[0],
                    runes[1],
                    "Abyssal Services apologize for any inconvenience. Please accept these runes as recompense.",
                )
            }
        } else {
            if (!GameWorld.settings!!.isMembers) {
                addItemOrDrop(player, Items.RUNE_ESSENCE_1437, roll)
            } else {
                addItemOrDrop(player, Items.PURE_ESSENCE_7937, roll)
            }
        }
    }
}
