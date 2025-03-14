package content.region.misc.dialogue.mosle

import core.api.inInventory
import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class PatchyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
            npcl(FaceAnim.FRIENDLY, "Arr? Be ye wantin' te go on account with our gang o' fillibusters?").also {
                stage = 1
            }
        } else {
            npcl(FaceAnim.FRIENDLY, "Hello there! Can I sew ye somethin' tegether?").also { stage = 4 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The powder monkey be takin' a caulk after gettin' rowdy on bumboo, so there be plenty of room for ye.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.STRUGGLE, "Riiiiight...").also { stage++ }
            3 -> playerl(FaceAnim.STRUGGLE, "I'll just be over here if you need me.").also { stage = END_DIALOGUE }
            4 -> playerl(FaceAnim.HALF_ASKING, "What do you mean?").also { stage++ }
            5 ->
                npc(
                    "Well, ye see it works like this; I can sew yer eye",
                    "patches onto yer pirate hats or pirate bandanas.",
                ).also {
                    stage++
                }
            6 -> npc("Just pirate bandanas mind ye, I hate the feel of", "snakeskin. (Shudder)").also { stage++ }
            7 -> npc("It costs 500 gold te do it, and 600 gold te get them", "separated.").also { stage++ }
            8 ->
                player(
                    FaceAnim.HALF_ASKING,
                    "Ok, why does it cost more to get them separated than",
                    "put together?",
                ).also {
                    stage++
                }
            9 -> npc("Wear and tear no scissors.").also { stage++ }
            10 ->
                npc(
                    "On another note, I also sew crab claw gauntlets on to",
                    "pirate hooks, highwayman masks to black cavaliers, and",
                    "mime masks to black berets, If ye have them with ye.",
                ).also {
                    stage++
                }
            11 -> npc(FaceAnim.HALF_ASKING, "So, do ye have anything ye wants doin?").also { stage++ }
            12 -> options("Yes", "No").also { stage++ }
            13 ->
                when (buttonId) {
                    1 -> player("Yes, please!").also { stage++ }
                    2 -> player("No").also { stage = 100 }
                }
            14 -> npc("Now, do you want to be sewing items together or", "separating them?").also { stage++ }
            15 -> options("Sew together", "Separate").also { stage++ }
            16 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HAPPY, "I'd like something sewn together.").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "I need something separated.").also { stage = 19 }
                }
            17 -> npc("Aye, pick out what items ye be wantin' sewn together.").also { stage++ }
            18 -> {
                end()
                openInterface(player, Components.SEW_INTERFACE_419)
            }
            19 ->
                npc("Aye, aye. Just use yer item on me and I'll see", "if I can separate them.").also {
                    stage =
                        END_DIALOGUE
                }
            100 -> npc("Fair enough. See you around!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return PatchyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PATCHY_4359)
    }
}
