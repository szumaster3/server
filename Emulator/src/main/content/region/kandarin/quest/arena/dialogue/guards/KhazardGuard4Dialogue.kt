package content.region.kandarin.quest.arena.dialogue.guards

import core.api.allInEquipment
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KhazardGuard4Dialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.FIGHT_ARENA)) {
            npcl(
                FaceAnim.FRIENDLY,
                "It's you! I don't believe it. You beat the General! You are a traitor to the uniform!",
            ).also { stage = END_DIALOGUE }
        } else if (allInEquipment(player, Items.KHAZARD_HELMET_74, Items.KHAZARD_ARMOUR_75)) {
            playerl(FaceAnim.FRIENDLY, "Hello.")
        } else {
            playerl(FaceAnim.FRIENDLY, "Hi.").also { stage = 3 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.ANGRY,
                    "Despicable thieving scum, that was good armour. Did you see anyone around here soldier?",
                ).also { stage++ }

            1 -> playerl(FaceAnim.SILENT, "Me? No, no one!").also { stage++ }
            2 -> npcl(FaceAnim.SUSPICIOUS, "Hmmmm").also { stage = END_DIALOGUE }
            3 ->
                npcl(FaceAnim.ANNOYED, "I don't know who you are. Get out of my house stranger.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_GUARD_256)
    }
}
