package content.region.fremennik.rellekka.miniquest.shadow_maj.dialogue

import content.region.fremennik.rellekka.miniquest.shadow_maj.plugin.GeneralShadow
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Quests

/**
 * Represents the scouts dialogue.
 *
 * # Relations
 * - [GeneralShadow]
 */
@Initializable
class ScoutDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, ScoutDialogueFile(), npc)
        return false
    }

    override fun newInstance(player: Player?): Dialogue = ScoutDialogue(player)
    override fun getIds(): IntArray = intArrayOf(5574, 5575, 5576, 5577)
}

private class ScoutDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val progress = GeneralShadow.getShadowProgress(player!!)
        val receivedLeg = getAttribute(player!!, GeneralShadow.GS_RECEIVED_SEVERED_LEG, false)
        when (stage) {
            0 -> when {
                !GeneralShadow.hasGhostlySet(player!!) -> {
                    npc("Whoooo wooo Whooooooooo")
                    stage = END_DIALOGUE
                }
                getAttribute(player!!, GeneralShadow.GS_TRUSTWORTHY, false) -> {
                    player("Hello there! General Khazard sent me.")
                    stage = when (progress to npc?.id) {
                        1 to 5574 -> 1
                        2 to 5575 -> 101
                        3 to 5576 -> 201
                        4 to 5577 -> 301
                        else -> 100
                    }
                }
                !GeneralShadow.isComplete(player!!) && receivedLeg -> {
                    playerl(FaceAnim.FRIENDLY, "The General said I would find my reward near where the fish-men gather. I don't know what he means!")
                    stage = 401
                }
                GeneralShadow.isComplete(player!!) -> {
                    player("Hello again.")
                    stage = 100
                }
                else -> {
                    sendDialogue(player!!, "The Scout is too busy to talk.")
                    stage = END_DIALOGUE
                }
            }
            1 -> npc(FaceAnim.SCARED, "Gah! Don't sneak up on me!").also { stage++ }
            2 -> player(FaceAnim.GUILTY, "Uh, sorry.").also { stage++ }
            3 -> npc("I think I can see my heart pounding!").also { stage++ }
            4 -> player(FaceAnim.LAUGH, "You're pretty jumpy for a scout. I thought you'd be", "made of sterner stuff.").also { stage++ }
            5 -> npc("What is it you want exactly?").also { stage++ }
            6 -> player("I bring a message from the General!").also { stage++ }
            7 -> player("Khazard says: 'The planets are nearly alignment; we", "will meet in the place of half light and ice soon. Beware", "of the others, for though they are weak and few, they", "are cunning.'").also { stage++ }
            8 -> npc("Oh good, I long for the cold. This place is so hot! The", "General has told me much of his time spent here and I", "can see why he left.").also { stage++ }
            9 -> player("Oh, did the General live here?").also { stage++ }
            10 -> npc("Long ago, yes. But not by choice. His evil lord and", "master forced him and his kind to reside here.").also { stage++ }
            11 -> player("Khazard with a master? Hard to believe. Who was it?").also { stage++ }
            12 -> npc("I think I have said too much already.").also { stage++ }
            13 -> player("So what have you discovered on your travels though", "the desert?").also { stage++ }
            14 -> npc("The fighting in the desert has come to a stop. However, ", "there is still much distrust between Menaphos and Al", "Kharid. It seems like it would be possible to reignite the", "war if needed.").also { stage++ }

            15 -> if (isQuestComplete(player!!, Quests.DESERT_TREASURE)) {
                npc("I have heard whisperings in the bandit camp that the", "prisoner of Jaldraocht is now free.").also { stage++ }
            } else if (hasRequirement(player!!, Quests.ENAKHRAS_LAMENT) &&
                !isQuestComplete(player!!, Quests.DESERT_TREASURE)) {
                npc("I followed one called Lazim. He let slip that he had", "discovered the buried temple. Now the two others who", "were there are on the move. The General may find there are new players at", "the ritual this time.").also {
                    stage++
                }
            } else {
                player("The Desert seems to be a hotbed of information.", "Do you want me to relay any of this to the", "General?").also { stage++ }
            }
            16 -> npc("Not at this time. I must investigate further.").also { stage++ }
            17 -> player(FaceAnim.SAD, "Any idea where I can find the other scouts?").also { stage++ }
            18 -> npc("Hmm. I left one of them near a haunted wood - the", "trees kept trying to hit us. One scout headed for a", "jungle and the other for the gnomes.").also { stage++ }
            19 -> {
                end()
                GeneralShadow.setShadowProgress(player!!, 2)
            }
            100 -> npc("I can't speak to you; I must continue on my mission.").also { stage++ }

            101 -> npc("Finally, news! What message does he send?").also { stage++ }
            102 -> player("Err, it was something about planets.").also { stage++ }
            103 -> npc("Well, spit it out! I can't wait here all day.").also { stage++ }
            104 -> player("Khazard says: 'The planets are nearly alignment; we", "will meet in the place of half light and ice soon. Beware", "of the others, for though they are weak and few, they", "are cunning.'").also { stage++ }
            105 -> npc("Good. I long for the slaughter. These local squabbles", "and politics bore me. I wonder if the others have begun", "to move as well?").also { stage++ }
            106 -> player("The other what? Who are you slaughtering?").also { stage++ }
            107 -> npc("Ah, not for your ears, messenger!").also { stage++ }
            108 -> player("So how's your mission going. Enjoying the Draynor", "woods?").also { stage++ }
            109 -> if (isQuestComplete(player!!, Quests.VAMPIRE_SLAYER)) {
                npc("There is talk in the village that the Count of Draynor", "Manor has been Slain by some meddling adventurer. I will", "need to enter the manor to verify this.").also { stage++ }
            } else {
                player("Have you been in touch with any of the other scouts?").also { stage = 111 }
            }
            110 -> npc("Other than that, nothing of interest.").also { stage = 113 }
            111 -> npc(FaceAnim.FRIENDLY, "I'm too busy scouting to be in touch. One headed this", "direction with me seeking the place of heat and sand, another sought a place of moisture and growth and the last sought gnomes.").also { stage++ }
            112 -> npc(FaceAnim.FRIENDLY, "One headed this direction with me seeking the place of heat and sand, another sought a place of moisture and growth and the last sought gnomes.").also { stage++ }
            113 -> {
                end()
                GeneralShadow.setShadowProgress(player!!, 3)
            }

            201 -> npc("You must be a great mage to be able to see me, ", "human.").also { stage++ }
            202 -> player("So, are you a ghost?").also { stage++ }
            203 -> npc("Nothing so morbid. I am a scout for Khazard, and I", "move more easily through the land unseen. There are", "enemies everywhere.").also { stage++ }
            204 -> player("If you're not a ghost, why do I need an Amulet of", "Ghostspeak to talk to you?").also { stage++ }
            205 -> npc("Those who walk fully in the Shadow Realm, be they alive or dead, become", "warped by it and speak as ghosts do. That is why I", "cannot speak to you unless you wear the amulet.").also { stage++ }
            206 -> player("Oh. Well, here's your message.").also { stage++ }
            207 -> player("Khazard says: 'The planets are nearly alignment; we", "will meet in the place of half light and ice soon. Beware", "of the others, for though they are weak and few, they", "are cunning.'").also { stage++ }
            208 -> npc("Thank you, messenger.").also { stage++ }
            209 -> player("So, how's your mission going?").also { stage++ }
            210 -> npc("There is nothing pleasant about this land. The animals", "are vile, the temperature is unbearable and the plant life", "is often more dangerous than the animals.").also { stage++ }
            211 -> if (isQuestComplete(player!!, Quests.JUNGLE_POTION)) {
                npc("There were some strange occurrences in Tai Bwo", "Wannai. The people fled their village because their gods", "were angry with them, or the like, but now they have", "returned. I must investigate why.").also { stage++ }
            } else {
                player("Well, there's always something interesting going on here.").also { stage++ }
            }
            212 -> if (!hasRequirement(player!!, Quests.SHILO_VILLAGE)) {
                player("Well, there's always something interesting going on here.").also { stage++ }
            } else {
                npc("Shilo Village was overrun with the undead, but someone", "of great ability has brought the situation under control.").also { stage++ }
            }
            213 -> player("Any idea where the other scouts are?").also { stage++ }
            214 -> npc("I am a scout, not a spy. I do not keep track of them.").also { stage++ }
            215 -> npc("I was told to go south, so here I am. I know one was", "to investigate the gnomes. I believe one headed towards", "Asgarnia. Another was told to go to a place of great", "heat, but that they would have to sneak through a pass.").also { stage++ }
            216 -> {
                end()
                GeneralShadow.setShadowProgress(player!!, 4)
            }

            301 -> npc("What news, skinbag?").also { stage++ }
            302 -> player("Hey, no need to be rude.").also { stage++ }
            303 -> npc("Your smell disgusts me, so make it quick.").also { stage++ }
            304 -> player("Khazard says: 'The planets are nearly alignment; we", "will meet in the place of half light and ice soon. Beware", "of the others, for though they are weak and few, they", "are cunning.'").also { stage++ }
            305 -> npc(FaceAnim.ANNOYED, "I'll be glad when all this is over. The General will be", "strong again and we will push back this tide of filth that", "is humanity.").also { stage++ }
            306 -> player(FaceAnim.ANGRY, "Oi! I am one of the filth, I mean, humans! Besides, we", "were here first.").also { stage++ }
            307 -> npc(FaceAnim.EXTREMELY_SHOCKED, "Haa, haa, haa. Oh, you naive mortals. There were many", "here before you. The last time the General went north, ", "all this land belonged to the elves. Do not think so", "highly of your race.").also { stage++ }
            308 -> player("Discovered anything interesting on your travels?").also { stage++ }
            309 -> if (hasRequirement(player!!, Quests.BIOHAZARD) || isQuestComplete(player!!, Quests.PLAGUE_CITY)) {
                npc("As I passed through Ardougne, I overheard some news.", "The plague in the west grows worse. King Lathas's", "popularity continues to increase, while the people are", "becoming more and more hostile to Tyras. I suspect").also { stage++ }
            } else {
                player("That is all that I have heard.").also { stage = 313 }
            }

            310 -> npc("there is more to be learned, but my mission is to", "observe gnomes, not humans.").also { stage++ }
            311 -> if (!hasRequirement(player!!, Quests.HAZEEL_CULT)) {
                npcl(FaceAnim.FRIENDLY, "I have heard that the cultists, who think they are so well hidden in the sewers, have failed in their ritual.").also { stage++ }
            } else {
                npcl(FaceAnim.FRIENDLY, "I have heard that the cultists, who think they are so well hidden in the sewers, have succeeded in their ritual.").also { stage++ }
            }
            312 -> if (!isQuestComplete(player!!, Quests.THE_GRAND_TREE)) {
                player("That is all that I have heard.").also { stage = 314 }
            } else {
                npcl(FaceAnim.FRIENDLY, "I head to the land of the gnomes; rumours say it is peaceful right now.").also { stage++ }
            }
            313 -> npcl(FaceAnim.FRIENDLY, "But I hear Glough has lost his position as advisor.").also { stage++ }
            314 -> {
                end()
                GeneralShadow.setShadowProgress(player!!, 4)
            }

            401 -> npcl(FaceAnim.NEUTRAL, "The General is a bit...cliche. Fish-men are fishermen. Now, away with you!").also { stage = END_DIALOGUE }
        }
    }
}
