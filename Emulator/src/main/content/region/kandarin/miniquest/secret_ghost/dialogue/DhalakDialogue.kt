package content.region.kandarin.miniquest.secret_ghost.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items

/**
 * Represents the [DhalakDialogue] & [DhalakDialogueFile].
 */
@Initializable
class DhalakDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DhalakDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): Dialogue = DhalakDialogue(player)
    override fun getIds(): IntArray = intArrayOf(2385, 2386, 2387)
}

class DhalakDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                val sequence = getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)
                val hasItem = hasAnItem(player!!, Items.GHOSTLY_HOOD_6109).container != null
                when {
                    // Talk without ghostspeak amulet.
                    !inEquipment(player!!, Items.GHOSTSPEAK_AMULET_552) -> {
                        openDialogue(player!!, RandomDialogue(), npc!!)
                    }
                    // Talk again if lost ghostly item.
                    !hasItem && getAttribute(player!!, GameAttributes.ZAROS_DHALAK_TALK, false) -> {
                        player(FaceAnim.SAD, "Could I have that hat again? I seem to have", "misplaced it somewhere...").also { stage = 39 }
                    }
                    // Talk again.
                    getAttribute(player!!, GameAttributes.ZAROS_DHALAK_TALK, false) -> {
                        player(FaceAnim.ASKING, "Where would you suggest I look for Viggora?").also { stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)) }
                    }
                    // First talk.
                    else -> {
                        // Correct path.
                        if(2384 + sequence == npc!!.id) {
                            player("Hello Dhalak.").also { stage++ }
                        } else {
                            openDialogue(player!!, WrongLocationDialogue(), npc!!)
                        }
                    }
                }
            }
            1 -> npc(FaceAnim.THINKING,"You see my form, hear my words, and know my name,", "yet your face I recognise not...").also { stage++ }
            2 -> npc(FaceAnim.HALF_THINKING, "Be you some mighty sorcerer to bind me so?").also { stage++ }
            3 -> if (getStatLevel(player!!, Skills.MAGIC) < 99) {
                player(FaceAnim.HALF_GUILTY, "Um...", "Well, not really...").also { stage++ }
            } else {
                player(FaceAnim.HALF_GUILTY, "Well, I don't mean to brag,", "but I guess I am with my level 99 magic...").also { stage++ }
            }
            4 -> player(FaceAnim.NEUTRAL, "But that is besides the point. It is not I who has trapped", "you here as a ghost.").also { stage++ }
            5 -> npc(FaceAnim.HALF_THINKING, "Then how comes it to be that you know my name, stranger?").also { stage++ }
            6 -> player("Lennissa told me about you, and where to find you.").also { stage++ }
            7 -> npc(FaceAnim.SAD, "Lennissa? Oh that poor sweet girl... Has my foolishness", "cursed her as well as myself???").also { stage++ }
            8 -> player(FaceAnim.HALF_THINKING, "Your foolishness?").also { stage++ }
            9 -> npc(FaceAnim.HALF_GUILTY,"The story shames me, stranger. I wouldst rather keep it", "unto myself.").also { stage++ }
            10 -> options("Tell em your story", "Goodbye then").also { stage++ }
            11 -> when (buttonID) {
                1 -> player("Look, I don't want to force you into telling me, but", "perhaps sharing it with someone might relieve your", "guilt?").also { stage++ }
                2 -> player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also { stage = END_DIALOGUE }
            }
            12 -> npc(FaceAnim.HALF_GUILTY, "Aye...", "Perhaps it might at that.").also { stage++ }
            13 -> npc(FaceAnim.THINKING, "So what has Lennissa told", "you of the events of the day the curse befell me?").also { stage++ }
            14 -> player("Well, she told me that she was working as an undercover agent", "of Saradomin amongst the followers of some 'Empty Lord', and", "when news of the theft of a god-weapon reached her, she passed", "the message on to you instead of taking it to Saradomin because", "she was scared her cover might be blown.").also { stage++ }
            15 -> npc("Aye, that is a fair account of events...").also { stage++ }
            16 -> player("But I don't understand why you didn't take her message to", "Saradomin?").also { stage++ }
            17 -> npc("Stranger, my foolishness was a result of my respect for", "Saradomin, not as a result of any attempted treachery!").also { stage++ }
            18 -> player("So why didn't you pass on Lennissa's message? As I understand", "it, something happened with the staff that could have been", "avoided if you had passed her message on!").also { stage++ }
            19 -> npc("*sigh* I know not what occurred with that god-weapon, but I", "have my suspicions... Let me explain myself. I was Lennissa's", "immediate superior, and I was often her contact for missions.").also { stage++ }
            20 -> npc("Because of this role, I had access to a larger picture of", "what was happening than she herself did, and I was not only", "well aware that her presence amongst the enemy camp was", "detected, but I also was aware that there was a growing", "faction amongst them who were plotting against their master.").also { stage++ }
            21 -> player("Their master being...?").also { stage++ }
            22 -> npc("That I will not tell you. I will tempt the fates no more", "than I already have done. But anyway, it had become clear to", "me that the Mahjarrat who had been liberated from the control", "of Icthlarin did not much appreciate one form of slavery to", "another.").also { stage++ }
            23 -> npc("Under the leadership of the mighty Zamorak, they were making", "plans to overthrow their master and take his power for", "themselves.").also { stage++ }
            24 -> npc("As powerful, long-lived and evil as they were, they were still", "just mortal, and I made the decision that it would be of benefit", "to my Lord Saradomin for his mightiest rival to be distracted", "by such internal conflicts.").also { stage++ }
            25 -> player("So that is why you decided not to pass the report from Lennissa on?").also { stage++ }
            26 -> npc("Yes, but my guilt is more than simply inaction... I knew that", "with such a weapon, Zamorak would be capable of launching an", "attack that could actually stand a chance of success.").also { stage++ }
            27 -> npc("But I also knew that he would never be able to get a chance", "to use it in battle, for being a god-weapon its presence", "would have sung out to their leader.").also { stage++ }
            28 -> player("I'm guessing you did something about that, then?").also { stage++ }
            29 -> npc("Indeed I did. To my eternal shame, I decided that I would", "assist Zamorak and his henchmen in their battle by secretly", "casting a spell of concealment upon the staff so that they", "might use it secretly against their master.").also { stage++ }
            30 -> player("So Zamorak knew about this?").also { stage++ }
            31 -> npc("No, nobody except myself, and now you, knew that I cast such", "a spell... Had I known what a threat to my Lord Saradomin he", "would later become, I wouldst have taken the message to", "Saradomin immediately! Alas, it is all too easy to see your", "mistakes after having made them...").also { stage++ }
            32 -> player("I'm confused. What exactly happened with the staff anyway?", "And why have all those various random people been cursed", "because of it?").also { stage++ }
            33 -> npc("I cannot answer your question with anything other than my", "own suppositions, but I do know of one who might be able to,", "and if any man deserved to be cursed for their actions that", "day, it was he!").also { stage++ }
            34 -> player("Who are you speaking of?").also { stage++ }
            35 -> npc("His name is Viggora. He was an evil man, brutal and vicious,", "and deadly with a blade. He was one of the few humans", "Zamorak allowed to rise to a position of power amongst his", "rebels, possibly because he imitated those same qualities of", "Zamorak.").also { stage++ }
            36 -> npc("If anyone knows what Zamorak did with that god-weapon to have", "caused this curse to have befallen us, it would have been he,", "for he would have been fighting on Zamorak's very right-hand", "side in their rebellion.").also { stage++ }
            37 -> npc("Please, if this curse can be lifted, find Viggora and find out", "what he wrought upon us! I have no wealth nor magic to aid you,", "but take my hood as reward; It has served me well these", "centuries past, and may bring you luck.").also {
                setAttribute(player!!, GameAttributes.ZAROS_DHALAK_TALK, true)
                addItemOrDrop(player!!, Items.GHOSTLY_HOOD_6109, 1)
                stage++
            }
            38 -> player("Where would you suggest I look for Viggora?").also { stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)) }
            39 -> player("Okay, well I'll try and find him for you then.").also { stage = END_DIALOGUE }
            40 -> {
                end()
                if (freeSlots(player!!) == 0) {
                    sendDialogue(player!!, "You don't have space for the reward.")
                } else {
                    npc(FaceAnim.CALM, "Certainly, I am not sure how, but it returned to me by some magic or other.")
                    addItem(player!!, Items.GHOSTLY_HOOD_6109, 1)
                }
            }
            // First path dialogue.
            101 -> npc(FaceAnim.HALF_GUILTY, "Ah, the evil swordsman Viggora...", "Paddewwa was where he fought many battles, perhaps", "he has returned to one of his old haunts?").also { stage = 39 }
            // Second path dialogue.
            102 -> npc(FaceAnim.HALF_GUILTY, "Ah, the evil swordsman Viggora...", "A rogue like him would probably flock to his own kind.").also { stage = 39 }
            // Third path dialogue.
            103 -> npc(FaceAnim.HALF_GUILTY, "Ah, the evil swordsman Viggora...", "Perhaps he has returned to his castle in the dark lands?").also { stage = 39 }
        }
    }
}
