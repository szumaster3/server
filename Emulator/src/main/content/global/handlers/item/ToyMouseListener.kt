package content.global.handlers.item

import content.data.GameAttributes
import content.global.handlers.npc.ToyMouseNPC
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import org.rs.consts.NPCs

class ToyMouseListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles releasing the toy mouse.
         */

        on(Items.TOY_MOUSE_WOUND_7769, IntType.ITEM, "release") { player, node ->
            if (removeItem(player, node.asItem())) {
                ToyMouseNPC.spawnToyMouse(player)
            }
            return@on true
        }

        /*
         * Handles winding the toy mouse.
         */

        on(Items.TOY_MOUSE_7767, IntType.ITEM, "wind") { player, node ->
            if (removeItem(player, node.asItem())) {
                ToyMouseNPC.spawnToyMouse(player)
            }
            return@on true
        }

        /*
         * Handles picking up the toy mouse.
         */

        on(NPCs.TOY_MOUSE_3597, IntType.NPC, "pick-up") { player, node ->
            val uid = player.details.uid
            val mouseOwnerUID = getAttribute(player, GameAttributes.ITEM_TOY_MOUSE_RELEASE, -1)

            if (mouseOwnerUID == -1 || mouseOwnerUID != uid) {
                sendMessage(player, "You can't interact with someone else toy.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough space in your inventory.")
                return@on true
            }

            val failRoll = AgilityHandler.hasFailed(player, 1, 0.01)
            if (failRoll) {
                sendMessage(player, "You fail to catch the mouse!")
                ToyMouseNPC.removeMouse(player, node.asNpc())
                return@on true
            }

            sendMessage(player, "You catch the mouse.")
            addItem(player, Items.TOY_MOUSE_WOUND_7769)
            rewardXP(player, Skills.AGILITY, 3.0)
            ToyMouseNPC.removeMouse(player, node.asNpc())
            return@on true
        }
    }
}
