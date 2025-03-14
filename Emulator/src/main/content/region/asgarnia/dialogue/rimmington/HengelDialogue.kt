package content.region.asgarnia.dialogue.rimmington

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager.getLocalNpcs
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HengelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("What are you doing here?").also { stage++ }
            1 ->
                options(
                    "I'm just wandering around.",
                    "I was hoping you'd give me some free stuff.",
                    "I've come to kill you.",
                ).also {
                    stage++
                }

            2 ->
                when (buttonId) {
                    1 -> player("I'm just wondering around.").also { stage++ }
                    2 -> player("I was hoping you'd give me some free stuff.").also { stage = 7 }
                    3 -> player("I've come to kill you.").also { stage = 9 }
                }

            3 -> npc("You do realise you're wandering around in my house?").also { stage++ }
            4 -> player("Yep.").also { stage++ }
            5 -> npc("Well please get out!").also { stage++ }
            6 -> player("Sheesh, keep your wig on!").also { stage = END_DIALOGUE }
            7 -> npc("No, I jolly well wouldn't!", "Get out of my house").also { stage++ }
            8 -> player("Meanie!").also { stage = END_DIALOGUE }
            9 -> {
                npc.sendChat("Aaaaarrgh!")
                for (npc1 in getLocalNpcs(player)) {
                    if (npc1.name.equals("anja", ignoreCase = true)) {
                        npc1.sendChat("Eeeek!")
                        break
                    }
                }
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HENGEL_2683)
    }
}
