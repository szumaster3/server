package content.region.kandarin.quest.arena.dialogue

import content.region.kandarin.quest.arena.cutscene.ScorpionFightCutscene
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import org.rs.consts.Quests

class HengradDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.HENGRAD_263)
        when (getQuestStage(player!!, Quests.FIGHT_ARENA)) {
            in 72..88 ->
                when (stage) {
                    0 -> {
                        lock(player!!, 1000)
                        lockInteractions(player!!, 1000)
                        face(findNPC(NPCs.HENGRAD_263)!!, player!!, 1)
                        npcl(FaceAnim.AFRAID, "Are you ok stranger?").also { stage++ }
                    }
                    1 -> {
                        face(player!!, findNPC(NPCs.HENGRAD_263)!!, 1)
                        playerl(FaceAnim.FRIENDLY, "I'm fine thanks.").also { stage++ }
                    }
                    2 -> npcl(FaceAnim.ASKING, " So Khazard got his hand on you too?").also { stage++ }
                    3 -> playerl(FaceAnim.HALF_WORRIED, " I'm afraid so.").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, " If you're lucky you may last as long as me.").also { stage++ }
                    5 -> playerl(FaceAnim.ASKING, " How long have you been here?").also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I've been in Khazard's prisons ever since I can remember. I was a child when his men kidnapped me. My whole life as been spent killing and fighting, all in the hope that, one day, I might escape.",
                        ).also {
                            stage++
                        }
                    7 -> playerl(FaceAnim.FRIENDLY, "Don't give up.").also { stage++ }
                    8 -> npcl(FaceAnim.FRIENDLY, "Thanks friend.").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.SILENT,
                            "Wait... Shhh, the guard is coming. Looks like you'll be going into the arena. Good luck, friend.",
                        ).also {
                            stage++
                        }

                    10 -> {
                        end()
                        setQuestStage(player!!, Quests.FIGHT_ARENA, 88)
                        ScorpionFightCutscene(player!!).start()
                    }
                }
        }
    }
}
