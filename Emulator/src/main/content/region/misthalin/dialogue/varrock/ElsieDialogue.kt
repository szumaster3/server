package content.region.misthalin.dialogue.varrock

import core.api.inInventory
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ElsieDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inInventory(player, Items.CUP_OF_TEA_712)) {
            npcl(FaceAnim.FRIENDLY, "Hello dearie! What can old Elsie do for you?")
        } else {
            npcl(FaceAnim.FRIENDLY, "Ooh - that looks like a lovely cup of tea, dearie. Is it for me?").also {
                stage = 10
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                showTopics(
                    Topic(FaceAnim.ASKING, "What are you making?", 1),
                    Topic(FaceAnim.ASKING, "Can you tell me a story?", 3),
                    Topic(FaceAnim.ASKING, "Can you tell me how to get rich?", 5),
                    Topic(FaceAnim.NEUTRAL, "I've got to go.", END_DIALOGUE),
                )

            1 -> npcl(FaceAnim.FRIENDLY, "I'm knitting a new stole for Father Lawrence downstairs.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "He could do with something to keep his neck warm, standing in " +
                        "that draughty old church all day.",
                ).also { stage = START_DIALOGUE }

            3 -> {
                if (!inInventory(player, Items.CUP_OF_TEA_712)) {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Maybe I could tell you a story if you'd fetch me a nice cup of tea.",
                    ).also { stage++ }
                } else {
                    npcl(
                        FaceAnim.HALF_THINKING,
                        "Perhaps... Can I have that lovely cup of tea you have over there?",
                    ).also { stage = 10 }
                }
            }

            4 -> playerl(FaceAnim.ROLLING_EYES, "I'll think about it.").also { stage = START_DIALOGUE }
            5 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, dearie, I am probably not the best person to ask about money, but I think the best thing " +
                        "would be for you to get a good trade.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.HALF_WORRIED,
                    "If you've got a good trade you can earn your way. That's what my old father would tell me.",
                ).also { stage++ }

            7 ->
                npcl(
                    FaceAnim.HALF_WORRIED,
                    "Saradomin rest his soul. I hear people try to get rich by fighting in the Wilderness north of here " +
                        "or the Duel Arena in the south... But that's no way for honest folk to earn a living!",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.HALF_WORRIED,
                    "So get yourself a good trade, and keep working at it. There's always folks wanting to buy ore and food around here.",
                ).also { stage++ }

            9 -> playerl(FaceAnim.ROLLING_EYES, "Thanks, old woman.").also { stage = START_DIALOGUE }
            10 ->
                showTopics(
                    Topic(FaceAnim.HAPPY, "Yes, you can have it.", 12),
                    Topic(FaceAnim.ANNOYED, "No, keep your hands off my tea.", 11),
                )

            11 ->
                npcl(
                    FaceAnim.SAD,
                    "Aww. Maybe another time, then... Anyway, what can old Elsie do for you?",
                ).also { stage = START_DIALOGUE }

            12 ->
                sendItemDialogue(
                    player,
                    Items.CUP_OF_TEA_712,
                    "Elsie takes a sip from the cup of tea.",
                ).also {
                    stage++
                    removeItem(player, Items.CUP_OF_TEA_712)
                }

            13 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Ahh, there's nothing like a nice cuppa tea. You know what, I'll tell you a story as a thank-you for that lovely tea...",
                ).also { stage++ }

            14 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "A long time ago, when I was a little girl, there was a handsome young man living in Varrock...",
                ).also { stage++ }

            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I saw him here in the church quite often. Everyone said he was going to become a priest and " +
                        "we girls were so sad about that...",
                ).also { stage++ }

            16 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "But young Dissy - that was the young man's nickname - he was a wild young thing.",
                ).also { stage++ }

            17 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "One night he gathered some lads together, and after the evening prayer-meeting " +
                        "they all put their masks on...",
                ).also { stage++ }

            18 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Then, they snuck down to the temple in the south of the city - the evil one. " +
                        "The next day, there was quite a hubbub...",
                ).also { stage++ }

            19 ->
                npcl(
                    FaceAnim.LAUGH,
                    "The guards told us that someone had painted 'Saradomin pwns' on the wall of Zamorakian temple!",
                ).also { stage++ }

            20 ->
                npcl(
                    FaceAnim.HALF_THINKING,
                    "Now, we'd always been taught to keep well away from that dreadful place...",
                ).also { stage++ }

            21 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "But it really did us all good to see someone wasn't afraid of the scum who live at that end of town.",
                ).also { stage++ }

            22 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Old Father Packett was furious, but Dissy just laughed it off. " +
                        "Dissy left town after that, saying he wanted to see the world.",
                ).also { stage++ }

            23 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "It was such a shame, he had the most handsome... Shoulders...",
                ).also { stage++ }

            24 ->
                npcl(
                    FaceAnim.HAPPY,
                    "One day, a young man came here looking for stories about Dissy - of course, " +
                        "that's not his proper name, but his friends called him Dissy - and I told " +
                        "him that one. ",
                ).also { stage++ }

            25 ->
                npcl(
                    FaceAnim.HALF_WORRIED,
                    "He said Dissy had become a really famous man and there was going to be a book about him. " +
                        "That's good and all, but I do wish Dissy had just come back to Varrock. I did miss him so much... ",
                ).also { stage++ }

            26 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Well, until I met my Freddie and we got married. But that's another story.",
                ).also { stage++ }

            27 ->
                playerl(FaceAnim.FRIENDLY, "Thank you. I'll leave you to your knitting now.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ElsieDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELSIE_5915)
    }
}
