package content.region.asgarnia.dialogue.rimmington

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.RegionManager.getLocalNpcs
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class AnjaDialogue(
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
            0 -> npc("Hello sir. What are you doing in my house?").also { stage++ }
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
                    1 -> player("I'm just wondering around.").also { stage = 3 }
                    2 -> player("I was hoping you'd give me some free stuff.").also { stage = 10 }
                    3 -> player("I've come to kill you.").also { stage = 13 }
                }

            3 -> npc("Oh dear are you lost?").also { stage++ }
            4 -> options("Yes, I'm lost.", "No, I know where I am.").also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> player("Yes, I'm lost.").also { stage++ }
                    2 -> player("No I know where I am.").also { stage = 8 }
                }

            6 ->
                npc(
                    "Okay, just walk north-east when you leave this house,",
                    "and soon you'll reach the big city of Falador.",
                ).also {
                    stage++
                }
            7 -> player("Thanks a lot.").also { stage = END_DIALOGUE }
            8 -> npc("Oh? Well, would you mind wandering somewhere else?", "This is my house.").also { stage++ }
            9 -> player("Meh!").also { stage = END_DIALOGUE }
            10 -> {
                val dialogues = arrayOf("Do you REALLY need it", "I don't have much on me...", "I don't know...")
                npc(dialogues[RandomFunction.random(0, 2)]).also { stage++ }
            }

            11 ->
                player(
                    FaceAnim.ASKING,
                    "I promise I'll stop bothering you!",
                    "Pleeease!",
                    "Pwetty pleathe wiv thugar on top!",
                ).also {
                    stage++
                }

            12 -> {
                end()
                npc("Oh, alright. Here you go.")
                player.inventory.add(Item(995, RandomFunction.random(1, 3)))
            }

            13 -> {
                npc.sendChat("Eeeek!")
                for (npc1 in getLocalNpcs(player)) {
                    if (npc1.name.equals("Hengel", ignoreCase = true)) {
                        npc1.sendChat("Aaaaarrgh!")
                        break
                    }
                }
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ANJA_2684)
    }
}
