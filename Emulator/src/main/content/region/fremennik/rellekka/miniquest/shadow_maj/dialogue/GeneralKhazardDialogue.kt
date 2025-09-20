package content.region.fremennik.rellekka.miniquest.shadow_maj.dialogue

import content.region.fremennik.rellekka.miniquest.shadow_maj.plugin.GeneralShadow
import core.api.*
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.Quests

/**
 * Represents the General Khazard dialogue.
 *
 * # Relations
 * - [GeneralShadow]
 */
@Initializable
class GeneralKhazardDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GeneralKhazardDialogueFile(), npc)
        return false
    }

    override fun newInstance(player: Player?): Dialogue = GeneralKhazardDialogue(player)
    override fun getIds(): IntArray = intArrayOf(5573)
}

class GeneralKhazardDialogueFile: DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        val progress = GeneralShadow.getShadowProgress(player!!)
        val hasWeapon = getItemFromEquipment(player!!, EquipmentSlot.WEAPON)
        val hasShield = getItemFromEquipment(player!!, EquipmentSlot.SHIELD)
        val hasSinSeersNote = inInventory(player!!, Items.SIN_SEERS_NOTE_10856)
        val hasLeg = hasAnItem(player!!, Items.SEVERED_LEG_10857).container != null
        when (stage) {
            0 -> {
                if (GeneralShadow.isComplete(player!!)) {
                    player(FaceAnim.ANNOYED,"Your dog attacked me. AGAIN!")
                    stage = 100
                }

                // Check requirements and start the mini-quest.
                if (hasWeapon != null || hasShield != null || !GeneralShadow.hasGhostlySet(player!!) || !isQuestComplete(player!!, Quests.FIGHT_ARENA)) {
                    npc(FaceAnim.OLD_DISTRESSED2, "You can see into the Shadow Realm and yet you are", "not of it. Oh well, you will be of no use.")
                    stage = END_DIALOGUE
                }

                // Handle dialogue after found all the scouts & lost leg.
                if (GeneralShadow.isComplete(player!!) && !hasLeg && getAttribute(player!!, GeneralShadow.GS_RECEIVED_SEVERED_LEG, false)) {
                    options("Sorry, sir. Where was my reward again?", "I lost the key to my reward.", "Never mind.")
                    stage = 200
                }

                // Handle dialogue after received sin seers note.
                if (hasSinSeersNote) {
                    playerl(FaceAnim.FRIENDLY, "I have been to the Sin Seer. She has made her verdict.")
                    stage = 24
                }

                // Handle dialogue while progressing.
                when {
                    GeneralShadow.getShadowProgress(player!!) == 0 -> {
                        player("Err, what was I supposed to do again?")
                        stage = 22
                    }

                    GeneralShadow.getShadowProgress(player!!) in 2..4 -> {
                        npcl(FaceAnim.OLD_NORMAL, "Do you make progress in finding my scouts?")
                        stage = 35
                    }

                    GeneralShadow.getShadowProgress(player!!) == 4 && !getAttribute(player!!, GeneralShadow.GS_RECEIVED_SEVERED_LEG, false) -> {
                        player(FaceAnim.HAPPY, "I did it, sir! I found all the scouts and delivered", "your message. The last one said I should report back to", "you.").also { stage = 42 }
                    }

                    else -> {
                        player(FaceAnim.FRIENDLY,"Hello, General.")
                        stage++
                    }
                }
            }
            1 -> npc(FaceAnim.OLD_NORMAL, "What? You are not dead and yet you walk and talk in", "the Shadow Realm. Explain yourself.").also { stage++ }
            2 -> player("I am a being of great power and experience. I can see", "and speak to ghosts. For my assistance they clothed me", "in their garb. Can I offer you assistance, General?").also { stage++ }
            3 -> npc(FaceAnim.OLD_NORMAL, "This intrigues me. How do you know me? You look", "familiar but I can't place it.").also { stage++ }
            4 -> options("Uh, well, you don't really have eyes, so how can you tell?", "I just have one of those faces.").also { stage++ }
            5 -> when (buttonID) {
                1 -> player("Uh, well, you don't really have eyes, so how can you", "tell?").also { stage = 8 }
                2 -> player("I just have one of those faces.").also { stage++ }
            }
            6 -> npc(FaceAnim.OLD_NORMAL, "True, I never can tell you humans apart. You're all", "ugly creatures, anyway.").also { stage++ }
            7 -> player("Hey!").also { stage = END_DIALOGUE }
            8 -> npc(FaceAnim.OLD_NORMAL, "There are ways to see that you humans cannot grasp.").also { stage++ }
            9 -> player("Oh.").also { stage++ }
            10 -> npc(FaceAnim.OLD_NORMAL, "It doesn't matter anyway. You are not of the blood", "and I cannot trust you.").also { stage++ }
            11 -> options("I'm a very trustworthy person.", "You're right. I'm completely untrustworthy.").also { stage++ }
            12 -> when (buttonID) {
                1 -> player("I'm a very trustworthy person.").also { stage++ }
                2 -> player("You're right. I'm completely untrustworthy.").also { stage = END_DIALOGUE }
            }
            13 -> npcl(FaceAnim.OLD_NORMAL, "That is not something I can take your word on.").also { stage++ }
            14 -> player("So who's word would you take?").also { stage++ }
            15 -> npc(FaceAnim.OLD_CALM_TALK1, " Hmm... There is one person's word I would believe. If", "you visited the Sin Seer, and she judged you", "trustworthy, then I would have use for you.").also { stage++ }
            16 -> player("Where would I find her?").also { stage++ }
            17 -> npc(FaceAnim.OLD_NORMAL, "Well, considering I told you she was a seer, of sorts, I", "would imagine you would find her in Seer's Village. It", "is to the south of here not far.").also { stage++ }
            18 -> npc(FaceAnim.OLD_NORMAL, "Go and see her, return with her report and I will see if", "I have use for you. You will be well rewarded.").also { stage++ }
            19 -> player("Okay! that sounds reasonable. Sin Seer, huh? How do", "you know someone like her?").also { stage++ }
            20 -> npcl(FaceAnim.OLD_NORMAL, "It's a long story, and I do not have the time to tell it.").also { stage++ }
            21 -> {
                end()
                GeneralShadow.setShadowProgress(player!!, 0)
            }
            22 -> npc(FaceAnim.OLD_NORMAL, "You goldfish! Go find the Sin Seer - she will determine if", "you are trustworthy. She lives in Seer's village to the", "south of here.").also { stage++ }
            23 -> end()
            24 -> npc(FaceAnim.OLD_NORMAL, "And?").also { stage++ }
            25 -> playerl(FaceAnim.FRIENDLY, "She said to give you this note to prove I went to see her.").also { stage++ }
            26 -> npcl(FaceAnim.OLD_NORMAL, "The note says you have committed many sins and atrocities in your life. I'm impressed; I will use you.").also { stage++ }
            27 -> npcl(FaceAnim.OLD_NORMAL, "It has been a long time since I have left my home in the south. I have lost track of what is going on in the world, but now it is imperative that I know.").also { stage++ }
            28 -> npcl(FaceAnim.OLD_NORMAL, "So I sent out four scouts to gather information for me. I brought them to walk like me in the Shadow Realm, so they would be safe from my enemies.").also { stage++ }
            29 -> npcl(FaceAnim.OLD_NORMAL, "I sent them on four missions. One was to go to the land of the gnomes and explore their holdings. One was to go to a jungle to the sound. One was to explore where the White Knights rule.").also { stage++ }
            30 -> npcl(FaceAnim.OLD_NORMAL, "And one was to the place that I once called home - a place of great heat.").also { stage++ }
            31 -> npcl(FaceAnim.OLD_NORMAL, "I am going to entrust you with a very important message, which you must deliver to each one of them.").also { stage++ }
            32 -> npcl(FaceAnim.OLD_NORMAL, "Tell them: The planets are nearing alignment; we will meet in the place of half light and ice soon. Beware the others, for though they are weak and few, they are cunning.").also { stage++ }
            33 -> player("You can trust me to do it, sir!").also { stage++ }
            34 -> {
                end()
                removeItem(player!!, Items.SIN_SEERS_NOTE_10856)
                setAttribute(player!!, GeneralShadow.GS_TRUSTWORTHY, true)
            }
            35 -> if (progress == 0) {
                playerl(FaceAnim.HALF_ASKING, "I haven't found any. Where were they headed?").also { stage++ }
            } else {
                player("I've found $progress, but they're kind of hard to find.", "Where were they headed again?").also { stage++ }
            }
            36 -> npc(FaceAnim.OLD_NORMAL, "One was to go to a jungle to the south.").also { stage++ }
            37 -> npc(FaceAnim.OLD_NORMAL, "One was to go to the land of the gnomes and explore", "their holdings.").also { stage++ }
            38 -> npc(FaceAnim.OLD_NORMAL, "One was to explore where the White Knights rule.").also { stage++ }
            39 -> npc(FaceAnim.OLD_NORMAL, "One was to go to a place of great heat.").also { stage++ }
            40 -> npc(FaceAnim.OLD_NORMAL, "If I knew more of their locations I would go myself.", "Now go and finish the job.").also { stage++ }
            41 -> end()
            42 -> npc(FaceAnim.OLD_NORMAL, "Oh, and what did he say that for?").also { stage++ }
            43 -> player("He said to tell you, 'We of the blood are still loyal.", "Pavlov is the messenger.'").also { stage++ }
            44 -> npc(FaceAnim.OLD_NORMAL, "Ah. Interesting. Well, you have served your purpose.", "Now to award you with something appropriate.").also { stage++ }
            45 -> npc(FaceAnim.OLD_NORMAL, "There is a cave near where the fish-men gather. Go to", "the east branch of this cave and seek out the one who lives", "in the Shadow Realm. He will reward you well.").also { stage++ }
            46 -> player("Right! Fish-men. Cave. Ghost. Thanks, General!").also { stage++ }
            47 -> npc(FaceAnim.OLD_NORMAL, "Wait! Take this, it is the key to your reward.").also { stage++ }
            48 -> sendItemDialogue(player!!, Items.SEVERED_LEG_10857, "The General hands you a severed leg.").also { stage++ }
            49 -> player(FaceAnim.SCARED, "Ugh, well, at least it's not another hand.").also { stage++ }
            50 -> npcl(FaceAnim.OLD_NORMAL, "Ah, you have dealt with severed limbs before?").also { stage++ }
            51 -> player("It's a long story.").also { stage++ }
            52 -> npc(FaceAnim.OLD_NORMAL, "Don't worry if you lose it, I can always get more to", "replace it.").also { stage++ }
            53 -> {
                end()
                addItemOrDrop(player!!, Items.SEVERED_LEG_10857)
                setAttribute(player!!, GeneralShadow.GS_RECEIVED_SEVERED_LEG, true)
            }
            100 -> npc(FaceAnim.OLD_NORMAL, "He was angry that you killed him.", "Quite natural, really.").also { stage++ }
            101 -> npc(FaceAnim.OLD_NORMAL, "Now get out of here, you filthy skinbag.", "I will not waste my time on you.").also { stage = END_DIALOGUE }
            200 -> when (buttonID) {
                1 -> player("Sorry, sir. Where was my reward again?").also { stage++ }
                2 -> player("I lost the key to my reward.").also { stage = 202 }
                3 -> player("Never mind.").also { stage = END_DIALOGUE }
            }
            201 -> npc(FaceAnim.OLD_NORMAL, "There is a cave near where the fish-men gather. Go to", "the east branch of this cave and seek out the one who lives", "in the Shadow Realm. He will reward you well.").also { stage = END_DIALOGUE }
            202 -> npc(FaceAnim.OLD_NORMAL, "Incompetent fool!").also { stage++ }
            203 -> if (!hasLeg) {
                npcl(FaceAnim.OLD_NORMAL, "Here, take another. They are easy to come by.").also { stage++ }
            } else {
                npcl(FaceAnim.OLD_NORMAL, "You already have one. What use could you have for two?").also { stage++ }
            }
            204 -> {
                end()
                sendItemDialogue(player!!, Items.SEVERED_LEG_10857, "The General hands you a severed leg.")
                addItemOrDrop(player!!, Items.SEVERED_LEG_10857)
            }
        }
    }
}
