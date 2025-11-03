package content.region.karamja.quest.roots

import core.api.addItemOrDrop
import core.api.sendItemDialogue
import core.api.setVarbit
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import shared.consts.*

class BackToMyRootsPlugin : InteractionListener {

    override fun defineListeners() {

        on(Scenery.SMELLY_PACKAGE_27055, IntType.SCENERY, "open") { player, _ ->
            player.animate(Animation(Animations.HUMAN_WITHDRAW_833))
            val npcId = Repository.findNPC(NPCs.RPDT_EMPLOYEE_843)
            npcId?.sendChat("Oh great, back to work.")
            setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 20, true)
            sendItemDialogue(player, Items.HAND_11763, "You find a hand with a scrap of a wizards robe attached.")
            addItemOrDrop(player, Items.HAND_11763)
            return@on true
        }
    }
}