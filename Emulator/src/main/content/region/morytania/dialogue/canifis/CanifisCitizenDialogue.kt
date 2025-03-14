package content.region.morytania.dialogue.canifis

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CanifisCitizenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val randomWithRing = RandomFunction.random(0, 9)
        val randomWithoutRing = RandomFunction.random(10, 17)
        if (inEquipment(player, Items.RING_OF_CHAROS_4202) || inEquipment(player, Items.RING_OF_CHAROSA_6465)) {
            when (randomWithRing) {
                0 ->
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "I haven't smelt you around here before...",
                    ).also { stage = END_DIALOGUE }
                1 ->
                    npc(FaceAnim.HALF_GUILTY, "If you catch anyone promise me you'll share.").also {
                        stage =
                            END_DIALOGUE
                    }
                2 ->
                    npc(FaceAnim.HALF_GUILTY, "You look to me like someone with a healthy taste for blood.").also {
                        stage =
                            END_DIALOGUE
                    }
                3 ->
                    npc(FaceAnim.HALF_GUILTY, "Fancy going up to the castle for a bit of a snack?").also {
                        stage =
                            END_DIALOGUE
                    }
                4 ->
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "I have no interest in talking to a pathetic",
                        "meat bag like yourself.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                5 -> npc(FaceAnim.HALF_GUILTY, "I bet you have wonderful paws.").also { stage = END_DIALOGUE }
                6 ->
                    npc(FaceAnim.HALF_GUILTY, "Seen any humans around here?", "I'm v-e-r-y hungry.").also {
                        stage =
                            END_DIALOGUE
                    }
                7 -> npc(FaceAnim.HALF_GUILTY, "Good day to you, my friend.").also { stage = END_DIALOGUE }
                8 -> npc(FaceAnim.HALF_GUILTY, "You smell familiar...").also { stage = END_DIALOGUE }
                9 ->
                    npc(FaceAnim.HALF_GUILTY, "A very miserable day, altogether...", "enjoy it while it lasts.").also {
                        stage =
                            END_DIALOGUE
                    }
            }
        } else {
            when (randomWithoutRing) {
                10 ->
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "I have no interest in talking to a pathetic",
                        "meat bag like yourself.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                11 -> npc(FaceAnim.HALF_GUILTY, "Get lost!").also { stage = END_DIALOGUE }
                12 -> npc(FaceAnim.HALF_GUILTY, "Leave me alone.").also { stage = END_DIALOGUE }
                13 -> npc(FaceAnim.HALF_GUILTY, "Have you no manners?").also { stage = END_DIALOGUE }
                14 ->
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "I don't have anything to give you so leave me alone,",
                        "mendicant.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                15 -> npc(FaceAnim.HALF_GUILTY, "I don't have time for this right now.").also { stage = END_DIALOGUE }
                16 ->
                    npc(FaceAnim.HALF_GUILTY, "Don't talk to me again if you value your life!").also {
                        stage =
                            END_DIALOGUE
                    }
                17 -> npc(FaceAnim.HALF_GUILTY, "Hmm... you smell strange...").also { stage++ }
                18 -> player(FaceAnim.HALF_ASKING, "Strange how?").also { stage++ }
                19 -> npc(FaceAnim.HALF_GUILTY, "Like a human!").also { stage++ }
                20 -> player(FaceAnim.STRUGGLE, "Oh! Er... I just ate one is why!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return CanifisCitizenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.BORIS_6026,
            NPCs.IMRE_6027,
            NPCs.YURI_6028,
            NPCs.JOSEPH_6029,
            NPCs.NIKOLAI_6030,
            NPCs.EDUARD_6031,
            NPCs.LEV_6032,
            NPCs.GEORGY_6033,
            NPCs.SVETLANA_6034,
            NPCs.IRINA_6035,
            NPCs.ALEXIS_6036,
            NPCs.MILLA_6037,
            NPCs.GALINA_6038,
            NPCs.SOFIYA_6039,
            NPCs.KSENIA_6040,
            NPCs.YADVIGA_6041,
            NPCs.NIKITA_6042,
            NPCs.VERA_6043,
            NPCs.ZOJA_6044,
            NPCs.LILIYA_6045,
        )
    }
}
