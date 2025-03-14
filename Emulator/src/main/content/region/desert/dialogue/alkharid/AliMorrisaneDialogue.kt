package content.region.desert.dialogue.alkharid

import core.api.interaction.openNpcShop
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class AliMorrisaneDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Good day and welcome back to Al Kharid.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Hello to you too.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.ASKING,
                    "My name is Ali Morrisane - the greatest salesman in the world.",
                ).also { stage++ }
            2 ->
                options(
                    "If you are, then why are you still selling goods from a stall?",
                    "So what are you selling then?",
                ).also { stage++ }
            3 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.ASKING,
                            "If you are, then why are you still selling goods from a stall?",
                        ).also {
                            stage =
                                10
                        }
                    2 -> {
                        end()
                        if (!hasRequirement(player, "The Feud")) {
                            return true
                        }
                        openNpcShop(player, NPCs.ALI_MORRISANE_1862)
                    }
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well one can only do and sell so much. If I had more staff I'd be able to sell more",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "rather than wasting my time on menial things I could get on with selling sand to the Bedabin and useless tourist trinkets to everyone.",
                ).also {
                    stage++
                }
            12 ->
                options(
                    "I'm far too busy - adventuring is a full time job you know.",
                    "I'd like to help you but....",
                ).also { stage++ }

            13 ->
                when (buttonId) {
                    1 ->
                        playerl(FaceAnim.FRIENDLY, "I'm far too busy - adventuring is a full time job you know.").also {
                            stage =
                                16
                        }
                    2 -> playerl(FaceAnim.FRIENDLY, "I'd like to help you but.....").also { stage++ }
                }

            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes I know, I know - the life of a shop keeper isn't slaying dragon and wooing damsels but it has its charms",
                ).also {
                    stage =
                        15
                }
            15 -> end()
            16 -> npcl(FaceAnim.FRIENDLY, "No problem my friend, perhaps another time.").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "Anyway, have a look at my wares.").also { stage++ }
            18 -> options("No, I'm really too busy.", "Okay.").also { stage++ }
            19 ->
                when (buttonId) {
                    1 -> end()
                    2 -> {
                        end()
                        if (!hasRequirement(player, "The Feud")) {
                            return true
                        }
                        openNpcShop(player, NPCs.ALI_MORRISANE_1862)
                    }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliMorrisaneDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_MORRISANE_1862)
    }
}
