package content.region.asgarnia.falador.dialogue

import content.data.GameAttributes
import content.global.skill.construction.CrestType
import core.ServerConstants
import core.Util
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents Sir Renitee.
 *
 * **Relations**
 * - [Family Crests][content.global.skill.construction.CrestType]
 */
@Initializable
class SirReniteeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hmm? What's that, young " + (if (player.appearance.isMale) "man" else "woman") + "? What can I do for", "you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I don't know, what can you do for me?", "Nothing, thanks").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "I don't know, what can you do for me?").also { stage = 3 }
                2 -> player(FaceAnim.HALF_GUILTY, "Nothing, thanks.").also { stage++ }
            }
            2 -> npc(FaceAnim.HALF_GUILTY, "Mmm, well, see you some other time maybe.").also { stage = END_DIALOGUE }
            3 -> npc(FaceAnim.HALF_GUILTY, "Hmm, well, mmm, do you have a family crest? I keep", "track of every " + ServerConstants.SERVER_NAME + " family, you know, so I might", "be able to find yours.").also { stage++ }
            4 -> npc(FaceAnim.HALF_GUILTY, "I'm also something of an, mmm, a painter. If you've", "met any important persons or visited any nice places I", "could paint them for you.").also { stage++ }
            5 -> showTopics(
                Topic("Can you see if I have a family crest?", 6),
                Topic("Can I buy a painting?", 35)
            )
            6 -> npc(FaceAnim.HALF_GUILTY, "What is your name?").also { stage++ }
            7 -> player(FaceAnim.HALF_GUILTY, player.username + ".").also { stage++ }
            8 -> npc(FaceAnim.HALF_GUILTY, "Mmm, " + player.username + ", let me see...").also { stage++ }
            9 -> {
                if (getStatLevel(player, Skills.CONSTRUCTION) >= 16) {
                    if (getAttribute(player, "sir-renitee-assigned-crest", false)) {
                        val crestName = player.houseManager.crest.name.lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                        npcl(FaceAnim.HALF_GUILTY, "According to my records, your crest is the symbol of $crestName.")
                    } else {
                        val crests =
                            arrayOf(CrestType.VARROCK, CrestType.ASGARNIA, CrestType.KANDARIN, CrestType.MISTHALIN)
                        val crest = crests[RandomFunction.random(crests.size)]
                        player.houseManager.crest = crest
                        setAttribute(player, "/save:sir-renitee-assigned-crest", true)
                        when (crest) {
                            CrestType.ASGARNIA -> setAttribute(player, GameAttributes.FAMILY_CREST, 2)
                            CrestType.KANDARIN -> setAttribute(player, GameAttributes.FAMILY_CREST, 10)
                            CrestType.MISTHALIN -> setAttribute(player, GameAttributes.FAMILY_CREST, 11)
                            else -> setAttribute(player, GameAttributes.FAMILY_CREST, 15)
                        }
                        val message = if (crest == CrestType.VARROCK) "you can use that city's" else "that can be your"
                        val crestString = Util.enumToString(player.houseManager.crest.name)
                        npc(FaceAnim.HALF_GUILTY, "Well, I don't think you have any noble blood,", "but I see that your ancestors came from $crestString,", " so $message crest.")
                    }
                    if (!player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.isComplete(0, 4)) {
                        player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.updateTask(player, 0, 4, true)
                    }
                    stage = 11
                } else {
                    val title = if (player.appearance.isMale) "man" else "woman"
                    npc(FaceAnim.HALF_GUILTY, "First thing's first, young $title! There is not much point", "in having a family crest if you cannot display it.").also { stage++ }
                }
            }
            10 -> npc(FaceAnim.HALF_GUILTY, "You should train construction until you can build a wall", "decoration in your dining room.").also { stage = END_DIALOGUE }
            11 -> showTopics(
                Topic("I don't like that crest. Can I have a different one?", 13),
                Topic("Thanks!", 12)
            )
            12 -> npc(FaceAnim.HALF_GUILTY, "You're welcome, my " + (if (player.appearance.isMale) "boy." else "girl.")).also { stage = END_DIALOGUE }
            13 -> npc(FaceAnim.HALF_GUILTY, "Mmm, very well. Changing your crest will cost", "5,000 coins.").also { stage++ }
            14 -> if (!inInventory(player, Items.COINS_995, 5000)) {
                player(FaceAnim.HALF_GUILTY, "I'll have to go and get some money then.").also { stage = END_DIALOGUE }
            } else {
                npc(FaceAnim.HALF_GUILTY, "There are sixteen different symbols; which one would", "you like?").also { stage++ }
            }
            15 -> showTopics(
                Topic("Shield of Arrav", 16, true),
                Topic("Asgarnia", 17, true),
                Topic("Dorgeshuun Symbol", 18, true),
                Topic("Dragon", 19, true),
                Topic("More...", 20, true)
            )
            20 -> showTopics(
                Topic("Fairy", 21, true),
                Topic("Guthix", 22, true),
                Topic("HAM", 23, true),
                Topic("Horse", 24, true),
                Topic("More...", 25, true)
            )
            25 -> showTopics(
                Topic("Jogre", 26, true),
                Topic("Kandarin", 27, true),
                Topic("Misthalin", 28, true),
                Topic("Money", 29, true),
                Topic("More...", 30, true)
            )
            30 -> showTopics(
                Topic("Saradomin", 31, true),
                Topic("Skull", 32, true),
                Topic("Varrock", 33, true),
                Topic("Zamorak", 34, true),
                Topic("More...", 15, true)
            )
            16,17,18,19,21,22,23,24,26,27,28,29,31,32,33,34 -> {
                val crestData: Map<Int, Triple<CrestType, String, String>> = mapOf(
                    16 to Triple(CrestType.ARRAV, "Ah yes, the shield that you helped to retrieve. You have certainly earned the right to wear its symbol.", "But that legendary shield is still lost! I don't think it would be proper for you to wear its symbol."),
                    17 to Triple(CrestType.ASGARNIA, "Ah, splendid, splendid. There is no better symbol than that of our fair land!", ""),
                    18 to Triple(CrestType.DORGESHUUN, "Ah yes, our new neighbours under Lumbridge. I hear you were the one who made contact with them, jolly good.", "Hmm, have you ever even met the Dorgeshuun? I don't think you should wear their symbol until you have made contact with that lost tribe."),
                    19 to Triple(CrestType.DRAGON, "I see you are a mighty dragon-slayer! You have certainly earned the right to wear a dragon symbol.", "When the dragon on Crandor Isle remains undefeated? I think you should prove yourself a dragon-slayer before you can wear a dragon symbol!"),
                    21 to Triple(CrestType.FAIRY, "Hmm, mmm, yes, everyone likes pretty fairies.", "A fairy? Fairies are rumoured to exist in a lost city somewhere. I don't think I should let you use them as a symbol until you have met them in person."),
                    22 to Triple(CrestType.GUTHIX, "Guthix, god of balance! I'm a Saradominist myself, you know, but we all find meaning in our own way, what?", "You do not seem to be very devoted to any god. I will not let you have a divine symbol unless you have level 70 prayer."),
                    23 to Triple(CrestType.HAM, "Hmm, I'm not sure I like that HAM group, their beliefs are a little extreme for me. But if that's what you want.", ""),
                    24 to Triple(CrestType.HORSE, "Ah, I see you've brought a toy horse for me to see. An interesting beast. Certainly you can use that as your crest if you like, although it seems a bit strange to me.", "A horse? I know people talk about them, but I'm not at all sure they ever existed. I don't think I could let you use as your symbol unless you can fetch me some kind of model of one."),
                    26 to Triple(CrestType.JOGRE, "A Jungle Ogre, eh? Odd beast, very odd.", ""),
                    27 to Triple(CrestType.KANDARIN, "Our neighbours in the west? Very good, very good.", ""),
                    28 to Triple(CrestType.MISTHALIN, "Ah, the fair land of Lumbridge and Varrock.", ""),
                    29 to Triple(CrestType.MONEY, "You wish to represent yourself by a moneybag? I think to make that meaningful I should increase the price to 500,000 coins. Do you agree?", "But I'll have to go and fetch the money."),
                    31 to Triple(CrestType.SARADOMIN, "Ah, the great god Saradomin! May he smile on your house as you adorn it with his symbol!", "You do not seem to be very devoted to any god. I will not let you have a divine symbol unless you have level 70 prayer."),
                    32 to Triple(CrestType.SKULL, "Of, of course you can have a skull symbol, " + (if (player.appearance.isMale) "sir!" else "madam!"), "A symbol of death? You do not seem like a killer to me; perhaps some other symbol would suit you better."),
                    33 to Triple(CrestType.VARROCK, "Ah, Varrock, a fine city!", ""),
                    34 to Triple(CrestType.ZAMORAK, "The god of Chaos? It is a terrible thing to worship that evil being. But if that is what you wish...", "You do not seem to be very devoted to any god. I will not let you have a divine symbol unless you have level 70 prayer.")
                )
                val (crest, successMessage, failMessage) = crestData[stage] ?: return true
                if (crest.eligible(player) && amountInInventory(player, Items.COINS_995) >= crest.cost && removeItem(player, Item(Items.COINS_995, crest.cost))) {
                    end()
                    player.houseManager.crest = crest
                    setAttribute(player, GameAttributes.FAMILY_CREST, stage)
                    npcl(FaceAnim.HALF_GUILTY, successMessage)
                    if (crest == CrestType.SARADOMIN && !player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!
                            .isComplete(2, 1)
                    ) {
                        player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.updateTask(player, 2, 1, true)
                    }
                } else {
                    end()
                    if (failMessage.isNotEmpty()) npcl(FaceAnim.HALF_GUILTY, failMessage)
                }
                stage = 15
                return true
            }
            35 -> npc(FaceAnim.ASKING, "Would you like a portrait or an, mmm, a landscape?", "Or a map, maybe?").also { stage++ }
            36 -> showTopics(
                Topic("a portrait", 37, true),
                Topic("a landscape", 39, true),
                Topic("a map", 41, true)
            )
            37 -> npcl(FaceAnim.HALF_GUILTY, "Mmm, well, there are a few portraits I can paint. I can only let you have one if you've got some connection with that person though. Who would you like?").also { stage++ }
            38 -> showTopics(
                Topic("King Arthur", 47, true),
                Topic("Elena of Ardougne", 48, true),
                Topic("King Alvis of Keldagrim", 49, true),
                Topic("The Prince and Princess of Miscellania", 50, true)
            )
            39 -> npcl(FaceAnim.HALF_GUILTY, "Mmm, well, I can paint a few places. Where have you had your adventures?").also { stage++ }
            40 -> showTopics(
                Topic("The River Lum", 54, true),
                Topic("The Kharid desert", 55, true),
                Topic("Morytania", 56, true),
                Topic("Karamja", 57, true),
                Topic("Isafdar", 58, true)
            )
            41 -> npcl(FaceAnim.HALF_GUILTY, "Mmm, yes, ah, I have painted maps of the known world on several different sizes of parchment. you like?").also { stage++ }
            42 -> showTopics(
                Topic("Small", 43, true),
                Topic("Medium", 44, true),
                Topic("Large", 45, true)
            )
            43,44,45 -> {
                val (requiredQP, mapSize, mapItemId) = maps[stage]!!

                if (getQuestPoints(player) < requiredQP) {
                    npcl(FaceAnim.HALF_GUILTY, "Mmm, I'm not sure you've had enough adventures in the world to deserve that map.")
                    sendMessage(player, "To buy a $mapSize map you must have $requiredQP Quest Points.")
                    stage = END_DIALOGUE
                } else {
                    npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.")
                    stage = 46
                }
            }
            46 -> {
                val cost = 1000
                val mapItemId = maps[stage]?.third ?: return true
                if (freeSlots(player) == 0) {
                    sendDialogue(player, "You don't have enough inventory space for that.")
                    return true
                }
                if (!removeItem(player, Item(Items.COINS_995, cost), Container.INVENTORY)) {
                    sendMessage(player, "You don't have enough money")
                    return true
                }
                npcl(FaceAnim.HALF_GUILTY, "There you go. Enjoy your map!")
                addItem(player, mapItemId, 1, Container.INVENTORY)
                stage = END_DIALOGUE
                return true
            }
            47,48,49,50 -> {
                val (quest, name, portraitItemId) = portraits[stage]!!

                val hasReq = when (quest) {
                    Quests.PLAGUE_CITY -> isQuestComplete(player, quest)
                    else -> hasRequirement(player, quest, false)
                }

                if (!hasReq) {
                    val messages = mapOf(
                        47 to "Do you have, mmm, a connection with King Arthur? He wouldn't like me to just give his picture to anyone.",
                        48 to "The last I heard, Elena was, mmm, trapped in West Ardougne. I wouldn't feel right selling her portrait while she was in danger.",
                        49 to "Have you ever been to Keldagrim? I think I'd need you to jog my memory...",
                        50 to "Do you have some connection with the prince and princess? I wouldn't want to give out their picture to just anyone?"
                    )
                    npcl(FaceAnim.HALF_GUILTY, messages[stage]!!)
                    sendMessage(player, "To buy the portrait of $name you must have completed $quest Quest.")
                    stage = END_DIALOGUE
                } else {
                    npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.")
                    stage = 51
                }
            }
            51 -> {
                val cost = 1000
                val portraitItemId = portraits[stage]?.third ?: return true
                if (freeSlots(player) == 0) {
                    end()
                    sendDialogue(player, "You don't have enough inventory space for that.")
                } else if (!removeItem(player, Item(Items.COINS_995, cost), Container.INVENTORY)) {
                    end()
                    sendMessage(player, "You don't have enough money")
                } else {
                    npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                    addItem(player, portraitItemId, 1, Container.INVENTORY)
                    stage = 52
                }
                return true
            }
            52 -> showTopics(
                Topic("a portrait", 38, true),
                Topic("a landscape", 40, true),
                Topic("a map", 42, true),
                Topic("No thanks", 53, true)
            )
            53 -> npcl(FaceAnim.HALF_GUILTY, "Mmm, well, see you some other time maybe.").also { stage = END_DIALOGUE }
            54,55,56,57,58 -> {
                val (requiredQuests, locationName, landscapeItemId) = landscapes[stage]!!
                val hasAll = requiredQuests.all { quest ->
                    when (quest) {
                        Quests.THE_FEUD, Quests.SHADES_OF_MORTTON, Quests.GHOSTS_AHOY, Quests.HAUNTED_MINE, Quests.TAI_BWO_WANNAI_TRIO, Quests.SHILO_VILLAGE -> hasRequirement(
                            player, quest, false
                        )

                        else -> isQuestComplete(player, quest)
                    }
                }
                if (!hasAll) {
                    npcl(FaceAnim.HALF_GUILTY, "Mmm, I'm not sure you've had enough adventures in $locationName to deserve a painting of it.")
                    val questNames = requiredQuests.joinToString(", ") { it.toString().replace("_", " ").capitalize() }
                    sendMessage(player, "To buy the painting of $locationName you must have completed $questNames.")
                    stage = END_DIALOGUE
                } else {
                    npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 2000 coins please.")
                    stage = 59
                }
            }
            59 -> {
                val landscapeItemId = landscapes[stage]?.third ?: return true
                val cost = 2000
                if (freeSlots(player) == 0) {
                    end()
                    sendDialogue(player, "You don't have enough inventory space for that.")
                } else if (!removeItem(player, Item(Items.COINS_995, cost), Container.INVENTORY)) {
                    end()
                    sendMessage(player, "You don't have enough money")
                } else {
                    npcl(FaceAnim.HALF_GUILTY, "There you go. Enjoy your painting!")
                    addItem(player, landscapeItemId, 1, Container.INVENTORY)
                    stage = END_DIALOGUE
                }
                return true
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_RENITEE_4249)

    companion object {
        val maps = mapOf(
            43 to Triple(51, "small", Items.SMALL_MAP_8004),
            44 to Triple(101, "medium", Items.MEDIUM_MAP_8005),
            45 to Triple(151, "large", Items.LARGE_MAP_8006)
        )
        val portraits = mapOf(
            47 to Triple(Quests.HOLY_GRAIL, "King Arthur", Items.ARTHUR_PORTRAIT_7995),
            48 to Triple(Quests.PLAGUE_CITY, "Elena", Items.ELENA_PORTRAIT_7996),
            49 to Triple(Quests.THE_GIANT_DWARF, "Giant Dwarf", Items.KELDAGRIM_PORTRAIT_7997),
            50 to Triple(Quests.THRONE_OF_MISCELLANIA, "Prince and Princess of Miscellania", Items.MISC_PORTRAIT_7998)
        )
        val landscapes = mapOf(
            54 to Triple(listOf(Quests.COOKS_ASSISTANT, Quests.RUNE_MYSTERIES, Quests.THE_RESTLESS_GHOST), "River Lum", Items.LUMBRIDGE_PAINTING_8002),
            55 to Triple(listOf(Quests.THE_TOURIST_TRAP, Quests.THE_FEUD, Quests.THE_GOLEM), "Kharid Desert", Items.DESERT_PAINTING_7999),
            56 to Triple(listOf(Quests.CREATURE_OF_FENKENSTRAIN, Quests.SHADES_OF_MORTTON, Quests.GHOSTS_AHOY, Quests.HAUNTED_MINE), "Morytania", Items.MORYTANIA_PAINTING_8003),
            57 to Triple(listOf(Quests.PIRATES_TREASURE, Quests.TAI_BWO_WANNAI_TRIO, Quests.SHILO_VILLAGE), "Karamja", Items.KARAMJA_PAINTING_8001),
            58 to Triple(listOf(Quests.ROVING_ELVES), "Isafdar", Items.ISAFDAR_PAINTING_8000)
        )
    }
}
