package content.region.misc.dialogue.keldagrim

import core.api.findNPC
import core.api.sendChat
import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class HegirDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val forceChat =
        arrayOf(
            "No need to shout!",
            "Calm down dear.",
            "I don't see why.",
            "Yes you did!",
            "I did not!",
            "What did you do that for?",
        )

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_ASKING, "Hello, can I ask you a question?")
        stage = 1
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
                    NPCs.HAERA_2189,
                    "Oh yes, young fellow? Yes, what do you want?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            2 ->
                sendNPCDialogue(
                    player,
                    NPCs.HAERA_2189,
                    "Who's that you're talking to Hegir?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            3 -> npc(FaceAnim.OLD_NORMAL, "Let me see, he appears to be a human of some sort.").also { stage++ }
            4 -> npc(FaceAnim.OLD_NORMAL, "What was your name again, human?").also { stage++ }
            5 -> player("${player.username}.").also { stage++ }
            6 -> npc(FaceAnim.OLD_NORMAL, "It's ${player.username}!").also { stage++ }
            7 ->
                sendNPCDialogue(
                    player,
                    NPCs.HAERA_2189,
                    "Well ask him what he wants then!",
                    FaceAnim.OLD_NORMAL,
                ).also { stage++ }
            8 -> npc(FaceAnim.OLD_NORMAL, "What do you want, human?").also { stage++ }
            9 -> player("Errr... I don't know... A quest, maybe?").also { stage++ }
            10 -> npc(FaceAnim.OLD_NORMAL, "He says he's looking for his quest!").also { stage++ }
            11 ->
                sendNPCDialogue(
                    player,
                    NPCs.HAERA_2189,
                    "What makes him think he lost his quest in here?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            12 -> npc(FaceAnim.OLD_NORMAL, "What makes you think you lost your quest in here?").also { stage++ }
            13 -> player("No no, I mean, do you have a task of", "some sort for me?").also { stage++ }
            14 -> npc(FaceAnim.OLD_NORMAL, "Now he says he wants a task!").also { stage++ }
            15 ->
                sendNPCDialogue(
                    player,
                    NPCs.HAERA_2189,
                    "Stop talking to that human and get back in here before I have a task for you!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            16 -> npc(FaceAnim.OLD_NORMAL, "No need to talk to me like that, Haera.").also { stage++ }
            17 ->
                sendNPCDialogue(
                    player,
                    NPCs.HAERA_2189,
                    "I'll talk to you in whatever way I like!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage++
                }
            18 -> player("I think I hear someone calling my name", "in the distance...").also { stage++ }
            19 -> {
                end()
                sendChat(findNPC(NPCs.HEGIR_2188)!!, forceChat.random())
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HegirDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HEGIR_2188)
    }
}
