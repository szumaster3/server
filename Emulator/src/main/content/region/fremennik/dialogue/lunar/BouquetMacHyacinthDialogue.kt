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
class BouquetMacHyacinthDialogue(
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
                player(FaceAnim.FRIENDLY, "Hi! What are you up to?")
            } else {
                player(FaceAnim.FRIENDLY, "Hi, I...").also { stage = 5 }
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
            0 -> npc(FaceAnim.FRIENDLY, "Watering the pretty flowers, you want to help?").also { stage++ }
            1 ->
                playerl(
                    FaceAnim.HALF_WORRIED,
                    "I don't have time to water flowers, I have people to save!",
                ).also { stage++ }
            2 -> npcl(FaceAnim.NEUTRAL, "Pft, you should take time to enjoy the simple things.").also { stage++ }
            3 -> player(FaceAnim.NEUTRAL, "I'm not a simple person.").also { stage++ }
            4 -> npc(FaceAnim.LAUGH, "So it seems.").also { stage = END_DIALOGUE }
            5 -> npc(FaceAnim.ANNOYED, "What are you doing here, Fremennik?!").also { stage++ }
            6 -> player(FaceAnim.WORRIED, "I have a seal of pass...").also { stage++ }
            7 -> npc(FaceAnim.ANNOYED, "No you do not! Begone!").also { stage++ }
            8 -> {
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
        return BouquetMacHyacinthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOUQUET_MAC_HYACINTH_4526)
    }
}
