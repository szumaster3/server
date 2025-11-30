package content.region.kandarin.yanille.quest.handsand

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Sounds

class SandpitCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(10032)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }
            1 -> {
                teleport(player, 43, 35)
                addNPC(NPCs.BERT_3108, 50, 27, Direction.SOUTH,0)
                moveCamera(43,34, 500)
                rotateCamera(46,31, 300)
                fadeFromBlack()
                timedUpdate(3)
            }
            2 -> dialogueUpdate(true,"The Wizard chants and your attention to the sandpit where Bert is found.")
            3 -> {
                move(getNPC(NPCs.BERT_3108)!!, 47, 28)
                timedUpdate(5)
            }
            4 -> {
                move(getNPC(NPCs.BERT_3108)!!, 46, 29)
                getNPC(NPCs.BERT_3108)!!.face(player)
                timedUpdate(1)
            }
            5 -> {
                sendChat(getNPC(NPCs.BERT_3108)!!, "My sand! My lovely sand!")
                playAudio(player, Sounds.HANDSAND_SANDSWIRL_1591)
                animateScenery(getObject(46,31)!!, 3038)
                getNPC(NPCs.BERT_3108)!!.animate(Animation(Animations.CRY_860))
                sendDialogue(player, "Something very strange happens to the Sandpit, it looks like it has filled itself up!")
                timedUpdate(12)
            }
            6 -> dialogueUpdate(true,"Something very strange happens to the Sandpit, it looks like it has filled itself up!")
            7 -> end{
                openDialogue(player, SandpitCutsceneEndDialogue())
            }
        }
    }
}

private class SandpitCutsceneEndDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 ->npcl(FaceAnim.HAPPY, "There, the sand pit will now magically refill. No more work for Bert!").also { stage++ }
            1 ->npcl(FaceAnim.FRIENDLY, "We must find the rest of Clarence, I've sent some wizards out to some of the sandpits, would you please check the Entrana sandpit?").also {
                stage = END_DIALOGUE
            }
        }
    }
}