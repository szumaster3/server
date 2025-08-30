package content.region.kandarin.ardougne.west.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.NPCs
import content.global.skill.summoning.pet.Pets

class CivilianCatPlugin : InteractionListener {
    private val civilians = intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)

    override fun defineListeners() {
        val GROWN_CAT = Pets.values().mapNotNull { pet -> listOfNotNull(pet.grownItemId.takeIf { it > 0 }, pet.overgrownItemId.takeIf { it > 0 }, pet.wilyItemId.takeIf { it > 0 }, pet.lazyItemId.takeIf { it > 0 }) }.flatten().toIntArray()
        val KITTEN_ID = Pets.values().map { it.babyItemId }.toIntArray()

        onUseWith(IntType.NPC, GROWN_CAT, *civilians) { player, used, _ ->
            if(removeItem(player, used.id)) {
                val pet = Pets.forId(used.id)
                pet?.let {
                    player.familiarManager.removeDetails(used.id)
                }
                addItem(player, Items.DEATH_RUNE_560, 100)
                sendItemDialogue(player, Items.DEATH_RUNE_560, "You hand over the cat.<br>You are given 100 Death Runes.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, KITTEN_ID, *civilians) { player, _, npc ->
            sendNPCDialogue(player, npc.id, "That kitten isn't big enough; come back when it's bigger.")
            return@onUseWith true
        }
    }

}
