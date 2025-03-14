package content.region.kandarin.quest.arena.dialogue

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LocalDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.FIGHT_ARENA)

        when {
            isQuestComplete(player, Quests.FIGHT_ARENA) -> {
                npcl(FaceAnim.FRIENDLY, "Hey, you're the guy from the arena! How'd you get out?")
                stage = END_DIALOGUE
            }
            questStage in 91..99 -> {
                playerl(FaceAnim.FRIENDLY, "Hello.")
                stage = 9
            }
            questStage >= 10 -> {
                playerl(FaceAnim.FRIENDLY, "Hello.")
            }
            else -> {
                playerl(FaceAnim.FRIENDLY, "Hello.")
                stage = 7
            }
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
                    FaceAnim.FRIENDLY,
                    "I heard the Servil family are fighting soon. Should be very entertaining.",
                ).also {
                    stage++
                }
            1 -> npcl(FaceAnim.ASKING, "Hello stranger, are you new to these parts?").also { stage++ }
            2 -> playerl(FaceAnim.ASKING, "I suppose I am.").also { stage++ }
            3 -> npcl(FaceAnim.ASKING, "What's your business?").also { stage++ }
            4 -> playerl(FaceAnim.ASKING, "Just visiting friends in the cells.").also { stage++ }
            5 -> npcl(FaceAnim.LAUGH, "Visiting, that's funny.").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Only Khazard guards are allowed to see prisoners. Unless you know where to get some Khazard armour, you won't be visiting anyone.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            7 -> npcl(FaceAnim.ASKING, "Hello stranger, are you new to these parts? You look lost.").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.HALF_THINKING,
                    "I suppose you're here for the fight arena? There are some rich folk fighting tomorrow. Should be entertaining.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You're the guy who beat Bouncer! Amazing! That makes you a champion to many, but you'd better get out of here while you still can. General Khazard liked that brute and will be after you.",
                ).also {
                    stage++
                }
            10 -> playerl(FaceAnim.FRIENDLY, "He tried, but I killed him. You are safe now.").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sadly, you can't kill what is already dead. He's probably still out there, somewhere.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LOCAL_268)
    }
}
