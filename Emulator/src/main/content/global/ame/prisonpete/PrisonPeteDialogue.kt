package content.global.ame.prisonpete

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

class PrisonPeteDialogue(
    val dialOpt: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val correctKeyValue = getAttribute(player!!, PrisonPeteUtils.POP_KEY, 0)
        npc = NPC(NPCs.PRISON_PETE_3118)
        if (dialOpt == 1) {
            when (stage) {
                0 ->
                    npc(
                        FaceAnim.HAPPY,
                        "Great, now you've got a key!",
                        "Bring it to me so I can try it on the door.",
                    ).also { stage++ }
                1 -> PrisonPeteUtils.bringKey(player!!)
            }
        } else if (dialOpt == 2) {
            when (stage) {
                0 -> {
                    runTask(player!!, 1) {
                        findLocalNPC(player!!, npc!!.id)?.let {
                            face(it, player!!)
                            animate(it, Animations.TAKE_THING_OUT_OF_POCKET_AND_GIVE_IT_4540)
                            animate(player!!, Animations.TAKE_THING_OUT_OF_POCKET_AND_GIVE_IT_4540)
                            removeItem(player!!, Items.PRISON_KEY_6966)
                        }
                    }
                    stage++
                }
                1 -> npc(FaceAnim.NOD_YES, "Ooh, thanks! I'll see if it's the right one...").also { stage++ }
                2 -> openDialogue(player!!, PrisonPeteDialogue(dialOpt = 3))
            }
        } else if (dialOpt == 3) {
            end()
            if (correctKeyValue == 4) {
                npc(FaceAnim.NEUTRAL, "You've served your sentence, so you can walk out now. Lucky you!").also {
                    openDialogue(player!!, PrisonPeteDialogue(dialOpt = 3))
                }
            } else if (correctKeyValue in 1..3) {
                npc(
                    FaceAnim.HAPPY,
                    "Hooray, you got the right one! Now pull the lever again",
                    "and let's get the next lock unlocked.",
                )
                unlock(player!!)
            } else if (getAttribute(player!!, PrisonPeteUtils.POP_KEY_FALSE, false)) {
                removeAttribute(player!!, PrisonPeteUtils.POP_KEY_FALSE)
                setAttribute(player!!, PrisonPeteUtils.POP_KEY, 0)
                npc(
                    FaceAnim.SAD,
                    "Aww, that was the wrong key! You must have popped",
                    "the wrong sort of animal. Try the big lever again; it'll",
                    "tell you which animal to pop.",
                )
                unlock(player!!)
            }
        } else if (dialOpt == 4) {
            when (stage) {
                0 -> player(FaceAnim.ASKING, "Aren't you coming?").also { stage++ }
                1 ->
                    npc(
                        FaceAnim.SAD,
                        "No, I've got a life sentence. Maybe if you'd been able",
                        "to get the door open for yourself I could have escaped",
                        "with you.",
                    ).also {
                        stage++
                    }
                2 -> player(FaceAnim.NEUTRAL, "I'm sorry.").also { stage++ }
                3 ->
                    npc(
                        FaceAnim.NEUTRAL,
                        "Oh, don't worry. Someone always rescues me",
                        "eventually. Then Evil Bob drags me back again. It's",
                        "been going on for ages. Now you'd better get out",
                        "before you're arrested for loitering.",
                    ).also {
                        stage++
                    }
                4 -> {
                    end()
                    PrisonPeteUtils.cleanup(player!!)
                    queueScript(player!!, 2, QueueStrength.SOFT) {
                        PrisonPeteUtils.reward(player!!)
                        unlock(player!!)
                        return@queueScript stopExecuting(player!!)
                    }
                }
            }
        } else if (dialOpt == 0) {
            when (stage) {
                0 -> {
                    setTitle(player!!, 2)
                    sendDialogueOptions(
                        player!!,
                        "What would you like to say?",
                        "What is this place?",
                        "How do I get out of here?",
                    ).also {
                        stage++
                    }
                }
                1 ->
                    when (buttonID) {
                        1 -> player(FaceAnim.DISGUSTED_HEAD_SHAKE, "What is this place?").also { stage++ }
                        2 ->
                            player(
                                FaceAnim.NEUTRAL,
                                "How do I get out of here?",
                                "I can't be held captive by a cat!",
                            ).also {
                                stage =
                                    6
                            }
                    }
                2 ->
                    npc(
                        FaceAnim.SAD,
                        "Don't You remember? This is ScapeRune's prison.",
                        "Evil Bob caught you and brought you here.",
                    ).also {
                        stage++
                    }
                3 ->
                    player(
                        FaceAnim.ANNOYED,
                        "What gives him the right to lock me up?",
                        "I demand to see a lawyer! I know my rights!",
                    ).also {
                        stage++
                    }
                4 ->
                    npc(
                        FaceAnim.SAD,
                        "Evil Bob doesn't care about people's rights.",
                        "He's cruel and utterly merciless. He's a cat.",
                    ).also {
                        stage++
                    }
                5 ->
                    player(
                        FaceAnim.NEUTRAL,
                        "How do I get out of here?",
                        "I can't be held captive by a cat!",
                    ).also { stage++ }
                6 ->
                    npc(
                        FaceAnim.SAD,
                        "Some of these balloon animals have keys in them, and if",
                        "you pull the big lever it tells you which shape animal",
                        "contains the correct key, but I can never find it.",
                    ).also {
                        stage++
                    }
                7 ->
                    npc(
                        FaceAnim.SAD,
                        "You need to pull the lever to find out which shape",
                        "animal contains the key, then pop that sort of animal to",
                        "get the key.",
                    ).also {
                        stage++
                    }
                8 -> npc(FaceAnim.SAD, "Bring me any keys you get and", "I'll try them on the door.").also { stage++ }
                9 -> player(FaceAnim.ASKING, "What happens if I get it wrong?").also { stage++ }
                10 ->
                    npc(
                        FaceAnim.SAD,
                        "You haven't got a life sentence, so they'll let you out in",
                        "3 minutes, but you should be able to escape much",
                        "faster if you go pull that lever and pop the right balloon",
                        "animals.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }
    }
}
