package content.region.fremennik.rellekka.dialogue

import content.region.fremennik.plugin.FremennikBoat
import content.region.fremennik.plugin.Travel
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Jarvald dialogue.
 * (Varbit 814, Wrapper 2345)
 */
@Initializable
class JarvaldDialogue(player: Player? = null) : Dialogue(player) {

    private val FIRST_TIME_JOIN_VARBIT = 814

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (npc.id == NPCs.JARVALD_2438) {
            if(!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                npcl(FaceAnim.HALF_ASKING, "Ah, you live yet, outerlander! Have you had your fill of the hunt and wish to return, or are you still feeling the joy of the cull?")
                stage = 56
            } else {
                npcl(FaceAnim.HAPPY, "Ah, ${FremennikTrials.getFremennikName(player)}! Such glorious battle makes you feel glad of life, does it not?")
                stage = 62
            }
            return true
        }
        if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(FaceAnim.HAPPY, "Greetings, ${FremennikTrials.getFremennikName(player)}!").also { stage = 4 }
        } else {
            npc(FaceAnim.ANNOYED, "What do you want from me outerlander?", "It is our policy not to associate with those not of our tribe.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val firstTimeJoin = getVarbit(player, FIRST_TIME_JOIN_VARBIT)
        val hasPaid = removeItem(player, Item(Items.COINS_995, 1000))
        when (stage) {
            0 -> showTopics(
                Topic("Where is your chieftain?", 1, true),
                Topic("What Jarvald is doing.", 5, true),
                Topic("Nothing.", 30, true)
            )
            1 -> playerl(FaceAnim.HALF_ASKING, "Where is your chieftain? I find it highly discriminatory to refuse to talk to someone on the grounds that they are not part of your tribe.").also { stage++ }
            2 -> npcl(FaceAnim.ANNOYED, "I don't rightly understand your speech outerlander, but my loyalty is with Chieftain Brundt.").also { stage++ }
            3 -> npcl(FaceAnim.ANNOYED, "He resides in our long hall; it is the large building over there, you should speak to him for he speaks for us all.").also { stage = 100 }
            4 -> showTopics(
                Topic("What Jarvald is doing.", 37, true),
                Topic("Nothing.", 30, true),
            )
            5 -> player(FaceAnim.HALF_ASKING, "So what are you doing here?").also { stage++ }
            6 -> npcl(FaceAnim.NEUTRAL, "This should not concern you, outerlander. I am awaiting other Fremenniks to join me on an expedition to Waterbirth Island.").also { stage++ }
            7 -> showTopics(
                Topic("Waterbirth Island?", 8),
                Topic("Can I come?", if(firstTimeJoin == 1) 19 else 11, true),
                Topic("Nice hat!", 24, true),
                Topic("Ok, 'bye.", 31, true)
            )
            8 -> npcl(FaceAnim.NEUTRAL,"It is a small crescent-shaped island just north-west of here, outerlander. We have many legends about it, such as the tale of the broken sky, and the day of the green seas.").also { stage++ }
            9 -> npcl(FaceAnim.NEUTRAL, "The reason I am travelling there is more serious Fremennik business, however. I doubt an outerlander would be interested.").also { stage = 7 }

            11 -> playerl(FaceAnim.HALF_THINKING,"Hey, scary barbarian type guy, think I can join you on this expedition?").also { stage++ }
            12 -> npcl(FaceAnim.THINKING,"An outerlander join us on an honoured hunt??? Well.... I guess... I might be able to allow you to join us, although it is a breach of many of our customs...").also { stage++ }
            13 -> player(FaceAnim.SAD,"Oh, pleeeeease? I really LOVE killing stuff!").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY,"Well... I remain unconvinced that it would be wise to allow an outerlander to join us in such dangerous battle, but your enthusiasm seems genuine enough...").also { stage++ }
            15 -> npcl(FaceAnim.NEUTRAL,"I will allow you to escort us, but you must pay me a sum of money first.").also { stage++ }
            16 -> playerl(FaceAnim.HALF_ASKING,"What? That's outrageous, why charge me money? And, uh, how much does it cost me?").also { stage++ }
            17 -> npcl(FaceAnim.NEUTRAL,"Ah, the outerlanders have stolen from my people for many years, in this way you can help my community with a small amount of money... Let us say... 1000 coins. Payable in advance, of course.").also { stage++ }
            18 -> npcl(FaceAnim.NEUTRAL, "For this I will take you to Waterbirth Island on my boat, and will bring you back here when you have had your fill of the hunt. Assuming you are still alive to wish to leave, of course.").also { stage++ }
            19 -> npcl(FaceAnim.HALF_ASKING, "So do you have the 1000 coins for my service, and are you ready to leave now?").also { stage++ }
            20 -> showTopics(
                Topic("YES", 21, true),
                Topic("NO", 22, true),
            )
            21 -> {
                end()
                if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                    if (!hasPaid) {
                        sendMessage(player, "You cannot afford that.")
                        npcl(FaceAnim.NEUTRAL, "Ah, outerlander... I go beyond my duty to allow you to accompany me, and you attempt to swindle me out of a reasonable fee? This is no way even for an outerlander to behave...")
                        stage = 100
                        return true
                    }
                }
                if (firstTimeJoin != 1) {
                    val message = if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                        "I suggest you head to the cave with some urgency outerlander, the cold air out here might be too much for the likes of you..."
                    } else {
                        "I would head straight for the cave and not tarry too long ${FremennikTrials.getFremennikName(player)}, the cold winds on this island can cut right through you should you spend too long in them."
                    }
                    npcl(FaceAnim.NEUTRAL, message)
                }
                FremennikBoat.sail(player, Travel.RELLEKKA_TO_WATERBIRTH)
                setVarbit(player, FIRST_TIME_JOIN_VARBIT, 1, true)
            }
            22 -> playerl(FaceAnim.HALF_GUILTY, "No, actually I have some stuff to do here first.").also { stage++ }
            23 -> npcl(FaceAnim.HALF_GUILTY,"As you wish. Come and see me when your bloodlust needs sating.").also { stage = 100 }
            24 -> playerl(FaceAnim.FRIENDLY, "Hey, I have to say, that's a fine looking hat you are wearing there.").also { stage++ }
            25 -> npcl(FaceAnim.FRIENDLY, "Aye, that it is ${FremennikTrials.getFremennikName(player)}!").also { stage++ }
            26 -> npcl(FaceAnim.FRIENDLY, "Skulgrimen fashioned it for me from the carcass of one of the monsters on Waterbirth Island after our last hunt!").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "I hope to kill enough creatures to fashion some fine armour as well when next we leave!").also { stage = 7 }

            30 -> playerl(FaceAnim.HALF_GUILTY, "Actually, I don't think I have anything to speak to you about...").also { stage = 100 }
            31 -> playerl(FaceAnim.HALF_GUILTY, "Wow, you Fremenniks sure know how to party. Well, see ya around.").also { stage++ }
            32 -> end()
            33 -> {
                val name = FremennikTrials.getFremennikName(player)
                if(name.equals(npc.name, true)) {
                    npcl(FaceAnim.HAPPY, "Ah, and what a glorious name that is! Worthy of only the finest warriors!").also { stage = 35 }
                } else {
                    npcl(FaceAnim.HALF_ASKING, "So what brings you back to fair Rellekka? It has been too long since you have drunk in the long hall with us and sang of your battles!").also { stage = 36 }
                }
            }
            34 -> npcl(FaceAnim.HALF_ASKING, "So what brings you back to fair Rellekka? It has been too long since you have drunk in the long hall with us and sang of your battles!").also { stage = 36 }
            35 -> playerl(FaceAnim.HALF_GUILTY, "Heh. Yup, you're right there.").also { stage = 34 }
            36 -> showTopics(
                Topic("What Jarvald is doing.", 37, true),
                Topic("Can I come?", 40, true),
                Topic("Nice hat!", 48, true),
                Topic("Ok, 'bye.", 32),
                Topic("Nothing", 100, true)
            )
            37 -> player(FaceAnim.HALF_ASKING, "So what are you doing here?").also { stage = 38 }
            38 -> npcl(FaceAnim.NEUTRAL, "You have not heard, ${FremennikTrials.getFremennikName(player)}? I am leading an expedition to Waterbirth Island!").also { stage = 39 }
            39 -> showTopics(
                Topic("Waterbirth Island?", 41),
                Topic("Can I come?", 40, true),
                Topic("Nice hat!", 48, true),
                Topic("Ok, 'bye.", 32)
            )
            40 -> npcl(FaceAnim.FRIENDLY, "Of course, ${FremennikTrials.getFremennikName(player)}! Your presence is more than welcome on this cull! You wish to leave now?").also { stage = 53 }
            41 -> npcl(FaceAnim.HALF_ASKING, "You have not ever travelled to Waterbirth Island, ${FremennikTrials.getFremennikName(player)}? I am surprised, it is a place of outstanding natural beauty.").also { stage++ }
            42 -> npcl(FaceAnim.NEUTRAL, "Or at least it used to be! But things have now changed...").also { stage++ }
            43 -> playerl(FaceAnim.HALF_ASKING, "Changed? How do you mean, changed?").also { stage++ }
            44 -> npcl(FaceAnim.NEUTRAL, "It seems as though the sea-beasts known to us as the daggermouths have begun their hatching once again... And there may be others of their ilk there too.").also { stage++ }
            45 -> player(FaceAnim.HALF_ASKING, "Daggermouths?").also { stage++ }
            46 -> npcl(FaceAnim.NEUTRAL, "Aye, the daggermouths! The vile creatures lived near here once, but we had thought them all driven back to the ocean depths many moons past.").also { stage++ }
            47 -> npcl(FaceAnim.NEUTRAL, "I can only imagine a daggermouth queen has nested somewhere nearby, and spawned her foul brood under the sea once more, and some of them have migrated to fair Waterbirth Island.").also { stage++ }
            48 -> playerl(FaceAnim.HALF_ASKING, "So you're scared they might attack Rellekka?").also { stage++ }
            49 -> npcl(FaceAnim.LAUGH, "Scared? Ha ha ha!").also { stage++ }
            50 -> npcl(FaceAnim.NEUTRAL, "You wound us with your questioning, ${FremennikTrials.getFremennikName(player)}!").also { stage = 51 }
            51 -> npcl(FaceAnim.NEUTRAL, "We are glad the daggermouths have returned to these shores, for it means we will get the chance to hunt them once again as our ancestors did!").also { stage++ }
            52 -> npcl(FaceAnim.NEUTRAL, "When treated in the correct manner, the creatures' remains can be used to make fine battleworthy armour!").also { stage = 39 }
            53 -> showTopics(
                Topic("YES", 54, true),
                Topic("NO", 55, true)
            )
            54 -> {
                end()
                if (firstTimeJoin != 1) {
                    npcl(FaceAnim.FRIENDLY, "I would head straight for the cave and not tarry too long ${FremennikTrials.getFremennikName(player)}, the cold winds on this island can cut right through you should you spend too long in them.")
                }
                FremennikBoat.sail(player, Travel.RELLEKKA_TO_WATERBIRTH)
            }
            55 -> playerl(FaceAnim.HALF_GUILTY, "No, actually I have some stuff to do here first.")
            56 -> sendOptions(player, "Leave island?", "YES", "NO").also { stage++ }
            57 -> when (buttonId) {
                1 -> {
                    end()
                    if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                        if (!hasPaid) {
                            sendMessage(player, "You cannot afford that.")
                            npcl(FaceAnim.NEUTRAL, "Ah, outerlander... I go beyond my duty to allow you to accompany me, and you attempt to swindle me out of a reasonable fee? This is no way even for an outerlander to behave...")
                            stage = 100
                            return true
                        }
                    }
                    if (firstTimeJoin != 1) {
                        npcl(FaceAnim.FRIENDLY, "Then let us away; There will be death to bring here another day!")
                    }
                    FremennikBoat.sail(player, Travel.WATERBIRTH_TO_RELLEKKA)
                }
                2 -> npcl(FaceAnim.LOUDLY_LAUGHING, "Ha Ha Ha! A true huntsman at heart!").also { stage++ }
            }
            58 -> npcl(FaceAnim.HAPPY, "I myself have killed over a hundred of the daggermouths, and did not think it too many!").also { stage = 100 }
            59 -> playerl(FaceAnim.HAPPY, "Hey, I have to say, that's a fine looking hat you are wearing there.").also { stage++ }
            60 -> npcl(FaceAnim.FRIENDLY, "Aye, that it is ${FremennikTrials.getFremennikName(player)}! Skulgrimen fashioned it for me from the carcass of one of the monsters on Waterbirth Island after our last hunt!").also { stage++ }
            61 -> npcl(FaceAnim.HAPPY, "I hope to kill enough creatures to fashion some fine armour as well when next we leave!").also { stage = 100 }
            62 -> npcl(FaceAnim.HALF_ASKING, "So what say you, stay here for the hunt, or return home to sweet Rellekka to feast and drink with your tribe?").also { stage = 56 }
            100 -> end()
        }

        return true
    }

    override fun newInstance(player: Player?): Dialogue = JarvaldDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.NULL_2435, NPCs.JARVALD_2436, NPCs.JARVALD_2437, NPCs.JARVALD_2438)
}