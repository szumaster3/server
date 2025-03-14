package content.region.fremennik.dialogue.lunar

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SeleneDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val Rellekka = Location.create(2663, 3644, 0)
    private var teled = false

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!teled) {
            if (inInventory(player, Items.SEAL_OF_PASSAGE_9083, 1) ||
                inEquipment(
                    player,
                    Items.SEAL_OF_PASSAGE_9083,
                    1,
                )
            ) {
                player(FaceAnim.FRIENDLY, "Can you tell me a bit about your people?")
            } else {
                player(FaceAnim.FRIENDLY, "Hi, I...").also { stage = 10 }
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val borders = inBorders(player, getRegionBorders(8253)) || inBorders(player, getRegionBorders(8252))
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Ok. Like what?").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "How about the values of the Moon clan!").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Let me see... We value knowledge of self because it is this that gives us our strength! It is most important!",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well... I know things about myself. I know I like hot chocolate!",
                ).also { stage++ }
            4 ->
                npcl(
                    FaceAnim.LAUGH,
                    "I was meaning something a little deeper than that. We also like to see someone listen. You know how they say a wise man listens?",
                ).also {
                    stage++
                }
            5 -> player(FaceAnim.HALF_WORRIED, "....").also { stage++ }
            6 -> npc(FaceAnim.HALF_WORRIED, "Did you hear me?").also { stage++ }
            7 -> player(FaceAnim.HALF_WORRIED, ".... I'm listening.").also { stage++ }
            8 -> npc(FaceAnim.HALF_WORRIED, "Most wise.").also { stage++ }
            9 -> player(FaceAnim.HALF_WORRIED, "Huh?").also { stage = END_DIALOGUE }
            10 -> npc(FaceAnim.ANNOYED, "What are you doing here, Fremennik?!").also { stage++ }
            11 -> player(FaceAnim.WORRIED, "I have a seal of pass...").also { stage++ }
            12 -> npc(FaceAnim.ANNOYED, "No you do not! Begone!").also { stage++ }
            13 -> {
                end()
                if (borders &&
                    !inInventory(player, Items.SEAL_OF_PASSAGE_9083, 1) ||
                    !inEquipment(player, Items.SEAL_OF_PASSAGE_9083, 1)
                ) {
                    playerl(
                        FaceAnim.WORRIED,
                        "Ooops. Suppose I need a seal of passage when I'm walking around that island.",
                    )
                } else {
                    teleport(player, Rellekka, TeleportManager.TeleportType.LUNAR)
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SeleneDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SELENE_4517)
    }
}
