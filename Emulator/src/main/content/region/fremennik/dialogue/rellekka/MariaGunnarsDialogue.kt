package content.region.fremennik.dialogue.rellekka

import content.region.fremennik.handlers.TravelDestination
import content.region.fremennik.handlers.WaterbirthTravel.sail
import core.api.*
import core.api.quest.isQuestComplete
import core.api.quest.requireQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MariaGunnarsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (npc.id == NPCs.MARIA_GUNNARS_5508) {
            if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I do not speak to outlanders. Maybe you should speak with Rellekka's Chieftain.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            } else {
                npcl(
                    FaceAnim.HAPPY,
                    "Welcome back, ${getAttribute(player!!, "fremennikname", "name")}. Do you have any questions?",
                ).also {
                    stage =
                        22
                }
            }
        } else {
            npcl(
                FaceAnim.HAPPY,
                "Hi, ${getAttribute(
                    player!!,
                    "fremennikname",
                    "name",
                )}, you should speak with Burgher Mawnis Burowgar in the main chambers. Do you have any questions?",
            ).also {
                stage =
                    0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Tell me about your husband.",
                    "Tell me about Jatizso.",
                    "Tell me about Neitiznot.",
                    "Can you ferry me to Rellekka?",
                    "I have nothing to ask you right now.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Tell me about your husband.").also { stage++ }
                    2 -> player("Tell me about Jatizso.").also { stage = 8 }
                    3 -> player("Tell me about Neitiznot.").also { stage = 16 }
                    4 -> player("Can you ferry me to Rellekka?").also { stage = 19 }
                    5 -> player("I have nothing to ask you right now.").also { stage = 21 }
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "My husband is a typical Fremennik, being both headstrong and brave.",
                ).also { stage++ }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "We fell in love when we were young, but as there was a war between our isles, we were ordered to stop seeing each other by our parents.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This didn't stop Mord climbing the wailing tower, on his side of the channel between our isles, to sing to me every night.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "After Mord's voice broke, his singing voice was lost, but I still listened to his terrible sonnets each and every night.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Until one night there was no singing! Then, far below me, I heard a gentle voice calling my name.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Mord had stolen his father's boat to come and fetch me from my prison and from that day forth we have lived in Rellekka.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "My husband does not speak of the inhabitants of the isle of Jatizso much.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "He has little positive to say about his parents, so he chooses not to speak of them in my presence.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The only thing I've noticed is that everyone on that island seems very sad...",
                ).also {
                    stage++
                }
            11 -> options("Tell me about King Sorvott IV.", "What were you saying before?").also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> player("Tell me about King Sorvott IV.").also { stage++ }
                    2 -> player("What were you saying before?").also { stage = 0 }
                }
            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "King Sorvott IV follows in the footsteps of a line of kings who have reigned since Jatizso himself passed away.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "It is rumoured that the King has no heir and is seeking a bride to bear him children.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Given his personality I'm not surprised he hasn't found a bride yet!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            16 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "To be honest with you, I do not keep up to date with all the goings on here now that I have a home in Rellekka.",
                ).also {
                    stage++
                }
            17 -> npcl(FaceAnim.HAPPY, "However, Neitiznot seems a happy place!").also { stage++ }
            18 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The Burgher seems a wiser rule than King Sorvott, although I believe he is worried about the trolls at the moment. They seem to be a big threat.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            19 -> npc("Sure.").also { stage++ }
            20 -> end().also { sail(player, TravelDestination.NEITIZNOT_TO_RELLEKKA) }
            21 -> npc("Fare thee well.").also { stage = END_DIALOGUE }

            22 ->
                options(
                    "Tell me about Neitiznot's geography.",
                    "Tell me about the islands' history.",
                    "Tell me about Burgher Mawnis Burowgar.",
                    "Can you ferry me to Neitiznot?",
                    "I just stopped to say 'hello'.",
                ).also { stage++ }

            23 ->
                when (buttonId) {
                    1 -> player("Tell me about Neitiznot's geography.").also { stage++ }
                    2 -> player("Tell me about the islands' history.").also { stage = 39 }
                    3 -> player("Tell me about Burgher Mawnis Burowgar.").also { stage = 79 }
                    4 -> player("Can you ferry me to Neitiznot?").also { stage = 90 }
                    5 -> player("I just stopped to say 'hello'.").also { stage = 92 }
                }
            24 -> npcl(FaceAnim.HALF_GUILTY, "Sure.").also { stage++ }
            25 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Well, as I have mentioned, I am from an island called Jatizso, while my wife is from another island called Neitiznot.",
                ).also {
                    stage++
                }
            26 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Neitiznot is a far less sophisticated and successful island which will not see reason with Jatizso.",
                ).also {
                    stage++
                }
            27 -> npcl(FaceAnim.HALF_GUILTY, "Ne it iz not!").also { stage++ }
            28 -> sendNPCDialogue(player, NPCs.MORD_GUNNARS_5481, "Ja it iz so!").also { stage++ }
            29 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Ahem, anyway, the islands are in a small volcanic archipelago north of here.",
                ).also {
                    stage++
                }
            30 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Along with the main isles there are numerous small islands and a frozen northern island that was recently overrun by trolls.",
                ).also {
                    stage++
                }
            31 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Jatizso is rich in minerals, and trades extensively, while Neitiznot has stolen all the decent arable land.",
                ).also {
                    stage++
                }
            32 -> npcl(FaceAnim.HALF_GUILTY, "Only because Jatizso hogs all the wealth.").also { stage++ }
            33 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Well, that wealth is needed to buy the food from Neitiznot at extortionate rates!",
                ).also {
                    stage++
                }
            34 -> npcl(FaceAnim.HALF_GUILTY, "Only because Jatizso hogs all the wealth!").also { stage++ }
            35 -> sendNPCDialogue(player, NPCs.MORD_GUNNARS_5481, "Ok Maria, stop that!").also { stage++ }
            36 -> npcl(FaceAnim.HALF_GUILTY, "Ok dear!").also { stage++ }
            37 -> sendNPCDialogue(player, NPCs.MORD_GUNNARS_5481, "Thank you!").also { stage++ }
            38 ->
                npcl(FaceAnim.HALF_GUILTY, "Where was I? Yup, that covers the layout of the islands.").also {
                    stage =
                        END_DIALOGUE
                }
            39 -> player("Tell me more about Jatizso.").also { stage++ }
            40 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Well now, the history of the isles is a bloody tale. When told in its intended form it takes 9 days and 9 nights. However, I will give you the abridged version.",
                ).also {
                    stage++
                }
            41 -> player("Phew!").also { stage++ }
            42 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Four generations ago there was a mighty king who ruled wisely and was loved by all.",
                ).also {
                    stage++
                }
            43 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "He had many sons, most of whom proved themselves in the usual Fremennik ways of fighting, sailing, drinking and so on.",
                ).also {
                    stage++
                }
            44 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "His youngest two sons, however, were a problem. Ever since they were old enough to hit one another they had bickered and fought.",
                ).also {
                    stage++
                }
            45 -> npcl(FaceAnim.HALF_GUILTY, "Their names were Jatizso and Neitiznot.").also { stage++ }
            46 -> sendNPCDialogue(player, NPCs.MORD_GUNNARS_5481, "Hey! Who's telling this story?").also { stage++ }
            47 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Ahem. At the time it was customary for the Kings' sons to claim a prize from his treasury when they came of age.",
                ).also {
                    stage++
                }
            48 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Each brother rallied support and strode out to confront the other on the field of battle to claim the same prize.",
                ).also {
                    stage++
                }
            49 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "The brothers met on the field, limbs were bruised, bones were crushed and heads clashed.",
                ).also {
                    stage++
                }
            50 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They probably didn't notice the head-clashing bit. Too thick skulled!",
                ).also {
                    stage++
                }
            51 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Can I finish the story without interruptions?",
                ).also { stage++ }
            52 -> npcl(FaceAnim.HALF_GUILTY, "Carry on dear.").also { stage++ }
            53 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Anyway, long into the night they fought, until they stood alone, facing each other.",
                ).also {
                    stage++
                }
            54 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "On the second day the old king, whose patience was exhausted, strode out onto the field and demanded to know who had claimed his prize.",
                ).also {
                    stage++
                }
            55 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Jatizso looked at his father and told him yes, it was his, to which the other called out 'No! It's not'.",
                ).also {
                    stage++
                }
            56 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "And so, the argument continued through to the third day, with the sounds of 'Jah! 'tis so!', 'Ne! It's not!' echoing over the field, becoming more hoarse as the darkness enveloped the brothers.",
                ).also {
                    stage++
                }
            57 -> npcl(FaceAnim.HALF_GUILTY, "Bah! What they needed was a good clip round the ear.").also { stage++ }
            58 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "At the end of the third day, when the brothers lay unable to move, let alone stand, the King stood between them in shame.",
                ).also {
                    stage++
                }
            59 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "As punishment for their squabbling he banished his sons from his realm, ordering that neither should return until they had conquered lands of their own.",
                ).also {
                    stage++
                }
            60 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Jatizso set sail with a band of loyal followers, sailing into the east, while Neitiznot went west.",
                ).also {
                    stage++
                }
            61 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Jatizso ventured far into uncharted oceans, his crew dwindling as the trials of sailing and sea monsters took them.",
                ).also {
                    stage++
                }
            62 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Jatizso's ship limped through the frozen wastes, in need of repairs, fighting off pirates who thought them easy pickings.",
                ).also {
                    stage++
                }
            63 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "At last a promising land was sighted, a land rich in minerals, deep fjords and sweeping mountains.",
                ).also {
                    stage++
                }
            64 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Jatizso claimed the land and named it after himself.",
                ).also {
                    stage++
                }
            65 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "A mighty citadel was constructed to protect his land; then Jatizso set forth to explore his new kingdom.",
                ).also {
                    stage++
                }
            66 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Crossing a few fjords, Jatizso spied an island which remained green and fertile, even at such latitude.",
                ).also {
                    stage++
                }
            67 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "With a mighty cheer Jatizso and his men marched on the island. Upon reaching the island, however, they discovered a wooden stockade.",
                ).also {
                    stage++
                }
            68 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Determined to be master of all the land, Jatizso stood in front of the gates. He looked up and demanded that those within the stockade surrendered.",
                ).also {
                    stage++
                }
            69 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Jatizso was horrified when his brother appeared on the battlements and cried 'This is my land! Go find yer own'.",
                ).also {
                    stage++
                }
            70 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "To which his brother replied 'No it's not!', throwing some rocks for good measure.",
                ).also {
                    stage++
                }
            71 -> sendNPCDialogue(player, NPCs.MORD_GUNNARS_5481, "And so the blood feud continued.").also { stage++ }
            72 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "The fighting between the towns has continued for generations with little sign of improvement.",
                ).also {
                    stage++
                }
            73 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Quite recently a massive troll army seized one of the islands to the north, while our townships' militias were busy fighting one another.",
                ).also {
                    stage++
                }
            74 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "They marched down and razed Neitiznot to the ground while Neitiznot's troops laid siege to Jatizso.",
                ).also {
                    stage++
                }
            75 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The smoke damage was atrocious, I had to replace my entire wardrobe.",
                ).also {
                    stage++
                }
            76 -> sendNPCDialogue(player, NPCs.MORD_GUNNARS_5481, "Yes, dear.").also { stage++ }
            77 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "Anyway, a hasty sort of peace has been agreed until the trolls are dealt with.",
                ).also {
                    stage++
                }
            78 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "But you can still see the bitter rivalry as guards hurl insults between the towns.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            79 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well Burgher Mawnis Burowgar is a gentle soul at heart. His kinsfolk are happy and content with their lot.",
                ).also {
                    stage++
                }
            80 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "'Soft' is the word we use in Jatizso.",
                ).also { stage++ }
            81 -> npcl(FaceAnim.HALF_GUILTY, "Well a soft Burgher is better than a stupid King.").also { stage++ }
            82 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Anyway, there was a time when Burgher Burowgar was similar to King Sorvott.",
                ).also {
                    stage++
                }
            83 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Argumentative, short tempered; I am sure you know what I mean.",
                ).also { stage++ }
            84 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The change happened when the trolls came and razed Neitiznot to the ground.",
                ).also {
                    stage++
                }
            85 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "You mean while Neitiznot's militias were assaulting Jatizso.",
                ).also {
                    stage++
                }
            86 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Yes, well our tribes have been at war for nearly four generations now.",
                ).also {
                    stage++
                }
            87 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Anyway, the Burgher lost his wife and youngest son in the trolls' raid and has been a changed man since.",
                ).also {
                    stage++
                }
            88 ->
                sendNPCDialogue(
                    player,
                    NPCs.MORD_GUNNARS_5481,
                    "May their journey to our ancestors' halls be swift.",
                ).also {
                    stage++
                }
            89 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Personally, I am much happier with the considerate Burgher we have now.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            90 -> npc("Let's set sail then.").also { stage++ }
            91 -> {
                end()
                if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) return true
                sail(player, TravelDestination.RELLEKKA_TO_NEITIZNOT)
            }
            92 -> npc("Thanks!").also { stage++ }
            93 -> player("I may be back later.").also { stage++ }
            94 -> end().also { findLocalNPC(player, npc.id)?.let { it1 -> sendChat(it1, "Bye!") } }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MariaGunnarsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MARIA_GUNNARS_5507, NPCs.MARIA_GUNNARS_5508)
    }
}
