package content.region.misthalin.dialogue.varrock

import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class VarrockEastBartenderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "What can I do yer for?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "A glass of your finest ale please.",
                    "Can you recommend where an adventurer might make his fortune?",
                    "Do you know where I can get some good equipment?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "A glass of your finest ale please.").also { stage = 10 }
                    2 ->
                        player(
                            FaceAnim.ASKING,
                            "Can you recommend where an adventurer might make",
                            "his fortune?",
                        ).also { stage = 20 }

                    3 -> player(FaceAnim.ASKING, "Do you know where I can get some good equipment?").also { stage = 30 }
                }

            10 -> npc(FaceAnim.FRIENDLY, "No problemo. That'll be 2 coins.").also { stage++ }
            11 ->
                if (player.inventory.contains(995, 2)) {
                    player.inventory.remove(Item(995, 2))
                    player.inventory.add(Item(1917, 1))
                    end()
                    sendMessage(player, "You buy a pint of beer.")
                } else {
                    end()
                    sendMessage(player, "You need 2 coins to buy ale.")
                }

            20 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Ooh I don't know if I should be giving away information,",
                    "makes the computer game too easy.",
                ).also { stage++ }

            21 ->
                options(
                    "Oh ah well...",
                    "Computer game? What are you talking about?",
                    "Just a small clue?",
                ).also { stage++ }

            22 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Oh ah well...").also { stage = END_DIALOGUE }
                    2 -> player(FaceAnim.ASKING, "Computer game? What are you talking about?").also { stage = 160 }
                    3 -> player(FaceAnim.THINKING, "Just a small clue?").also { stage = 170 }
                }

            160 ->
                npc(
                    FaceAnim.THINKING,
                    "This world around us... is a computer game.... called",
                    settings!!.name + ".",
                ).also { stage++ }

            161 ->
                player(
                    FaceAnim.HALF_THINKING,
                    "Nope, still don't understand what you are talking about.",
                    "What's a computer?",
                ).also { stage++ }

            162 ->
                npc(
                    FaceAnim.THINKING,
                    "It's a sort of magic box thing, which can do all sorts of",
                    "stuff.",
                ).also { stage++ }

            163 -> player(FaceAnim.WORRIED, "I give up. You're obviously completely mad.").also { stage = END_DIALOGUE }
            30 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Well, there's the sword shop across the road, or there's",
                    "also all sorts of shops up around the market.",
                ).also { stage = END_DIALOGUE }

            170 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Go and talk to the bartender at the Holly Boar Inn, he",
                    "doesn't seem to mind giving away clues.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return VarrockEastBartenderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_733)
    }
}
