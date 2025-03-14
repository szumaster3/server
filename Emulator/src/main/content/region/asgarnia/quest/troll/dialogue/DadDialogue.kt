package content.region.asgarnia.quest.troll.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DadDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.TROLL_STRONGHOLD)) {
            in 3..4 -> {
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.OLD_HAPPY,
                            "What tiny human do in troll arena? Dad challenge human to fight!",
                        ).also {
                            stage++
                        }
                    1 ->
                        showTopics(
                            Topic(FaceAnim.THINKING, "Why are you called Dad?", 2),
                            Topic(FaceAnim.FRIENDLY, "I accept your challenge!", 3),
                            Topic(FaceAnim.SCARED, "Eek! No thanks.", END_DIALOGUE),
                        )
                    2 -> npcl(FaceAnim.OLD_HAPPY, "Troll named after first thing try to eat!").also { stage = 1 }
                    3 -> npcl(FaceAnim.OLD_HAPPY, "Tiny human brave. Dad squish!").also { stage++ }
                    4 ->
                        npc!!.attack(player).also {
                            npc!!.skills.lifepoints = npc!!.skills.maximumLifepoints
                            setQuestStage(player!!, Quests.TROLL_STRONGHOLD, 4)
                            stage = END_DIALOGUE
                        }
                }
            }

            in 5..100 -> {
                sendMessage(player, "He doesn't seem interested in talking right now.")
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAD_1125)
    }
}

class DadDialogueFile(
    private val dialogueNum: Int = 0,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (dialogueNum) {
            1 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(FaceAnim.OLD_HAPPY, "No human pass through arena without defeating Dad!").also {
                            stage = END_DIALOGUE
                            setQuestStage(player!!, Quests.TROLL_STRONGHOLD, 3)
                        }
                }

            2 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.OLD_NORMAL, "Tiny human brave. Dad squish!").also { stage++ }
                    1 ->
                        npc!!.attack(player).also {
                            npc!!.skills.lifepoints = npc!!.skills.maximumLifepoints
                            setQuestStage(player!!, Quests.TROLL_STRONGHOLD, 4)
                            stage = END_DIALOGUE
                        }
                }

            3 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.OLD_NORMAL, "Stop! You win. Not hurt Dad.").also { stage++ }
                    1 ->
                        showTopics(
                            Topic(FaceAnim.FRIENDLY, "I'll be going now.", END_DIALOGUE),
                            Topic(FaceAnim.ANGRY_WITH_SMILE, "I'm not done yet! Prepare to die!", 2),
                        )

                    2 ->
                        player!!.attack(npc).also {
                            setQuestStage(player!!, Quests.TROLL_STRONGHOLD, 5)
                            stage = END_DIALOGUE
                        }
                }
        }
    }
}
