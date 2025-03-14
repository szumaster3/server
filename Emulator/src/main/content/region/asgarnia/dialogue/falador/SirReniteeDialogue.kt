package content.region.asgarnia.dialogue.falador

import content.data.GameAttributes
import content.global.skill.construction.Crests
import core.ServerConstants
import core.Util
import core.api.*
import core.api.quest.getQuestPoints
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SirReniteeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HALF_GUILTY,
            "Hmm? What's that, young " + (if (player.appearance.isMale) "man" else "woman") + "? What can I do for",
            "you?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("I don't know, what can you do for me?", "Nothing, thanks").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I don't know, what can you do for me?").also { stage = 100 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Nothing, thanks.").also { stage = 30 }
                }
            30 -> npc(FaceAnim.HALF_GUILTY, "Mmm, well, see you some other time maybe.").also { stage = END_DIALOGUE }
            100 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Hmm, well, mmm, do you have a family crest? I keep",
                    "track of every " + ServerConstants.SERVER_NAME + " family, you know, so I might",
                    "be able to find yours.",
                ).also {
                    stage =
                        110
                }
            110 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm also something of an, mmm, a painter. If you've",
                    "met any important persons or visited any nice places I",
                    "could paint them for you.",
                ).also {
                    stage =
                        120
                }
            120 -> options("Can you see if I have a family crest?", "Can I buy a painting?").also { stage = 130 }
            130 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Can you see if I have a family crest?").also { stage = 200 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Can I buy a painting?").also { stage = 500 }
                }
            200 -> npc(FaceAnim.HALF_GUILTY, "What is your name?").also { stage = 210 }
            210 -> player(FaceAnim.HALF_GUILTY, player.username + ".").also { stage = 220 }
            220 -> npc(FaceAnim.HALF_GUILTY, "Mmm, " + player.username + ", let me see...").also { stage = 230 }
            230 ->
                if (getStatLevel(player, Skills.CONSTRUCTION) >= 16) {
                    if (getAttribute(player, "sir-renitee-assigned-crest", false)) {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "According to my records, your crest is the symbol of ",
                            "${
                                player.houseManager.crest.name.lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                            }.",
                        )
                    } else {
                        val c = RandomFunction.random(4)
                        val crests =
                            arrayOf(Crests.VARROCK, Crests.ASGARNIA, Crests.KANDARIN, Crests.MISTHALIN)
                        val crest = crests[c]
                        player.houseManager.crest = crest
                        setAttribute(player, "/save:sir-renitee-assigned-crest", true)
                        when (crest) {
                            Crests.ASGARNIA -> setAttribute(player, GameAttributes.FAMILY_CREST, 2)
                            Crests.KANDARIN -> setAttribute(player, GameAttributes.FAMILY_CREST, 10)
                            Crests.MISTHALIN -> setAttribute(player, GameAttributes.FAMILY_CREST, 11)
                            else -> setAttribute(player, GameAttributes.FAMILY_CREST, 15)
                        }
                        var message = "that can be your"
                        if (crest == Crests.VARROCK) {
                            message = "you can use that city's"
                        }
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Well, I don't think you have any noble blood,",
                            "but I see that your ancestors came from " +
                                Util.enumToString(player.houseManager.crest.name) +
                                ",",
                            " so $message crest.",
                        )
                    }
                    if (!player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.isComplete(0, 4)) {
                        player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.updateTask(player, 0, 4, true)
                    }
                    stage = 240
                } else {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "First thing's first, young " + (if (player.appearance.isMale) "man" else "woman") +
                            "! There is not much point",
                        "in having a family crest if you cannot display it.",
                    ).also {
                        stage =
                            235
                    }
                }
            235 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You should train construction until you can build a wall",
                    "decoration in your dining room.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            240 -> options("I don't like that crest. Can I have a different one?", "Thanks!").also { stage = 250 }
            250 ->
                when (buttonId) {
                    1 ->
                        player(FaceAnim.HALF_GUILTY, "I don't like that crest. Can I have a different one?").also {
                            stage = 300
                        }
                    2 -> player(FaceAnim.HALF_GUILTY, "Thanks!").also { stage = 260 }
                }
            260 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You're welcome, my " + (if (player.appearance.isMale) "boy." else "girl."),
                ).also {
                    stage =
                        END_DIALOGUE
                }
            300 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Mmm, very well. Changing your crest will cost",
                    "5,000 coins.",
                ).also { stage++ }
            301 ->
                if (amountInInventory(player, Items.COINS_995) < 5000) {
                    player(
                        FaceAnim.HALF_GUILTY,
                        "I'll have to go and get some money then.",
                    ).also { stage = END_DIALOGUE }
                } else {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "There are sixteen different symbols; which one would",
                        "you like?",
                    ).also {
                        stage =
                            310
                    }
                }
            310 -> options("Shield of Arrav", "Asgarnia", "Dorgeshuun Symbol", "Dragon", "More...").also { stage = 320 }
            320 ->
                when (buttonId) {
                    1 -> {
                        val c = Crests.ARRAV
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 1)
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Ah yes, the shield that you helped to retrieve. You have certainly earned the right to wear its symbol.",
                            )
                        } else {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "But that legendary shield is still lost! I don't think it would be proper for you to wear its symbol.",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }
                    2 -> {
                        val c = Crests.ASGARNIA
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 2)
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Ah, splendid, splendid. There is no better symbol than that of our fair land!",
                            )
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                        }
                    }
                    3 -> {
                        val c = Crests.DORGESHUUN
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 3)
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Ah yes, our new neighbours under Lumbridge. I hear you were the one who made contact with them, jolly good.",
                            )
                        } else {
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "Hmm, have you ever even met the Dorgeshuun? I don't",
                                "think you should wear their symbol until you have",
                                "made contact with that lost tribe.",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }
                    4 -> {
                        val c = Crests.DRAGON
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 4)
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "I see you are a mighty dragon-slayer! You have certainly earned the right to wear a dragon symbol.",
                            )
                        } else {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "When the dragon on Crandor Isle remains undefeated? I think you should prove yourself a dragon-slayer before you can wear a dragon symbol!",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }
                    5 -> options("Fairy", "Guthix", "HAM", "Horse", "More...").also { stage = 330 }
                }

            330 ->
                when (buttonId) {
                    1 -> {
                        val c = Crests.FAIRY
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(
                                player,
                                Item(Items.COINS_995, c.cost),
                            )
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 5)
                            npcl(FaceAnim.HALF_GUILTY, "Hmm, mmm, yes, everyone likes pretty fairies.")
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                        }
                    }
                    2 -> {
                        val c = Crests.GUTHIX
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 6)
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Guthix, god of balance! I'm a Saradominist myself, you know, but we all find meaning in our own way, what?",
                            )
                        } else {
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "You do not seem to be very devoted to any god.",
                                "I will not let you have a divine symbol",
                                "unless you have level 70 prayer.",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }

                    3 -> {
                        val c = Crests.HAM
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 7)
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "Hmm, I'm not sure I like that HAM group, their beliefs are",
                                "a little extreme for me. But if that's what you want.",
                            )
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                        }
                    }

                    4 -> {
                        val c = Crests.HORSE
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 8)
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Ah, I see you've brought a toy horse for me to see. An interesting beast. Certainly you can use that as your crest if you like, although it seems a bit strange to me.",
                            )
                        } else {
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "A horse? I know people talk about them, but I'm not at all sure",
                                "they ever existed. I don't think I could let you use as",
                                "your symbol unless you can fetch me some kind of model of one.",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }
                    5 -> options("Jogre", "Kandarin", "Misthalin", "Money", "More...").also { stage = 340 }
                }

            340 ->
                when (buttonId) {
                    1 -> {
                        val c = Crests.JOGRE
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 9)
                            npcl(FaceAnim.HALF_GUILTY, "A Jungle Ogre, eh? Odd beast, very odd.")
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                        }
                    }

                    2 -> {
                        val c = Crests.KANDARIN
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 10)
                            npcl(FaceAnim.HALF_GUILTY, "Our neighbours in the west? Very good, very good.")
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                        }
                    }

                    3 -> {
                        val c = Crests.MISTHALIN
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 11)
                            npcl(FaceAnim.HALF_GUILTY, "Ah, the fair land of Lumbridge and Varrock.")
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                        }
                    }
                    4 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "You wish to represent yourself by a moneybag?",
                            "I think to make that meaningful I should increase",
                            "the price to 500,000 coins. Do you agree?",
                        ).also {
                            stage =
                                341
                        }
                    5 -> options("Saradomin", "Skull", "Varrock", "Zamorak", "More...").also { stage = 350 }
                }

            341 -> options("All right.", "No way!").also { stage++ }
            342 ->
                when (buttonId) {
                    1 -> {
                        val c = Crests.MONEY
                        if (c.eligible(player) && amountInInventory(player, Items.COINS_995) < c.cost) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            player(FaceAnim.HALF_GUILTY, "All right.").also { stage = 343 }
                        }
                    }
                    2 -> player(FaceAnim.HALF_GUILTY, "No way!").also { stage = 344 }
                }

            343 -> {
                val c = Crests.MONEY
                if (c.eligible(player) &&
                    amountInInventory(player, Items.COINS_995) > c.cost &&
                    removeItem(player, Item(Items.COINS_995, c.cost))
                ) {
                    end()
                    player.houseManager.crest = c
                    removeAttribute(player, GameAttributes.FAMILY_CREST)
                    setAttribute(player, GameAttributes.FAMILY_CREST, 12)
                    npc(FaceAnim.HALF_GUILTY, "Thank you very much! You may now use a money-bag", "as your symbol.")
                } else {
                    npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                }
            }

            344 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well we can't have just any pauper using a money-bag",
                    "as a symbol, can we? You'll have to choose a",
                    "different symbol.",
                ).also {
                    stage =
                        310
                }
            350 ->
                when (buttonId) {
                    1 -> {
                        val c = Crests.SARADOMIN
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 13)
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Ah, the great god Saradomin! May he smile on your house as you adorn it with his symbol!",
                            )
                            if (!player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.isComplete(2, 1)) {
                                player.achievementDiaryManager
                                    .getDiary(
                                        DiaryType.FALADOR,
                                    )!!
                                    .updateTask(player, 2, 1, true)
                            }
                        } else {
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "You do not seem to be very devoted to any god. I will",
                                "not let you have a divine symbol unless you have level",
                                "70 prayer.",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }

                    2 -> {
                        val c = Crests.SKULL
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 14)
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "Of, of course you can have a skull symbol, " +
                                    (if (player.appearance.isMale) "sir!" else "madam!"),
                            )
                        } else {
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "A symbol of death? You do not seem like a killer to me;",
                                "perhaps some other symbol would suit you better.",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }

                    3 -> {
                        val c = Crests.VARROCK
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 15)
                            npc(FaceAnim.HALF_GUILTY, "Ah, Varrock, a fine city!")
                        } else {
                            npc(FaceAnim.HALF_GUILTY, "[MISSING DIALOGUE - NOT ELIGIBLE]").also { stage = 310 }
                        }
                    }

                    4 -> {
                        val c = Crests.ZAMORAK
                        if (c.eligible(player) &&
                            amountInInventory(player, Items.COINS_995) > c.cost &&
                            removeItem(player, Item(Items.COINS_995, c.cost))
                        ) {
                            end()
                            player.houseManager.crest = c
                            removeAttribute(player, GameAttributes.FAMILY_CREST)
                            setAttribute(player, GameAttributes.FAMILY_CREST, 16)
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "The god of Chaos? It is a terrible thing to worship",
                                "that evil being. But if that is what you wish...",
                            )
                        } else {
                            npc(
                                FaceAnim.HALF_GUILTY,
                                "You do not seem to be very devoted to any god.",
                                "I will not let you have a divine symbol",
                                "unless you have level 70 prayer.",
                            ).also {
                                stage =
                                    310
                            }
                        }
                    }
                    5 ->
                        options(
                            "Shield of Arrav",
                            "Asgarnia",
                            "Dorgeshuun Symbol",
                            "Dragon",
                            "More...",
                        ).also { stage = 320 }
                }
            500 ->
                npc(
                    FaceAnim.ASKING,
                    "Would you like a portrait or an, mmm, a landscape?",
                    "Or a map, maybe?",
                ).also { stage++ }
            501 -> options("a portrait", "a landscape", "a map").also { stage++ }
            502 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mmm, well, there are a few portraits I can paint. I can only let you have one if you've got some connection with that person though. Who would you like?",
                        ).also {
                            stage++
                        }
                    2 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mmm, well, I can paint a few places. Where have you had your adventures?",
                        ).also {
                            stage =
                                514
                        }
                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mmm, yes, ah, I have painted maps of the known world on several different sizes of parchment. you like?",
                        ).also {
                            stage =
                                526
                        }
                }
            503 ->
                options(
                    "King Arthur",
                    "Elena of Ardougne",
                    "King Alvis of Keldagrim",
                    "The Prince and Princess of Miscellania",
                ).also {
                    stage++
                }
            504 ->
                when (buttonId) {
                    1 -> {
                        if (!hasRequirement(player, Quests.HOLY_GRAIL, false)) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Do you have, mmm, a connection with King Arthur? He wouldn't like me to just give his picture to anyone.",
                            )
                            sendMessage(
                                player,
                                "To buy the portrait of King Arthur you must have completed The Holy Grail.",
                            )
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.").also { stage = 505 }
                        }
                    }

                    2 -> {
                        if (!isQuestComplete(player, Quests.PLAGUE_CITY)) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "The last I heard, Elena was, mmm, trapped in West Ardougne. I wouldn't feel right selling her portrait while she was in danger.",
                            )
                            sendMessage(
                                player,
                                "To buy the portrait of Elena you must have completed the ${Quests.PLAGUE_CITY} quest.",
                            )
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.").also { stage = 508 }
                        }
                    }

                    3 ->
                        if (!hasRequirement(player, Quests.THE_GIANT_DWARF, false)) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Have you ever been to Keldagrim? I think I'd need you to jog my memory...",
                            )
                            sendMessage(
                                player,
                                "To buy the portrait of the Giant Dwarf you must have completed The Giant Dwarf.",
                            )
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.").also { stage = 510 }
                        }

                    4 ->
                        if (!hasRequirement(player, Quests.THRONE_OF_MISCELLANIA, false)) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Do you have some connection with the prince and princess? I wouldn't want to give out their picture to just anyone?",
                            )
                            sendMessage(
                                player,
                                "To buy the portrait of the Prince and Princess of Miscellania you must have completed The Throne of Miscellania.",
                            )
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.").also { stage = 512 }
                        }
                }

            505 -> options("All right", "No thanks").also { stage++ }
            506 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.ARTHUR_PORTRAIT_7995, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            508 -> options("All right", "No thanks").also { stage++ }
            509 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.ELENA_PORTRAIT_7996, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            510 -> options("All right", "No thanks").also { stage++ }
            511 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.KELDAGRIM_PORTRAIT_7997, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            512 -> options("All right", "No thanks").also { stage++ }
            513 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.MISC_PORTRAIT_7998, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            514 -> options("The River Lum", "The Kharid desert", "Morytania", "Karamja", "Isafdar").also { stage++ }
            515 ->
                when (buttonId) {
                    1 -> {
                        if (!isQuestComplete(player, Quests.COOKS_ASSISTANT) ||
                            !isQuestComplete(player, Quests.RUNE_MYSTERIES) ||
                            !isQuestComplete(player, Quests.THE_RESTLESS_GHOST)
                        ) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in Lumbridge to deserve a painting of it.",
                            )
                            sendMessage(
                                player,
                                "To buy the painting of the Lum you must have completed Cook's Assistant, Rune Mysteries, and The Restless Ghost.",
                            )
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 2000 coins please.").also { stage = 516 }
                        }
                    }

                    2 -> {
                        if (!isQuestComplete(player, Quests.THE_TOURIST_TRAP) ||
                            !hasRequirement(player, Quests.THE_FEUD, false) ||
                            !isQuestComplete(player, Quests.THE_GOLEM)
                        ) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in the desert to deserve a painting of it.",
                            )
                            sendMessage(
                                player,
                                "To buy the painting of the desert you must have completed Tourist Trap, The Feud, and The Golem.",
                            )
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 2000 coins please.").also { stage = 518 }
                        }
                    }

                    3 -> {
                        if (!isQuestComplete(player, Quests.CREATURE_OF_FENKENSTRAIN) ||
                            !hasRequirement(player, Quests.SHADES_OF_MORTTON, false) ||
                            !isQuestComplete(player, Quests.GHOSTS_AHOY) ||
                            !hasRequirement(player, Quests.HAUNTED_MINE, false)
                        ) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in Morytania to deserve a painting of it.",
                            )
                            sendMessage(
                                player,
                                "To buy the painting of Morytania you must have completed Shades of Mort'ton, The Creature of Fenkenstrain",
                            )
                            sendMessage(player, "Ghosts Ahoy, and The Haunted Mine.")
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 2000 coins please.").also { stage = 520 }
                        }
                    }

                    4 -> {
                        if (!isQuestComplete(player, Quests.PIRATES_TREASURE) ||
                            !hasRequirement(player, Quests.TAI_BWO_WANNAI_TRIO, false) ||
                            !hasRequirement(player, Quests.SHILO_VILLAGE, false)
                        ) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in Karamja to deserve a painting of it.",
                            )
                            sendMessage(
                                player,
                                "To buy the painting of Karamja you must have completed Pirate's Treasure, Tai Bwo Wannai Trio, and Shilo Village.",
                            )
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 2000 coins please.").also { stage = 522 }
                        }
                    }

                    5 -> {
                        if (!isQuestComplete(player, Quests.ROVING_ELVES)) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in Isafdar to deserve a painting of it.",
                            )
                            sendMessage(player, "To buy the painting of Isafdar you must have completed Roving Elves.")
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 2000 coins please.").also { stage = 524 }
                        }
                    }
                }

            516 -> options("All right", "No thanks").also { stage++ }
            517 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 2000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.LUMBRIDGE_PAINTING_8002, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            518 -> options("All right", "No thanks").also { stage++ }
            519 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 2000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.DESERT_PAINTING_7999, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            520 -> options("All right", "No thanks").also { stage++ }
            521 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 2000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.MORYTANIA_PAINTING_8003, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            522 -> options("All right", "No thanks").also { stage++ }
            523 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 2000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.KARAMJA_PAINTING_8001, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            524 -> options("All right", "No thanks").also { stage++ }
            525 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 2000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.ISAFDAR_PAINTING_8000, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            526 -> options("Small", "Medium", "Large").also { stage++ }
            527 ->
                when (buttonId) {
                    1 -> {
                        if (getQuestPoints(player) < 51) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in the world to deserve that map.",
                            )
                            sendMessage(player, "To buy a small map you must have 51 Quest Points.")
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.").also { stage = 528 }
                        }
                    }

                    2 -> {
                        if (getQuestPoints(player) < 101) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in the world to deserve that map.",
                            )
                            sendMessage(player, "To buy a medium map you must have 101 Quest Points.")
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.").also { stage = 530 }
                        }
                    }

                    3 -> {
                        if (getQuestPoints(player) < 151) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Mmm, I'm not sure you've had enough adventures in the world to deserve that map.",
                            )
                            sendMessage(player, "To buy a large map you must have 151 Quest Points.")
                            stage = 700
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "That will be, mmm, 1000 coins please.").also { stage = 520 }
                        }
                    }
                }

            528 -> options("All right", "No thanks").also { stage++ }
            529 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.SMALL_MAP_8004, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            530 -> options("All right", "No thanks").also { stage++ }
            531 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.MEDIUM_MAP_8005, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            532 -> options("All right", "No thanks").also { stage++ }
            533 ->
                when (buttonId) {
                    1 ->
                        if (freeSlots(player) == 0) {
                            end()
                            sendDialogue(player, "You don't have enough inventory space for that.")
                        } else if (!removeItem(player, Item(Items.COINS_995, 1000), Container.INVENTORY)) {
                            end()
                            sendMessage(player, "You don't have enough money")
                        } else {
                            npcl(FaceAnim.HALF_GUILTY, "There you go. Would you like another painting?")
                            addItem(player, Items.LARGE_MAP_8006, 1, Container.INVENTORY)
                            stage = 600
                        }

                    2 -> end()
                }

            600 -> options("a portrait", "a landscape", "a map", "No thanks").also { stage++ }
            601 ->
                when (buttonId) {
                    1 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mmm, well, there are a few portraits I can paint. I can only let you have one if you've got some connection with that person though. Who would you like?",
                        ).also {
                            stage =
                                503
                        }
                    2 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mmm, well, I can paint a few places. Where have you had your adventures?",
                        ).also {
                            stage =
                                514
                        }
                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mmm, yes, ah, I have painted maps of the known world on several different sizes of parchment. you like?",
                        ).also {
                            stage =
                                526
                        }
                    4 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mmm, well, see you some other time maybe.",
                        ).also { stage = END_DIALOGUE }
                }
            700 -> npcl(FaceAnim.HALF_GUILTY, "Would you like a different painting?").also { stage = 600 }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_RENITEE_4249)
    }
}
