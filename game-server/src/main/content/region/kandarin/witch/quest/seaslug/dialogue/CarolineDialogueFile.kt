package content.region.kandarin.witch.quest.seaslug.dialogue

import core.api.finishQuest
import core.api.getQuestStage
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Caroline dialogue extension.
 *
 * # Relations
 * - [Sea Slug quest][content.region.kandarin.witch.quest.seaslug.SeaSlug]
 */
class CarolineDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.SEA_SLUG)
        npc = NPC(NPCs.CAROLINE_696)
        when (questStage) {
            // Concerned Caroline.
            in 0..1 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FaceAnim.HALF_CRYING, "Is there any chance you could help me?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "What's wrong?").also { stage++ }
                    3 -> npc(FaceAnim.WORRIED, "It's my husband, he works on a fishing platform. Once", "a month he takes our son, Kennith, out with him.").also { stage++ }
                    4 -> npc(FaceAnim.WORRIED, "They usually write to me regularly, but I've heard", "nothing all week. It's very strange.").also { stage++ }
                    5 -> player(FaceAnim.ASKING, "Maybe the post was lost!").also { stage++ }
                    6 -> npc(FaceAnim.WORRIED, "Maybe, but no-one's heard from the other fishermen on", "the platform. Their families are becoming quite", "concerned.").also { stage++ }
                    7 -> npc(FaceAnim.ASKING, "Is there any chance you could visit the platform and", "find out what's going on?").also { stage++ }
                    8 -> options("I suppose so, how do I get there?", "I'm sorry, I'm too busy.").also { stage++ }
                    9 -> when (buttonID) {
                        1 -> player(FaceAnim.ASKING, "I suppose so, how do I get there?").also { stage++ }
                        2 -> player(FaceAnim.NEUTRAL, "I'm sorry, I'm too busy.").also { stage = END_DIALOGUE }
                    }
                    10 -> npc(FaceAnim.HALF_GUILTY, "That's very good of you ${player!!.username}. My friend Holgart", "will take you there.").also { stage++ }
                    11 -> player("Ok, I'll go and see if they're ok.").also { stage++ }
                    12 -> npc(FaceAnim.HALF_GUILTY, "I'll reward you for your time. It'll give me peace of", "mind to know Kennith and my husband, Kent, are safe.").also { stage++ }
                    13 -> {
                        end()
                        setQuestStage(player!!, Quests.SEA_SLUG, 2)
                    }
                }
            }

            // Concerned Caroline: Talking to Caroline again.
            in 2..49 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.HAPPY, "Brave ${player!!.name}, have you any news about my son and his father?").also { stage++ }
                    1 -> player(FaceAnim.NEUTRAL, "I'm working on it now Caroline.").also { stage++ }
                    2 -> npcl(FaceAnim.FRIENDLY, "Please bring them back safe and sound.").also { stage++ }
                    3 -> player(FaceAnim.FRIENDLY, "I'll do my best.").also { stage = END_DIALOGUE }
                }
            }

            /*
             * Pearls of Wisdom - finish sea slug quest.
             */

            in 50..99 -> {
                when (stage) {
                    0 -> npc("Brave ${player!!.username}, you've returned!").also { stage++ }
                    1 -> npc(
                        "Kennith told me about the strange goings-on at the",
                        "platform. I had no idea it was so serious.",
                    ).also {
                        stage++
                    }

                    2 -> npc("I could have lost my son and my husband if it wasn't", "for you.").also { stage++ }
                    3 -> player("We found Kent stranded on an island.").also { stage++ }
                    4 -> npc(
                        "Yes. Holgart told me and sent a rescue party out.",
                        "Kent's back home now, resting with Kennith. I don't",
                        "think he'll be doing any fishing for a while.",
                    ).also {
                        stage++
                    }

                    5 -> npc(
                        "Here, take these Oyster pearls as a reward. They're",
                        "worth quite a bit and can be used to make lethal",
                        "crossbow bolts.",
                    ).also {
                        stage++
                    }

                    6 -> player("Thanks!").also { stage++ }
                    7 -> npc("Thank you. Take care of yourself ${player!!.username}.").also { stage++ }
                    8 -> {
                        end()
                        finishQuest(player!!, Quests.SEA_SLUG)
                    }
                }
            }
        }
    }
}
