package content.global.random.event.gravedigger

import content.data.GameAttributes
import core.api.*
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.emote.Emotes
import core.game.system.task.Pulse
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Leo dialogue file for Gravedigger random event.
 * @author szu
 */
class LeoDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.LEO_3508)
        when (stage) {
            0 -> if (getAttribute(player!!, GameAttributes.GRAVEDIGGER_SCORE, 0) >= 1) {
                options("There, finished!", "I want to leave.").also { stage = 9 }
            } else if(getAttribute(player!!, "random:talk-to", false)) {
                player("Remind me what I'm supposed to do.").also { stage = 4 }
            } else {
                npcl("You have to help me dig these graves ${player!!.username}, I don't know who else to turn to!").also { stage++ }
            }
            1 -> playerl("What do you mean?").also { stage++ }
            2 -> npcl("I've put the wrong coffins into these graves.").also { stage++ }
            3 -> npcl("I saw you burying bones and it struck me that you seemed to know what you were doing.").also { stage++ }
            4 -> npcl("I need you to pick up the coffins and put them in the right graves.").also { stage++ }
            5 -> npcl("If you need to free up some space in your inventory then just store things in the mausoleum, and I'll take them to the bank for you.").also { stage++ }
            6 -> npcl("I have a reward for you if you get it right.").also { stage++ }
            7 -> playerl("Ok, I'll get right on it.").also { stage++ }
            8 -> {
                end()
                setAttribute(player!!, "random:talk-to", true)
                GravediggerPlugin.init(player!!)
                openInterface(player!!, Components.MESSAGESCROLL_220).also {
                    sendString(player!!, arrayOf("You need to:", "Pick up the coffins.", "Check the body inside.", "Find out where they need to be buried.", "Put all give coffins in the correct graves.", "Then talk to Leo to get a reward.", "You can store items in the mausoleum if you need more", "inventory space.").joinToString("<br>"), Components.MESSAGESCROLL_220, 6)
                }
            }
            9 -> when (buttonID) {
                1 -> sendDialogue(player!!, "Ok, let's have a look.").also { stage = 12 }
                2 -> playerl("I want to leave.").also { stage++ }
            }
            10 -> npcl("In that case, I'll take you back to where I found you.").also { stage = 18 }
            12 -> {
                lock(player!!, 3)
                runTask(player!!, 3) {
                    if(getAttribute(player!!, GameAttributes.GRAVEDIGGER_SCORE, 0) < 5) {
                        Component.setUnclosable(player!!, player!!.dialogueInterpreter.sendDialogues(NPC(NPCs.LEO_3508), FaceAnim.FRIENDLY, "Well, that's a good attempt, but it's just not right."))
                        stage = 15
                    } else {
                        Component.setUnclosable(player!!, player!!.dialogueInterpreter.sendDialogues(NPC(NPCs.LEO_3508), FaceAnim.FRIENDLY, "Wonderful! That's taken care of all of them."))
                        stage++
                    }
                }
            }
            13 -> npcl("Here, I'll take you back to where I found you, and give you your reward.").also { stage++ }
            14 -> {
                end()
                GravediggerPlugin.cleanup(player!!)
                val rewardID = listOf(Items.ZOMBIE_MASK_7594, Items.ZOMBIE_SHIRT_7592, Items.ZOMBIE_TROUSERS_7593, Items.ZOMBIE_GLOVES_7595, Items.ZOMBIE_BOOTS_7596)
                player!!.pulseManager.run(
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            val item = rewardID.filter { hasAnItem(player!!, it).container == null }.randomOrNull()

                            if (item != null) {
                                addItemOrDrop(player!!, item, 1)
                                return true
                            }

                            addItemOrDrop(player!!, Items.COINS_995, 500)
                            if (!hasEmote(player!!, Emotes.ZOMBIE_DANCE)) {
                                unlockEmote(player!!, Emotes.ZOMBIE_DANCE)
                            }
                            if (!hasEmote(player!!, Emotes.ZOMBIE_WALK)) {
                                unlockEmote(player!!, Emotes.ZOMBIE_WALK)
                            }
                            return true
                        }
                    }
                )
            }
            15 -> npcl("Try looking in the coffins to get a better idea of who is in them, and then read the gravestones to find who needs to be in there.",).also { stage++ }
            16 -> playerl("All right, I'll give it another shot.").also { stage++ }
            17 -> npcl("Don't forget to store any items that you don't need in the mausoleum. I'll take them to the bank while you work.").also { stage = END_DIALOGUE }
            18 -> {
                end()
                player!!.pulseManager.run(
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            GravediggerPlugin.cleanup(player!!)
                            return true
                        }
                    }
                )
            }
        }
    }
}
