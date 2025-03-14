package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class VictoriaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.THE_LOST_TRIBE) == 10) {
            player("Do you know what happened in the cellar?").also { stage = 19 }
        } else {
            player("Good day.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "To you too, traveller. I am Victoria. Tell me, have you seen my brother, Lachtopher, around the town?",
                ).also {
                    stage++
                }
            1 -> options("Yes, I've seen Lachtopher.", "No, I haven't seen him.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Ah, he'll have asked you for money, no doubt. I hope you didn't give him any.",
                        ).also {
                            stage =
                                5
                        }
                    2 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Well, if you do meet him, he'll ask you for money, no doubt. Please don't give him any.",
                        ).also {
                            stage =
                                5
                        }
                }
            3 ->
                options(
                    "No, I didn't give him a single coin.",
                    "Yes, I loaned him money, just like he asked.",
                ).also { stage++ }
            4 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Oh, good! If you had, then you would never have got it back.",
                        ).also {
                            stage =
                                5
                        }
                    2 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Oh dear. I'm sorry to tell you this, but that's the last you'll see of that money.",
                        ).also {
                            stage =
                                5
                        }
                }
            5 -> playerl(FaceAnim.HALF_ASKING, "Why not?").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Sorry to tell you this, but if you lend him money you'll never see it again.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "My brother is such a waste of space. I've been lending him things for years and he never gives them back. Yes, but it never used to be this bad. You see...",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Lachtopher used to live on the east side of the river, before it was overrun with goblins. Although he didn't have a steady job, he used to help out around farms when he needed cash.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Then, one day, the Duke told us it was no longer safe to live on the east riverbank, so some villagers had to move across here. With no money for lodgings, and nowhere else to go, Lachtopher came to live with me.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I've only a small house, so he sleeps downstairs on the floor.",
                ).also { stage++ }
            11 -> playerl(FaceAnim.HALF_GUILTY, "Goodness. That sounds quite uncomfortable.").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Not uncomfortable enough, it seems. I thought he'd only be staying for a couple of weeks, just until he'd got some money together, but he's been here for ages now.",
                ).also {
                    stage++
                }
            13 -> playerl(FaceAnim.HALF_ASKING, "So, why not just throw him out on to the streets?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, no! I couldn't do that to my brother. Besides, my parents taught me to support and care for those in need. I'm sure that, if I try hard enough, I can change my brother's ways.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "That doesn't mean he's having any more money out of me, however. He can have a roof over his head, but that's all.",
                ).also {
                    stage++
                }
            16 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Good luck with that. I don't think Lachtopher deserves a sister like you.",
                ).also {
                    stage++
                }
            17 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Such kind words. Thank you. Remember: don't give him any money - tell him to get a job instead.",
                ).also {
                    stage++
                }
            18 -> playerl(FaceAnim.HALF_GUILTY, "Okay, I'll try to remember that.").also { stage = END_DIALOGUE }
            19 -> npcl(FaceAnim.HALF_GUILTY, "No. What happened in the cellar?").also { stage++ }
            20 -> playerl(FaceAnim.HALF_GUILTY, "Well, part of the wall has collapsed.").also { stage++ }
            21 -> npcl(FaceAnim.HALF_GUILTY, "Good heavens! You'd better find out what happened!").also { stage++ }
            22 -> playerl(FaceAnim.HALF_GUILTY, "Well, yes, that's what I'm doing.").also { stage++ }
            23 -> npcl(FaceAnim.HALF_GUILTY, "Good! I know we're safe in your hands.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VICTORIA_7872)
    }
}
