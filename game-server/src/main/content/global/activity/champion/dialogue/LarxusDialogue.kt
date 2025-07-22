package content.global.activity.champion.dialogue

import content.data.GameAttributes
import content.global.activity.champion.plugin.ChampionScrollsDropHandler
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the dialogue plugin used for the Larxus NPC.
 * @author Vexia, szu
 */
@Initializable
class LarxusDialogue(player: Player? = null, ) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        npc = NPC(NPCs.LARXUS_3050)
        val challengeStart: Boolean = false
        if (getAttribute(player, GameAttributes.ACTIVITY_CHAMPION_BOSS_CHALLENGE, false)) {
            npc(
                FaceAnim.NEUTRAL,
                "Leon D'Cour has issued you a challenge, he has stated",
                "there will be no items allowed expect those you're",
                "wearing. Do you want to accept the challenge?",
            )
            stage = 5
            return true
        }
        if (challengeStart) {
            openDialogue(player, LarxusDialogueFile(false), npc)
            return true
        } else {
            when (stage) {
                START_DIALOGUE -> {
                    face(findNPC(NPCs.LARXUS_3050)!!, player!!, 1)
                    npcl(FaceAnim.NEUTRAL, "Is there something I can help you with?").also { stage++ }
                }

                1 -> if (anyInInventory(player, *ChampionScrollsDropHandler.SCROLLS)) {
                    options("I was given a challenge, what now?", "What is this place?", "Nothing thanks.").also { stage = 2 }
                } else {
                    options("What is this place?", "Nothing thanks.").also { stage = 6 }
                }

                2 -> when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "I was given a challenge, what now?").also { stage = 3 }
                    2 -> playerl(FaceAnim.HALF_ASKING, "What is this place?").also { stage = 4 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "Nothing thanks.").also { stage = END_DIALOGUE }
                }

                3 -> npcl(FaceAnim.NEUTRAL, "Well pass it here and we'll get you started.").also { stage = END_DIALOGUE }
                4 -> npcl(FaceAnim.NEUTRAL, "This is the champions' arena, the champions of various, races use it to duel those they deem worthy of the, honour.").also { stage = END_DIALOGUE }
                5 -> end().also { openDialogue(player, LarxusDialogueFile()) }
                6 -> when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "What is this place?").also { stage = 4 }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Nothing thanks.").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LARXUS_3050)
}
class LarxusDialogueFile(val challengeStart: Boolean = false) : DialogueFile() {

    companion object {
        private val baseText = listOf(
            "So you want to accept the challenge huh? Well there",
            "are some specific rules for these Champion fights. For"
        )

        private val challengeTexts = mapOf(
            Items.CHAMPION_SCROLL_6798 to listOf(
                "this fight you're not allowed to use any Prayer's."
            ),
            Items.CHAMPION_SCROLL_6799 to listOf(
                "ths fight you're only allowed to take Weapons, no other",
                "items are allowed."
            ),
            Items.CHAMPION_SCROLL_6800 to listOf(
                "this fight you're only allowed to use Melee attacks, no",
                "Ranged or Magic."
            ),
            Items.CHAMPION_SCROLL_6801 to listOf(
                "this fight you're only allowed to use Magic attacks, no",
                "Melee or Ranged."
            ),
            Items.CHAMPION_SCROLL_6802 to listOf(
                "this fight you're not allowed to use any Melee attacks."
            ),
            Items.CHAMPION_SCROLL_6803 to listOf(
                "this fight you're not allowed to use any Special Attacks."
            ),
            Items.CHAMPION_SCROLL_6804 to listOf(
                "this fight you're not allowed to use any Ranged attacks."
            ),
            Items.CHAMPION_SCROLL_6805 to listOf(
                "this fight you're allowed to use any Weapons or Armour."
            ),
            Items.CHAMPION_SCROLL_6806 to listOf(
                "this fight you're only allowed to use Ranged attacks, no",
                "Melee or Magic."
            ),
            Items.CHAMPION_SCROLL_6807 to listOf(
                "this fight you're not allowed to use any Magic attacks."
            )
        )

        private val endingLine = "Do you still want to proceed?"
    }

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.LARXUS_3050)

        if (!challengeStart) return

        when (stage) {
            0 -> {
                face(findNPC(NPCs.LARXUS_3050)!!, player!!, 1)

                val scrollEntry = challengeTexts.entries.firstOrNull { (itemId, _) ->
                    inInventory(player!!, itemId)
                }

                if (scrollEntry != null) {
                    val fullText = baseText + scrollEntry.value + listOf(endingLine)
                    npc(*fullText.toTypedArray())
                    stage = 1
                } else if (getAttribute(player!!, GameAttributes.ACTIVITY_CHAMPION_BOSS_CHALLENGE, false)) {
                    options("Yes, let me at him!", "No, thanks I'll pass.").also { stage = 2 }
                } else {
                    end()
                    sendMessage(player!!, "Nothing interesting happens.")
                }
            }

            1 -> options("Yes, let me at him!", "No, thanks I'll pass.").also { stage = 2 }

            2 -> when (buttonID) {
                1 -> playerl("Yes, let me at him!").also { stage = 3 }
                2 -> playerl("No, thanks I'll pass.").also { stage = END_DIALOGUE }
            }

            3 -> npcl("Your challenger is ready, please go down through the trapdoor when you're ready.").also { stage = 4 }

            4 -> {
                end()
                setAttribute(player!!, GameAttributes.ACTIVITY_CHAMPION_CHALLENGE, true)
            }
        }
    }
}