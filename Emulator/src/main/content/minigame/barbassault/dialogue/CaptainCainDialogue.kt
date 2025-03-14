package content.minigame.barbassault.dialogue

import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class CaptainCainDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello. What's this place?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Why you wretched Son-of-a-Big-Nosed-Warty-Smelly-Goblin! How dare you speak to a commanding officer like that?",
                ).also { stage++ }

            1 -> playerl(FaceAnim.FRIENDLY, "I...err... What?").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Stand straight! Chest out, shoulders back!").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Okay, okay.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "You'll address me as captain!").also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY, "Yea...yes, captain!").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Better. Now what is a low-life, pathetic excuse for a human being doing sniffing around my arena?",
                ).also { stage++ }

            7 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Exploring, captain. Looked like a chance for combat, sir!",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "And you think you have what is necessary to take on the Penance? No chance.",
                ).also { stage++ }

            9 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "With respect, captain, I don't understand what this place is.",
                ).also { stage++ }

            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You really are useless, aren't you? Very well, make the most of this because I don't suffer idiots easily! This is the Barbarian Assault arena. You want to sign up with us?",
                ).also { stage++ }

            11 -> options("Sir, yes sir!", "Nah, sounds boring.").also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Sir, yes sir!").also { stage++ }
                    2 -> end()
                }

            13 -> npcl(FaceAnim.FRIENDLY, "Then listen closely.").also { stage++ }
            14 ->
                openInterface(player!!, Components.BARBASSAULT_TUTORIAL_496).also {
                    end()
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_CAIN_5030)
    }
}
