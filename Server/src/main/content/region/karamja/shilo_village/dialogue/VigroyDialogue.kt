package content.region.karamja.shilo_village.dialogue

import content.region.karamja.shilo_village.plugin.ShiloVillagePlugin
import core.api.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class VigroyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if (!hasRequirement(player, Quests.SHILO_VILLAGE)) {
            npcl(FaceAnim.HALF_GUILTY, "Sorry, I'm busy. I've got to get out of here! This place is swarming with zombies.")
            return false
        }

        npcl(FaceAnim.FRIENDLY,"I am offering a cart ride to Brimhaven if you're interested. It will cost 10 gold coins.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic("Yes please. I'd like to go to Brimhaven.", 1, true),
                Topic("No, thanks.", END_DIALOGUE, true)
            )
            1 -> {
                end()
                ShiloVillagePlugin.quickTravel(player, NPCs.VIGROY_511)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VIGROY_511)
}
