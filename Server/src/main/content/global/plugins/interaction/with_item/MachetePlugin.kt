package content.global.plugins.interaction.with_item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

class MachetePlugin : InteractionListener {

    private val macheteID = intArrayOf(Items.MACHETE_975, Items.OPAL_MACHETE_6313, Items.JADE_MACHETE_6315, Items.RED_TOPAZ_MACHETE_6317)
    private val thatchSparLight = Items.THATCH_SPAR_LIGHT_6281
    private val thatchSparMed = Items.THATCH_SPAR_MED_6283
    private val thatchSparDense = Items.THATCH_SPAR_DENSE_6285
    private val skewerStick = Items.SKEWER_STICK_6305

    override fun defineListeners() {
        onUseWith(IntType.ITEM, macheteID, thatchSparLight) { player, used, _ ->
            animate(player, getAnimation(used))
            playAudio(player, Sounds.TBCU_PREPARE_WOOD_1274)
            if (!removeItem(player, thatchSparLight, Container.INVENTORY)) return@onUseWith false
            addItem(player, skewerStick, RandomFunction.random(3, 6))
            sendMessage(player, "You slice the thatch spar light into skewer sticks")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, macheteID, thatchSparMed) { player, used, _ ->
            animate(player, getAnimation(used))
            if (!removeItem(player, thatchSparMed, Container.INVENTORY)) return@onUseWith false
            addItem(player, skewerStick, RandomFunction.random(3, 6))
            sendMessage(player, "You slice the thatch spar medium into skewer sticks")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, macheteID, thatchSparDense) { player, used, _ ->
            animate(player, getAnimation(used))
            if (!removeItem(player, thatchSparDense, Container.INVENTORY)) return@onUseWith false
            addItem(player, skewerStick, RandomFunction.random(3, 6))
            sendMessage(player, "You slice the thatch spar dense into skewer sticks")
            return@onUseWith true
        }
    }

    private fun getAnimation(machete: Node): Int {
        return when (machete.asItem().id) {
            Items.MACHETE_975 -> Animations.MAKE_SKEWER_TAI_BWO_WANNAI_CLEANUP_2389
            Items.OPAL_MACHETE_6313 -> Animations.OPAL_MACHETE_2429
            Items.JADE_MACHETE_6315 -> 6430
            Items.RED_TOPAZ_MACHETE_6317 -> Animations.RED_TOPAZ_MACHETE_2431
            else -> return Animations.MAKE_SKEWER_TAI_BWO_WANNAI_CLEANUP_2389
        }
    }
}
