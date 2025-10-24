package content.region.kandarin.ardougne.west.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.NPCs
import content.global.skill.summoning.objectpets.Pets

class CivilianCatPlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.NPC, GROWN_CATS, *CIVILIANS) { player, used, _ ->
            if (removeItem(player, used.id)) {
                Pets.forId(used.id)?.let {
                    player.familiarManager.removeDetails(used.id)
                }
                addItem(player, Items.DEATH_RUNE_560, 100)
                sendItemDialogue(player, Items.DEATH_RUNE_560, "You hand over the cat.<br>You are given 100 Death Runes.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, KITTENS, *CIVILIANS) { player, _, npc ->
            sendNPCDialogue(player, npc.id, "That kitten isn't big enough; come back when it's bigger.")
            return@onUseWith true
        }
    }

    companion object {
        /**
         * The civilians.
         */
        private val CIVILIANS = intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)

        /**
         * The grown cats.
         */
        private val GROWN_CATS = Pets.values()
            .flatMap { listOfNotNull(it.grownItemId.takeIf { id -> id > 0 }, it.overgrownItemId.takeIf { id -> id > 0 }) }
            .toIntArray()

        /**
         * The kittens.
         */
        private val KITTENS = Pets.values()
            .map { it.babyItemId }
            .toIntArray()
    }
}
