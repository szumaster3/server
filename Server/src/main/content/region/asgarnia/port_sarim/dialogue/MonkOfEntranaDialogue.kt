package content.region.asgarnia.port_sarim.dialogue

import content.global.travel.charter.CharterShip
import core.api.sendDialogue
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Monk Of Entrana dialogue.
 */
class MonkOfEntranaDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        if (player == null || npc == null) return

        val monkLeaveIds = intArrayOf(NPCs.MONK_OF_ENTRANA_2730, NPCs.MONK_OF_ENTRANA_658, NPCs.MONK_OF_ENTRANA_2731)

        when (stage) {
            0 -> if (npc!!.id in monkLeaveIds) {
                npc(FaceAnim.HALF_ASKING, "Do you wish to leave holy Entrana?").also { stage = 25 }
            } else {
                npc(FaceAnim.HALF_ASKING, "Do you seek passage to holy Entrana? If so, you must", "leave your weaponry and armour behind. This is", "Saradomin's will.").also { stage = 1 }
            }
            1 -> showTopics(
                Topic(FaceAnim.NEUTRAL, "No, not right now.", 10),
                Topic(FaceAnim.HAPPY, "Yes, okay, I'm ready to go.", 20)
            )
            10 -> npc(FaceAnim.HAPPY, "Very well.").also { stage = END_DIALOGUE }
            20 -> npc(FaceAnim.FRIENDLY, "Very well. One moment please.").also { stage++ }
            21 -> sendDialogue(player!!, "The monk quickly searches you.").also { stage++ }
            22 -> if (!ItemDefinition.canEnterEntrana(player)) {
                npc(FaceAnim.ANGRY, "NO WEAPONS OR ARMOUR are permitted on holy", "Entrana AT ALL. We will not allow you to travel there", "in breach of mighty Saradomin's edict.").also { stage++ }
            } else {
                npc(FaceAnim.FRIENDLY, "All is satisfactory. You may board the boat now.").also { stage = 24 }
            }
            23 -> npc(FaceAnim.ANGRY, "Do not try and decieve us again. Come back when you", "have liad down your Zamorakian instruments of death.").also { stage = END_DIALOGUE }
            24 -> {
                end()
                CharterShip.PORT_SARIM_TO_ENTRANA.sail(player!!)
            }
            25 -> showTopics(
                Topic(FaceAnim.HAPPY, "Yes, I'm ready to go.", 26),
                Topic(FaceAnim.NEUTRAL, "Not just yet.", END_DIALOGUE)
            )
            26 -> npc(FaceAnim.HAPPY, "Okay, let's board...").also { stage++ }
            27 -> {
                end()
                CharterShip.ENTRANA_TO_PORT_SARIM.sail(player!!)
            }
        }
    }
}