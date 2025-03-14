package content.region.misc.dialogue.keldagrim.library

import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LibrarianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_NORMAL, "Welcome to the Keldagrim library, human traveller!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (!isQuestComplete(player, Quests.BETWEEN_A_ROCK)) {
                    options(
                        "Can you tell me more about the library?",
                        "How do these eras correspond with the Ages of the world?",
                    ).also {
                        stage++
                    }
                } else {
                    options(
                        "Can you tell me more about the library?",
                        "What do you know about impenetrable rocks?",
                    ).also { stage++ }
                }
            }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "Can you tell me more about the library?").also { stage++ }
                    2 -> {
                        if (!isQuestComplete(player, Quests.BETWEEN_A_ROCK)) {
                            playerl(
                                FaceAnim.FRIENDLY,
                                "How do these eras correspond with the Ages of the world?",
                            ).also {
                                stage =
                                    7
                            }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "What do you know about impenetrable rocks?").also { stage = 10 }
                        }
                    }
                }
            2 -> npcl(FaceAnim.OLD_NORMAL, "Why, certainly, it is one of my favourite subjects!").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I am Hugi, you see, I am the chief librarian here. Hugi, my parents named me that, it means 'personification of thought' in the ancient dwarvish tongue.",
                ).also {
                    stage++
                }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Quite like the name myself.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "The books in this library have been collected for many centuries now, through various generous donations.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You'll find books dating from many different eras as well, such as the Era of Kings, the Rise of the Consortium and the Era of Prosperity, our current era.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            7 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Oh, they are quite different, and most of them take place in the Fourth Age.",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Since we dwarves hid underground for a long time to sit out the wars that raged above the ground our history books differ slightly from those written by humans.",
                ).also {
                    stage++
                }
            9 -> playerl(FaceAnim.FRIENDLY, "Thanks for the information!").also { stage = END_DIALOGUE }
            10 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You're confusing me, what rock exactly are you talking about?",
                ).also { stage++ }
            11 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "The big one in the mines south of the city. Dondakan's big rock.",
                ).also { stage++ }
            12 -> npcl(FaceAnim.OLD_NORMAL, "I see, I see.").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I heard there was a demon inside there that you banished.",
                ).also { stage++ }
            14 -> playerl(FaceAnim.HALF_ASKING, "Yes, what exactly does that mean anyway?").also { stage++ }
            15 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "There are some demons that we cannot see properly as mortals. They also cannot be fought with normal weapons.",
                ).also {
                    stage++
                }
            16 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "But sometimes these creatures manifest themselves in a solid form in our world. Their avatar.",
                ).also {
                    stage++
                }
            17 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "When you defeat an avatar, you defeat the actual demon behind it. But they don't die, they are banished to where they came from originally.",
                ).also {
                    stage++
                }
            18 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Does that mean I will have to deal with this demon again some time?",
                ).also {
                    stage++
                }
            19 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Doubtful. He will grow stronger and stronger over time, but that's a process that will take a thousand years at least.",
                ).also {
                    stage++
                }
            20 -> npcl(FaceAnim.OLD_NORMAL, "Even dwarves don't live so long.").also { stage++ }
            21 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "No, we won't see him again and the mines are safe to dig for gold now. Of course, that might lead to other problems eventually...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LibrarianDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LIBRARIAN_2165)
    }
}
