package content.region.fremennik.dialogue.rellekka

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FreygerdDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when ((0..3).random()) {
            0 ->
                when (stage) {
                    START_DIALOGUE -> player("How's the King treating you then?").also { stage++ }
                    1 -> npc(FaceAnim.HALF_GUILTY, "Like serfs.").also { stage++ }
                    2 -> player("Serf?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Yes, you know - peons, plebs, the downtrodden. He treats us like his own personal possessions.",
                        ).also {
                            stage++
                        }
                    4 -> player("You should leave this place.").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I keep trying to save up enough to leave, but the King keeps taxing us! We have no money left.",
                        ).also {
                            stage++
                        }
                    6 -> player("Oh dear.").also { stage = END_DIALOGUE }
                }

            1 ->
                when (stage) {
                    START_DIALOGUE -> player("It's a bit grey round here, isn't it?").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "It gets you down after a while, you know. There are 273 shades of grey, you know, and we have them all.",
                        ).also {
                            stage++
                        }
                    2 -> player("That's grey-t.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "That attempt at humour merely made me more depressed. Leave me alone.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            2 ->
                when (stage) {
                    START_DIALOGUE -> player("How are you today?").also { stage++ }
                    1 -> npcl(FaceAnim.HALF_GUILTY, "**sigh**").also { stage++ }
                    2 -> player("That good? Everyone around here seems a little depressed.").also { stage++ }
                    3 -> npcl(FaceAnim.HALF_GUILTY, "**sigh**").also { stage++ }
                    4 -> player("And not particularly talkative.").also { stage++ }
                    5 -> npcl(FaceAnim.HALF_GUILTY, "**sigh**").also { stage++ }
                    6 ->
                        player("I'll leave you to your sighing. It looks like you have plenty to do.").also {
                            stage =
                                END_DIALOGUE
                        }
                }

            3 ->
                when (stage) {
                    START_DIALOGUE -> player("Cheer up! It's not the end of the world.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "I'd prefer that, if it meant I didn't have to talk to people as insanely happy as you.",
                        ).also {
                            stage++
                        }
                    2 -> player("Whoa! I think you need to get out more.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FreygerdDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FREYGERD_1310)
    }
}
