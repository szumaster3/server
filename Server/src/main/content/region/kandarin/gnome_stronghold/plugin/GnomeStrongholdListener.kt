package content.region.kandarin.gnome_stronghold.plugin

import content.region.kandarin.gnome_stronghold.dialogue.GnomeWomanDialogue
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class GnomeStrongholdListener : InteractionListener {

    override fun defineListeners() {
        on(intArrayOf(NPCs.GNOME_WOMAN_168,NPCs.GNOME_WOMAN_169), IntType.NPC, "Talk-to") { player, node ->
            openDialogue(player, GnomeWomanDialogue(), node.id)
            return@on true
        }

        /*
         * Handles interaction with kitchen cabinet.
         * https://runescape.wiki/w/Kitchen_cabinet
         */

        on(Scenery.KITCHEN_CABINET_17132, IntType.SCENERY, "search") { player, node ->
            openDialogue(player, KitchenCabinetDialogue())
            return@on true
        }
    }

    private class KitchenCabinetDialogue : DialogueFile() {

        private val itemMap = mapOf(
            1 to Items.KNIFE_946,
            2 to Items.GNOMEBOWL_MOULD_2166,
            3 to Items.CRUNCHY_TRAY_2165,
            4 to Items.BATTA_TIN_2164
        )

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> showTopics(
                    Topic("Knife", 1, true),
                    Topic("Gnomebowl mould", 2, true),
                    Topic("Crunchy tray", 3, true),
                    Topic("Batta tin", 4, true),
                    title = "What do you want to take?"
                )
                in 1..4 -> handleItem(stage)
            }
        }

        private fun handleItem(stage: Int) {
            end()
            val item = itemMap[stage] ?: return
            if(freeSlots(player!!) == 0) {
                sendMessage(player!!, "You don't have enough inventory space.")
                this.stage = END_DIALOGUE
                return
            }
            addItem(player!!, item)
        }
    }
}