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
class KhazardGuard2Dialogue(
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
            playerl(FaceAnim.FRIENDLY, "Hi.").also { stage = 7 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "I've never seen you around here before!").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "Long live General Khazard!").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Erm.. yes.. soldier, I take it you're new around here?").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "You could say that.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Khazard died two hundred years ago. However his dark spirit remains in the form of the undead maniac General Khazard. Remember he is your master, always watching.",
                ).also { stage++ }

            5 -> npcl(FaceAnim.FRIENDLY, "Got that newbie?").also { stage++ }
            6 ->
                playerl(FaceAnim.FRIENDLY, "Undead, maniac, master. Got it, loud and clear.").also {
                    stage = END_DIALOGUE
                }

            7 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "This area is restricted. Leave now! OUT! And don't come back.",
                ).also { stage++ }

            8 ->
                options(
                    "I apologise, I stumbled in here by mistake.",
                    "You have no right to tell me where I can and cannot go.",
                ).also { stage++ }

            9 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_WORRIED, "I apologise, I stumbled in here by mistake.").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.HALF_WORRIED,
                            "You have no right to tell me where I can and cannot go.",
                        ).also { stage = 11 }
                }

            10 ->
                npcl(FaceAnim.FRIENDLY, "Well, don't just stand there - get out of here!").also {
                    stage = END_DIALOGUE
                }

            11 -> npcl(FaceAnim.FRIENDLY, "Fair enough. Let's do this the hard way.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_GUARD_254)
    }
}
