package content.minigame.sorceressgarden.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SorceressDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("What are you doing in my house?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "None of your business!",
                    "I'm here to kill you!",
                    "Can I have some sq'irks please?",
                    "I'm just passing by.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FURIOUS, "None of your business!").also { stage++ }
                    2 -> player("I'm here to kill you!").also { stage = 5 }
                    3 -> player("Can I have some sq'irks please?").also { stage = 7 }
                    4 -> player("I'm just passing by.").also { stage = END_DIALOGUE }
                }

            2 -> player("I go where I like and do what I like.").also { stage++ }
            3 -> npc("Not in my house. Be gone!").also { stage++ }
            4 -> {
                end()
                teleport()
            }

            5 -> npc("I think not!").also { stage++ }
            6 -> {
                end()
                teleport()
            }

            7 -> npc("What do you want them for?").also { stage++ }
            8 -> player("Someone asked me to bring them some.").also { stage++ }
            9 -> npc("Who?").also { stage++ }
            10 ->
                player(
                    "<col=0000FF>You find yourself compelled to answer truthfully:</col>",
                    "Osman.",
                ).also { stage++ }

            11 ->
                npc(
                    "In that case I'm sorry, you can't. I have had a falling",
                    "out with him recently and would rather not oblige him.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SORCERESS_5531)
    }

    private fun teleport() {
        sendChat(npc, "Be gone intruder!")
        lock(player, 2)
        submitWorldPulse(
            object : Pulse(2, player) {
                override fun pulse(): Boolean {
                    unlock(player)
                    teleport(player!!, Location(3321, 3143, 0))
                    return true
                }
            },
        )
    }
}
