package content.region.kandarin.quest.fishingcompo.dialogue

import content.region.kandarin.quest.fishingcompo.FishingContest
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class MorrisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "What are you sitting around here for?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm making sure only those with a competition pass enter",
                    "the fishing contest.",
                )
                stage++
            }

            1 ->
                if (player.inventory.containsItem(FishingContest.FISHING_PASS)) {
                    player("I have one here...")
                    stage++
                } else {
                    end()
                }

            2 -> {
                player.dialogueInterpreter.sendDialogue("You show Morris your pass.")
                stage++
            }

            3 -> {
                npc("Move on through. Talk to Bonzo", "to enter the competition.")
                setAttribute(player, "/save:fishing_contest:pass-shown", true)
                player.inventory.remove(FishingContest.FISHING_PASS)
                stage = 100
            }

            100 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MORRIS_227)
    }
}
