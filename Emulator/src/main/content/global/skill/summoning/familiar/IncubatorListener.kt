package content.global.skill.summoning.familiar

import content.global.skill.summoning.pet.IncubatorEgg
import content.global.skill.summoning.pet.IncubatorTimer
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.tools.StringUtils
import org.rs.consts.Scenery

class IncubatorListener : InteractionListener {

    private val eggIds = IncubatorEgg.values().map { it.egg.id }.toIntArray()
    private val incubatorIds = intArrayOf(28550, 28352, Scenery.INCUBATOR_28359)

    override fun defineListeners() {
        on(incubatorIds, IntType.SCENERY, "take-egg") { player, _ ->
            val region = player.location.regionId
            val activeEgg = IncubatorTimer.getEggFor(player, region) ?: return@on false

            if (!activeEgg.finished) {
                sendMessage(player, "That egg hasn't finished incubating!")
                return@on true
            }

            if (freeSlots(player) < 1) {
                sendMessage(player, "You do not have enough inventory space to do that.")
                return@on true
            }

            val egg = IncubatorTimer.removeEgg(player, region) ?: return@on false
            val product = egg.product
            val name = getItemName(egg.product.id).lowercase()

            sendMessage(player, "You take your $name out of the incubator.")
            addItem(player, product.id)
            return@on true
        }

        on(incubatorIds, IntType.SCENERY, "inspect") { player, _ ->
            val activeEgg = IncubatorTimer.getEggFor(player, player.location.regionId)

            if (activeEgg == null) {
                sendMessage(player, "The incubator is currently empty.")
                return@on true
            }

            if (activeEgg.finished) {
                sendMessage(player, "The egg inside has finished incubating.")
                return@on true
            }

            val creatureName = getItemName(activeEgg.egg.product.id).lowercase()

            sendMessage(
                player,
                "There is currently ${if (StringUtils.isPlusN(creatureName)) "an" else "a"} $creatureName egg incubating.",
            )
            return@on true
        }

        onUseWith(IntType.SCENERY, eggIds, *incubatorIds) { player, used, _ ->
            val egg = IncubatorEgg.forItem(used.asItem()) ?: return@onUseWith false
            val activeEgg = IncubatorTimer.getEggFor(player, player.location.regionId)

            if (activeEgg != null) {
                sendMessage(player, "You already have an egg in this incubator.")
                return@onUseWith true
            }

            if (removeItem(player, used.asItem())) {
                IncubatorTimer.registerEgg(player, player.location.regionId, egg)
            }
            return@onUseWith true
        }
    }

}
