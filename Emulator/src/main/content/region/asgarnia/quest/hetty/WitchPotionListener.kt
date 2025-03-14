package content.region.asgarnia.quest.hetty

import core.api.quest.getQuestStage
import core.api.sendMessage
import core.api.sendPlayerDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WitchPotionListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CAULDRON_2024, IntType.SCENERY, "Drink From") { player, _ ->
            when (getQuestStage(player, Quests.WITCHS_POTION)) {
                20, 100 -> sendPlayerDialogue(player, "As nice as that looks I think I'll give it a miss for now.")
                40 -> player.dialogueInterpreter.open(NPCs.HETTY_307, true, 1)
                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }
    }
}
