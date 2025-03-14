package content.region.kandarin.dialogue.stronghold

import core.api.anyInInventory
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GnomeWaiterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val gnomeFood =
        intArrayOf(
            Items.PREMADE_WM_BATTA_2219,
            Items.PREMADE_TD_BATTA_2221,
            Items.PREMADE_C_PLUST_BATTA_2223,
            Items.PREMADE_FRT_BATTA_2225,
            Items.PREMADE_VEG_BATTA_2227,
            Items.PREMADE_CHOC_BOMB_2229,
            Items.PREMADE_TTL_2231,
            Items.PREMADE_WORM_HOLE_2233,
            Items.PREMADE_VEG_BALL_2235,
            Items.PREMADE_WM_CRUN_2237,
            Items.PREMADE_CH_CRUNCH_2239,
            Items.PREMADE_SY_CRUNCH_2241,
            Items.PREMADE_TD_CRUNCH_2243,
        )

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Hello " + (if (player.isMale) "sir" else "madam") +
                        ", can I tempt you with any of the dishes on our new menu?",
                ).also { stage++ }
            1 ->
                if (!anyInInventory(player, *gnomeFood)) {
                    options("I'll take a look.", "Not really.").also { stage++ }
                } else {
                    options(
                        "I'll take a look.",
                        "Not really.",
                        "Actually I'd like to sell some dishes.",
                    ).also { stage++ }
                }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I'll take a look.").also { stage = 4 }
                    2 -> playerl(FaceAnim.NEUTRAL, "Not really.").also { stage = 3 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Actually I'd like to sell some dishes.").also { stage = 7 }
                }
            3 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "That's your choice " + (if (player.isMale) "sir" else "madam") + ", enjoy your stay.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I hope you like what you see. Although all these premade dishes are good to eat - they were made by the last human assistant chef. He wasn't up to Chef's exacting standards - and you may find that they will",
                ).also {
                    stage++
                }
            5 -> npcl(FaceAnim.OLD_NORMAL, "not be accepted by some people as the 'real' thing.").also { stage++ }
            6 -> end().also { openNpcShop(player, npc.id) }
            7 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    " Mr Gianne is the one to talk to if you want to sell any dishes you've made yourself. He is the owner and head chef of this establishment. If on the other hand you are more interested in making food to deliver",
                ).also {
                    stage++
                }
            8 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "which can be quite lucrative, you should talk to Mr Giannes son Aluft Gianne jnr. Just don't call him Lufty, he doesn't like it.",
                ).also {
                    stage++
                }
            9 -> playerl(FaceAnim.FRIENDLY, "Thank you.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GnomeWaiterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GNOME_WAITER_851)
    }
}
