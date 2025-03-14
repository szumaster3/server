package content.region.asgarnia.dialogue.whitewolfmountain

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class OracleDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        when (quest!!.getStage(player)) {
            20 -> player("I seek a piece of the map to the island of Crandor.").also { stage = 0 }
            else -> player("Can you impart your wise knowledge on me, O Oracle?").also { stage = 0 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            20 ->
                when (stage) {
                    0 ->
                        npc(
                            "The map's behind a door below,",
                            "but entering is rather tough.",
                            "This is that you need to know:",
                            "You must use the following stuff.",
                        ).also {
                            stage++
                        }
                    1 ->
                        npc(
                            "First, a drink used by a mage.",
                            "Next, some worm string, changed to sheet.",
                            "Then, a small crustacean cage.",
                            "Last, a bowl that's not seen heat.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            else ->
                when (stage) {
                    0 ->
                        npc("Don't judge a book by its cover - judge it on its'", "grammar and punctuation.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ORACLE_746)
    }

    companion object {
        var quest: Quest? = null
    }
}
