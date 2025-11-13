package content.region.kandarin.gnome_stronghold.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Gnome Woman dialogue.
 */
class GnomeWomanDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.GNOME_WOMAN_168)
        when (stage) {
            0 -> {
                playerl(FaceAnim.ASKING, "Hello.")
                stage = when (Random.nextInt(6)) {
                    0 -> 1  // Option 1
                    1 -> 10 // Option 2
                    2 -> 18 // Option 3
                    3 -> 26 // Option 4
                    4 -> 32 // Option 5
                    else -> 38 // Option 6
                }
            }
            // Option 1
            2 -> npcl(FaceAnim.OLD_DEFAULT, "Hello adventurer. Here are some wise words:").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "OK.").also { stage++ }
            4 -> npcl(FaceAnim.OLD_DEFAULT, "Happiness is inward and not outward. So it does not depend on what we have but on what we are!").also { stage = 50 }

            // Option 2
            10 -> npcl(FaceAnim.OLD_DEFAULT, "Hi. I've never seen so many humans in my life.").also { stage++ }
            11 -> playerl(FaceAnim.FRIENDLY, "I've never seen so many gnomes!").also { stage++ }
            12 -> npcl(FaceAnim.OLD_DEFAULT, "So we're both learning.").also { stage = 50 }

            // Option 3
            18 -> npcl(FaceAnim.OLD_DEFAULT, "Hello traveller. Are you eating properly? You look tired.").also { stage++ }
            19 -> playerl(FaceAnim.FRIENDLY, "I think so.").also { stage++ }
            20 -> npcl(FaceAnim.OLD_DEFAULT, "Here, get this worm down you. It'll do you the world of good.").also { stage++ }
            21 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage = 50 }

            // Option 4
            26 -> npcl(FaceAnim.OLD_DEFAULT, "Well good day to you kind sir. Are you new to these parts?").also { stage++ }
            27 -> playerl(FaceAnim.FRIENDLY, "Kind of.").also { stage++ }
            28 -> npcl(FaceAnim.OLD_DEFAULT, "Well if you're looking for a good night out: Blurberry's cocktail bar is great!").also { stage = 50 }

            // Option 5
            32 -> npcl(FaceAnim.OLD_DEFAULT, "Some people grumble because roses have thorns. I'm thankful that thorns have roses!").also { stage++ }
            33 -> playerl(FaceAnim.FRIENDLY, "Good attitude!").also { stage = 50 }

            // Option 6
            38 -> playerl(FaceAnim.FRIENDLY, "How are you?").also { stage++ }
            39 -> npcl(FaceAnim.OLD_DEFAULT, "Not bad, a little worn out.").also { stage++ }
            40 -> playerl(FaceAnim.FRIENDLY, "Maybe you should have a lie down.").also { stage++ }
            41 -> npcl(FaceAnim.OLD_DEFAULT, "With three kids to feed I've no time for naps!").also { stage++ }
            42 -> playerl(FaceAnim.FRIENDLY, "Sounds like hard work!").also { stage++ }
            43 -> npcl(FaceAnim.OLD_DEFAULT, "It is but they're worth it.").also { stage = 50 }

            50 -> end()
        }
    }
}
