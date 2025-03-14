package content.region.asgarnia.dialogue.falador

import core.api.openInterface
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HairdresserDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size == 2) {
            if (player.equipment[EquipmentContainer.SLOT_HAT] == null &&
                player.equipment[EquipmentContainer.SLOT_WEAPON] == null &&
                player.equipment[EquipmentContainer.SLOT_SHIELD] == null
            ) {
                end()
                openInterface(player, if (player.isMale) 596 else 592)
            } else if (player.equipment[EquipmentContainer.SLOT_HAT] != null ||
                player.equipment[EquipmentContainer.SLOT_WEAPON] != null ||
                player.equipment[EquipmentContainer.SLOT_SHIELD] != null
            ) {
                npc(
                    FaceAnim.SCARED,
                    (if (player.isMale) "Sir, " else "Madam, ") + "I can't cut your hair with those things pointing",
                    "at me. Please take them off and speak to me again.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            } else {
                player(FaceAnim.SAD, "I don't have 2000 gold coins on me...").also { stage = 8 }
            }
            return true
        }
        npc(
            FaceAnim.NEUTRAL,
            "Good afternoon ${if (player.isMale) "sir" else "madam"}. In need of a haircut are we?",
            if (player.isMale) "Perhaps a shave too?" else "",
        ).also {
            stage =
                1
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                if (player.isMale) {
                    options("I'd like a haircut please.", "I'd like a shave please.", "No thank you.").also {
                        stage = 2
                    }
                } else {
                    options("I'd like a haircut please.", "No thank you.").also { stage = 5 }
                }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "I'd like a haircut please.").also { stage = 6 }
                    2 -> player(FaceAnim.HAPPY, "I'd like a shave please.").also { stage = 6 }
                    3 -> player(FaceAnim.NEUTRAL, "No thank you.").also { stage = 10 }
                }
            5 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "I'd like a haircut please.").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "No thank you.").also { stage = 20 }
                }
            6 ->
                npc(
                    FaceAnim.HAPPY,
                    "Certainly ${if (player.isMale) "sir" else "madam"}. I cut ${if (player.isMale) "men's" else "women's"} hair at the bargain rate of",
                    "only 2000 gold coins. I'll even throw in a free recolour!",
                ).also {
                    stage++
                }
            7 ->
                if (player.equipment[EquipmentContainer.SLOT_HAT] == null &&
                    player.equipment[EquipmentContainer.SLOT_WEAPON] == null &&
                    player.equipment[EquipmentContainer.SLOT_SHIELD] == null
                ) {
                    npc(
                        FaceAnim.NEUTRAL,
                        "Please select the hairstyle and colour you would like",
                        "from this brochure.",
                    ).also {
                        stage =
                            9
                    }
                } else if (player.equipment[EquipmentContainer.SLOT_HAT] != null ||
                    player.equipment[EquipmentContainer.SLOT_WEAPON] != null ||
                    player.equipment[EquipmentContainer.SLOT_SHIELD] != null
                ) {
                    npc(
                        FaceAnim.SCARED,
                        (if (player.isMale) "Sir, " else "Madam, ") + "I can't cut your hair with those things pointing",
                        "at me. Please take them off and speak to me again.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    player(FaceAnim.SAD, "I don't have 2000 gold coins on me...").also { stage++ }
                }
            8 ->
                npc(FaceAnim.ANNOYED, "Well, come back when you do. I'm not running a", "charity here!").also {
                    stage =
                        END_DIALOGUE
                }
            9 -> {
                end()
                openInterface(player, if (player.isMale) 596 else 592)
            }
            10 -> npc(FaceAnim.NEUTRAL, "Very well. Come back if you change your mind.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HAIRDRESSER_598)
    }
}
