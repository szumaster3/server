package content.global.ame.evilbob

import content.data.GameAttributes
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class ServantDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SERVANT_2481)
        when (stage) {
            0 -> {
                if (getAttribute(player!!, GameAttributes.RE_BOB_COMPLETE, false)) {
                    playerl(FaceAnim.NEUTRAL, "Evil Bob has fallen asleep, come quickly!").also { stage = 2 }
                } else if (!getAttribute(player!!, GameAttributes.RE_BOB_DIAL, false)) {
                    playerl(FaceAnim.ANGRY, "I need help, I've been kidnapped by an evil cat!").also { stage = 200 }
                } else if (getAttribute(player!!, GameAttributes.RE_BOB_DIAL_INDEX, false)) {
                    npcl(
                        FaceAnim.SAD,
                        "Look... over t-t-there! That fishing spot c-c-contains the f-f-f-fish he likes.",
                    ).also {
                        stage =
                            1
                    }
                } else {
                    npcl(
                        FaceAnim.SAD,
                        "F-f-f-fish... give him the f-f-f-fish he likes and he might f- f-f-fall asleep.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }
            }

            1 -> {
                end()
                setAttribute(player!!, GameAttributes.RE_BOB_DIAL_INDEX, false)
                when (getAttribute(player!!, GameAttributes.RE_BOB_ZONE, EvilBobUtils.northFishingZone.toString())) {
                    EvilBobUtils.northFishingZone.toString() -> ServantCutsceneN(player!!).start()
                    EvilBobUtils.southFishingZone.toString() -> ServantCutsceneS(player!!).start()
                    EvilBobUtils.eastFishingZone.toString() -> ServantCutsceneE(player!!).start()
                    EvilBobUtils.westFishingZone.toString() -> ServantCutsceneW(player!!).start()
                }
            }

            2 -> npcl(FaceAnim.SAD, "Come? Come where?").also { stage++ }
            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Away from this place! To " + GameWorld.settings!!.name + " proper!",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.SAD,
                    "You go, ${player!!.username}, I don't belong there... I belong here, in Scape2009. This is the only place I can ever go...",
                ).also {
                    stage++
                }

            5 -> options("But I love you!", "Oh alright then.").also { stage++ }
            6 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.SAD, "But I love you!").also { stage = 100 }
                    2 ->
                        playerl(FaceAnim.NEUTRAL, "Oh, alright then, I'll be off! See you around!").also {
                            stage = END_DIALOGUE
                        }
                }

            100 -> npcl(FaceAnim.NEUTRAL, "Our love can never be, sweet ${player!!.username}.").also { stage++ }
            101 -> npcl(FaceAnim.NEUTRAL, "Go now! Go, and don't look back!").also { stage = END_DIALOGUE }
            200 -> npcl(FaceAnim.SAD, "Meow! Errr... I c-c-c-can't help you... He'll kill us all!").also { stage++ }
            201 ->
                playerl(
                    FaceAnim.ANGRY,
                    "Now you listen to me! He's just a little cat! There must be something I can do!",
                ).also {
                    stage++
                }

            202 ->
                npcl(
                    FaceAnim.SAD,
                    "F-f-f-fish... give him the f-f-f-fish he likes and he might f- f-f-fall asleep.",
                ).also {
                    stage++
                }

            203 -> {
                npcl(
                    FaceAnim.SAD,
                    "Look... over t-t-there! That fishing spot c-c-contains the f-f-f-fish he likes.",
                ).also {
                    stage =
                        1
                }
                setAttribute(player!!, GameAttributes.RE_BOB_DIAL, true)
            }
        }
    }
}
