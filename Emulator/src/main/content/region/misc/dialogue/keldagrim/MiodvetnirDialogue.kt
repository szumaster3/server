package content.region.misc.dialogue.keldagrim

import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MiodvetnirDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "Oh look, a human!").also { stage = 1 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNU_1848,
                    "A human? Can we eat it?",
                    FaceAnim.OLD_DEFAULT,
                ).also { stage++ }
            2 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNI_1849,
                    "No, you rock brain! Humans are our friends!",
                    FaceAnim.OLD_DEFAULT,
                ).also {
                    stage++
                }
            3 -> player("Hello, excuse me...").also { stage++ }
            4 ->
                sendNPCDialogue(
                    player,
                    NPCs.MIODVETNIR_1847,
                    "Not sure if I would call them our friends... Not enemies either, of course.",
                    FaceAnim.OLD_DEFAULT,
                ).also {
                    stage++
                }
            5 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNU_1848,
                    "Not like the trolls. We hate trolls.",
                    FaceAnim.OLD_DEFAULT,
                ).also {
                    stage++
                }
            6 -> player("Excuse me, I just want to-").also { stage++ }
            7 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNI_1849,
                    "Nothing's worse than a troll.",
                    FaceAnim.OLD_DEFAULT,
                ).also { stage++ }
            8 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNU_1848,
                    "Except discovering that your pickaxe's gone blunt just as you-",
                    FaceAnim.OLD_DEFAULT,
                ).also {
                    stage++
                }
            9 -> player("Excuse me!!!").also { stage++ }
            10 ->
                sendNPCDialogue(
                    player,
                    NPCs.MIODVETNIR_1847,
                    "Why, hello, yes, what is it?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            11 -> player("I er... well, who are you?").also { stage++ }
            12 -> sendNPCDialogue(player, NPCs.DERNI_1849, "I'm Derni.", FaceAnim.OLD_NORMAL).also { stage++ }
            13 -> sendNPCDialogue(player, NPCs.DERNU_1848, "I'm Dernu.", FaceAnim.OLD_NORMAL).also { stage++ }
            14 ->
                sendNPCDialogue(
                    player,
                    NPCs.MIODVETNIR_1847,
                    "And I'm Miodvetnir. These are my brothers. And who might you be?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            15 -> player("My name is ${player.username}.").also { stage++ }
            16 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNI_1849,
                    "Pleased to meet you, ${player.username}!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            17 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNU_1848,
                    "I say, we don't get to see a hummon down here very often!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            18 ->
                sendNPCDialogue(
                    player,
                    NPCs.MIODVETNIR_1847,
                    "It's a human, you metal head! You should pay more attention!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            19 -> player(FaceAnim.HALF_ASKING, "So, can I ask you dwarves a few questions?").also { stage++ }
            20 ->
                sendNPCDialogue(
                    player,
                    NPCs.MIODVETNIR_1847,
                    "Of course, we love answering questions!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            21 ->
                sendNPCDialogue(
                    player,
                    NPCs.DERNU_1848,
                    "We're very good at answering questions!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            22 ->
                sendNPCDialogue(player, NPCs.DERNI_1849, "Fire away!", FaceAnim.OLD_NORMAL).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MiodvetnirDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MIODVETNIR_1847, NPCs.DERNU_1848, NPCs.DERNI_1849)
    }
}
