package content.region.asgarnia.dialogue.entrana

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class HighPriestEntranaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                npcl(FaceAnim.FRIENDLY, "Many greetings. Welcome to our fair island.").also {
                    stage = if (hasSilverPot()) 6 else 1
                }
            }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You are standing on the holy island of Entrana. It was here that Saradomin first stepped upon Gielinor.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "In homage to Saradomin's first arrival, we have built a great church, and devoted the island to those who wish peace for the world.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The inhabitants of this island are mostly monks who spend their time meditating on Saradomin's ways.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Of course, there are now more pilgrims to this holy site, since Saradomin defeated Zamorak in the battle of Lumbridge.",
                ).also {
                    stage++
                }
            5 ->
                npcl(FaceAnim.FRIENDLY, "It is good that so many see Saradomin's true glory!").also {
                    stage =
                        END_DIALOGUE
                }
            6 -> playerl(FaceAnim.FRIENDLY, "Hi, I was wondering, can you quickly bless this for me?").also { stage++ }
            7 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "A somewhat strange request, but I see no harm in it.",
                    "There you go.",
                    "May Saradomin walk with you.",
                ).also {
                    stage++
                }
            8 -> {
                end()
                handleBlessing()
                stage = END_DIALOGUE
            }
        }
        return true
    }

    private fun hasSilverPot(): Boolean {
        val silverPots =
            listOf(
                Items.SILVER_POT_4658,
                Items.SILVER_POT_4660,
                Items.SILVER_POT_4662,
                Items.SILVER_POT_4664,
                Items.SILVER_POT_4666,
            )
        return silverPots.any { inInventory(player, it) }
    }

    private fun handleBlessing() {
        val silverToBlessedMap =
            mapOf(
                Items.SILVER_POT_4658 to Items.BLESSED_POT_4659,
                Items.SILVER_POT_4660 to Items.BLESSED_POT_4661,
                Items.SILVER_POT_4662 to Items.BLESSED_POT_4663,
                Items.SILVER_POT_4664 to Items.BLESSED_POT_4665,
                Items.SILVER_POT_4666 to Items.BLESSED_POT_4667,
            )

        silverToBlessedMap.forEach { (silverPot, blessedPot) ->
            if (inInventory(player, silverPot) && removeItem(player, silverPot)) {
                addItemOrDrop(player, blessedPot)
            }
        }
    }

    override fun newInstance(player: Player?): Dialogue {
        return HighPriestEntranaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HIGH_PRIEST_216)
    }
}
