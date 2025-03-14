package content.global.handlers.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.tools.RandomFunction
import org.rs.consts.Items

class MacheteListener : InteractionListener {
    val MACHETE =
        intArrayOf(Items.MACHETE_975, Items.OPAL_MACHETE_6313, Items.JADE_MACHETE_6315, Items.RED_TOPAZ_MACHETE_6317)
    val THATCHSPAR_LIGHT = Items.THATCH_SPAR_LIGHT_6281
    val THATCHSPAR_MED = Items.THATCH_SPAR_MED_6283
    val THATCHSPAR_DENSE = Items.THATCH_SPAR_DENSE_6285
    val SKEWER_STICK = Items.SKEWER_STICK_6305

    override fun defineListeners() {
        onUseWith(IntType.ITEM, MACHETE, THATCHSPAR_LIGHT) { player, used, _ ->
            animate(player, getAnimation(used))
            playAudio(player, 1274)
            if (!removeItem(player, THATCHSPAR_LIGHT, Container.INVENTORY)) return@onUseWith false
            addItem(player, SKEWER_STICK, RandomFunction.random(3, 6))
            sendMessage(player, "You slice the thatch spar light into skewer sticks")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, MACHETE, THATCHSPAR_MED) { player, used, _ ->
            animate(player, getAnimation(used))
            if (!removeItem(player, THATCHSPAR_MED, Container.INVENTORY)) return@onUseWith false
            addItem(player, SKEWER_STICK, RandomFunction.random(3, 6))
            sendMessage(player, "You slice the thatch spar medium into skewer sticks")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, MACHETE, THATCHSPAR_DENSE) { player, used, _ ->
            animate(player, getAnimation(used))
            if (!removeItem(player, THATCHSPAR_DENSE, Container.INVENTORY)) return@onUseWith false
            addItem(player, SKEWER_STICK, RandomFunction.random(3, 6))
            sendMessage(player, "You slice the thatch spar dense into skewer sticks")
            return@onUseWith true
        }
    }

    private fun getAnimation(machete: Node): Int {
        return when (machete.asItem().id) {
            Items.MACHETE_975 -> 2389
            Items.OPAL_MACHETE_6313 -> 2429
            Items.JADE_MACHETE_6315 -> 6430
            Items.RED_TOPAZ_MACHETE_6317 -> 2431
            else -> return 2389
        }
    }
}
