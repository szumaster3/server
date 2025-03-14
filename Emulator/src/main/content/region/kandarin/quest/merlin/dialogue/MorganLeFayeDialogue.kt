package content.region.kandarin.quest.merlin.dialogue

import content.region.kandarin.quest.merlin.handlers.MerlinUtils
import core.api.quest.getQuest
import core.api.removeAttribute
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.world.repository.Repository
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class MorganLeFayeDialogue : DialogueFile() {
    var STAGE_VANISH = 50
    var STAGE_EXCALIBUR = 15
    var STAGE_MAGIC_WORDS = 32
    var STAGE_KILL = 100

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MORGAN_LE_FAYE_248)

        when (stage) {
            0 -> {
                val sirMordred = Repository.findNPC(NPCs.SIR_MORDRED_247)
                sirMordred?.lock()

                npcl(FaceAnim.SCARED, "STOP! Please... spare my son.")
                stage++
            }

            1 ->
                showTopics(
                    Topic(FaceAnim.ANGRY, "Tell me how to untrap Merlin and I might.", 3),
                    Topic(FaceAnim.ANGRY, "No. He deserves to die.", STAGE_KILL),
                    Topic(FaceAnim.NEUTRAL, "Ok then.", STAGE_VANISH),
                )

            2 -> npcl(FaceAnim.NEUTRAL, "You have guessed correctly that I'm responsible for that.").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I suppose I can live with that fool Merlin being loose for the sake of my son.",
                ).also {
                    stage++
                }

            4 -> npcl(FaceAnim.NEUTRAL, "Setting him free won't be easy though.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "You will need to find a magic symbol as close to the crystal as you can find.",
                ).also {
                    stage++
                }

            6 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "You will then need to drop some bats' bones on the magic symbol while holding a lit black candle.",
                ).also {
                    stage++
                }

            7 -> npcl(FaceAnim.NEUTRAL, "This will summon a mighty spirit named Thrantax.").also { stage++ }
            8 -> npcl(FaceAnim.NEUTRAL, "You will need to bind him with magic words.").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Then you will need the sword Excalibur with which the spell was bound in order to shatter the crystal.",
                ).also {
                    stage++
                }

            10 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "So where can I find Excalibur?", STAGE_EXCALIBUR),
                    Topic(FaceAnim.NEUTRAL, "OK I will do all that.", STAGE_VANISH),
                    Topic(FaceAnim.NEUTRAL, "What are the magic words?", STAGE_MAGIC_WORDS),
                )

            STAGE_EXCALIBUR ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The lady of the lake has it. I don't know if she'll give it to you though, she can be rather temperamental.",
                ).also {
                    var quest = getQuest(player!!, Quests.MERLINS_CRYSTAL)

                    if (quest.getStage(player) == 30) {
                        player!!.questRepository.setStage(quest, 40)
                    }
                    stage = 31
                }

            31 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "OK, I will go do all that.", STAGE_VANISH),
                    Topic(FaceAnim.NEUTRAL, "What are the magic words?", STAGE_MAGIC_WORDS),
                )

            STAGE_MAGIC_WORDS ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "You will find the magic words at the base of one of the chaos altars.",
                ).also {
                    stage++
                }

            33 -> npcl(FaceAnim.NEUTRAL, "Which chaos altar I cannot remember.").also { stage++ }
            34 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "So where can I find Excalibur?", STAGE_EXCALIBUR),
                    Topic(FaceAnim.NEUTRAL, "OK I will go do all that.", STAGE_VANISH),
                )

            STAGE_VANISH -> {
                morganDisapear(true)
                stage = END_DIALOGUE
            }

            STAGE_KILL -> {
                val sirMordred = Repository.findNPC(NPCs.SIR_MORDRED_247)

                if (sirMordred != null && sirMordred.isActive) {
                    player!!.sendMessage("You kill Mordred.")
                    DeathTask.startDeath(sirMordred, player!!)
                }

                end()
                stage = END_DIALOGUE
                morganDisapear(false)
            }
        }
    }

    fun morganDisapear(sendMessage: Boolean) {
        val morgan = player!!.getAttribute<NPC>(MerlinUtils.TEMP_ATTR_MORGAN, null)

        if (morgan != null) {
            if (sendMessage) {
                sendDialogue(player!!, "Morgan Le Faye vanishes.")
            }
            removeAttribute(player!!, MerlinUtils.TEMP_ATTR_MORGAN)
        } else {
            end()
        }
    }
}
