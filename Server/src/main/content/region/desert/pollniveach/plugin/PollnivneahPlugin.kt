package content.region.desert.pollniveach.plugin

import content.region.desert.pollniveach.dialogue.AliTheBarmanDialogue
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class PollnivneahPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles taking the beer from the bar table.
         */

        on(Scenery.TABLE_6246, IntType.SCENERY, "take-beer") { player, node ->
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You don't have enough inventory space.")
                return@on true
            }

            lock(player, 1)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            replaceScenery(node.asScenery(), 602, 80)
            addItem(player, Items.BEER_1917)

            return@on true
        }

        /*
         * Handles using coins on money pot.
         */

        onUseWith(IntType.SCENERY, Items.COINS_995, Scenery.MONEY_POT_6230) { player, _, _ ->
            if (removeItem(player, Item(Items.COINS_995, 3))) {
                player.dialogueInterpreter.open(NPCs.ALI_THE_SNAKE_CHARMER_1872, true)
            }
            return@onUseWith true
        }


        /*
         * Handles talking to npc around city.
         */

        on(NPCs.ALI_THE_BARMAN_1864, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, AliTheBarmanDialogue(), node.asNpc())
            return@on true
        }


    }
}
