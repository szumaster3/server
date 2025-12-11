package content.global.plugins.item.equipment.fog_gloves

import core.api.sendMessage
import core.api.toIntArray
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import kotlin.math.min

private val FOG_GLOVES = (Items.IRIT_GLOVES_12856..Items.EARTH_RUNECRAFTING_GLOVES_12865).toIntArray()
private val MAX_CHARGES = intArrayOf(100, 100, 100, 100, 1000, 1000, 1000, 1000, 1000, 1000)

class FOGGlovesListener : InteractionListener {

    override fun defineListeners() {
        on(FOG_GLOVES, IntType.ITEM, "inspect") { player, node ->
            sendMessage(player, "${node.name}: ${min(node.asItem().charge, MAX_CHARGES[node.id - Items.IRIT_GLOVES_12856])} charge left.")
            return@on true
        }
    }
}
