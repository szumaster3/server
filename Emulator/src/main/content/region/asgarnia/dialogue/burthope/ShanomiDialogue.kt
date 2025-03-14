package content.region.asgarnia.dialogue.burthope

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Shanomi dialogue.
 */
@Initializable
class ShanomiDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Greetings " + player.username + ". Welcome you are in the test of", "combat.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What do I do here?",
                    "Where do the machines come from?",
                    "May I claim my tokens please?",
                    "Bye.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What do I do here?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Where do the machines come from?").also { stage = 11 }
                    3 -> {
                        end()
                        player.dialogueInterpreter.open("wg:claim-tokens", npc.id)
                    }
                    4 -> player(FaceAnim.HALF_GUILTY, "Bye.").also { stage = 16 }
                }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "A spare suit of plate armour need you will. Full helm",
                    "plate, leggings and platebody. Placing it in the centre",
                    "of the magical machines you will be doing. KA-POOF!",
                    "The armour it attacks most furiously as if alive! Kill it",
                ).also {
                    stage =
                        -3
                }
            -3 -> npc("you must, yes.").also { stage = 3 }
            3 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "So I use a full set of plate armour on the centre plate of",
                    "the machines and it will animate it? Then I have to kill my",
                    "own armour... how bizarre!",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Yes. It is as you are saying. For this earn tokens you",
                    "will. Also gain experience in combat you will. Trained",
                    "long and hard here have I.",
                ).also {
                    stage++
                }
            5 -> player(FaceAnim.HALF_GUILTY, "You're not from around here are you...?").also { stage++ }
            6 -> npc(FaceAnim.HALF_GUILTY, "It is as you say.").also { stage++ }
            7 -> player(FaceAnim.HALF_GUILTY, "So will I lose my armour?").also { stage++ }
            8 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Lose armour you will if damaged to much it becomes.",
                    "Rare this is, but still possible. If kill you the armour does,",
                    "also lose armour you will.",
                ).also {
                    stage++
                }
            9 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "So, occasionally I might lose a bit because it's being",
                    "bashed about and I'll obviously lose it if I die... that it?",
                ).also {
                    stage++
                }
            10 -> npc(FaceAnim.HALF_GUILTY, "It is as you say.").also { stage = 0 }
            11 -> npc(FaceAnim.HALF_GUILTY, "Make them I did, with magics.").also { stage++ }
            12 -> player(FaceAnim.HALF_GUILTY, "Magic, in the Warrior's Guild?").also { stage++ }
            13 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "A skilled warrior also am I. Harrallak mistakes does not",
                    "make. Potential in my invention he sees and opportunity",
                    "grasps.",
                ).also {
                    stage++
                }
            14 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "I see, so you made the magical machines and Harrallak",
                    "saw how they could be used in the guild to train warrior's",
                    "combat... interesting. Harrallak certainly is an intelligent",
                    "guy.",
                ).also {
                    stage++
                }
            15 -> npc(FaceAnim.HALF_GUILTY, "It is as you say.").also { stage = 0 }
            16 -> npc(FaceAnim.HALF_GUILTY, "Health be with you travelling.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHANOMI_4290)
    }
}
