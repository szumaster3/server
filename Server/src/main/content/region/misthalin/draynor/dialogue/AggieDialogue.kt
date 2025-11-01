package content.region.misthalin.draynor.dialogue

import content.data.GameAttributes
import content.region.misthalin.draynor.quest.swept.plugin.SweptUtils.resetLines
import core.api.*
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.*

/**
 * Represents the Aggie dialogue.
 */
@Initializable
class AggieDialogue(player: Player? = null) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (args.size >= 2) {
            options(
                "What do you need to make a red dye?",
                "What do you need to make yellow dye?",
                "What do you need to make blue dye?",
            )
            stage = 42
            return true
        }
        if(npc.id == NPCs.AGGIE_8207) {
            if (getQuestStage(player, Quests.SWEPT_AWAY) == 3) {
                player("Woah, I felt that down to my toes!")
                stage = 911
            } else if(getQuestStage(player, Quests.SWEPT_AWAY) == 2 && player.getAttribute("total_sweeps", 0) >= 0) {
                npcl(FaceAnim.SAD, "Hmm, that doesn't look quite right. To enchant the broom, you need to sweep away 4 lines to make a pattern of 4 small triangles - and no extra lines or shapes.").also { stage = 921 }
            } else {
                player("Woah! Where are we and what are we doing here?")
                stage = 900
            }
            return true
        }
        quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        npc("What can I help you with?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (quest!!.getStage(player) == 20 ||
                    quest!!.getStage(player) == 30 ||
                    quest!!.getStage(player) == 40 ||
                    quest!!.getStage(
                        player,
                    ) == 50 ||
                    quest!!.getStage(player) == 60
                ) {
                    options(
                        "Could you think of a way to make skin paste?",
                        "What could you make for me?",
                        "Cool, do you turn people into frogs?",
                        "You mad old witch, you can't help me.",
                        "Can you make dyes for me please?",
                    )
                    stage = 720
                    return true
                }
                if (!isQuestComplete(player, Quests.SWEPT_AWAY) && getQuestStage(player, Quests.SWEPT_AWAY) >= 1) {
                    options(
                        "What could you make for me?",
                        "Cool, do you turn people into frogs?",
                        "Talk about Swept away.",
                        "You mad old witch, you can't help me.",
                        "Can you make dyes for me please?",
                    )
                    stage = 1
                } else {
                    options(
                        "What could you make for me?",
                        "Cool, do you turn people into frogs?",
                        "You mad old witch, you can't help me.",
                        "Can you make dyes for me please?",
                    )
                    stage = 1
                }
            }

            720 ->
                when (buttonId) {
                    1 -> {
                        player("Could you think of a way to make skin paste?")
                        stage = 721
                    }

                    2 -> {
                        player("What could you make for me?")
                        stage = 10
                    }

                    3 -> {
                        player("Cool, do you turn people into frogs?")
                        stage = 20
                    }

                    4 -> {
                        player(FaceAnim.FURIOUS, "You mad old witch, you can't help me.")
                        stage = 30
                    }

                    5 -> {
                        player(FaceAnim.FURIOUS, "Can you make dyes for me please?")
                        stage = 40
                    }
                }

            721 ->
                stage =
                    if (!hasIngredients(player)) {
                        npc(
                            "Why it's one of my most popular potions. The women",
                            "here, they like to have smooth looking skin. And I must",
                            "admit, some of the men buy it as well.",
                        )
                        722
                    } else {
                        npc(
                            "Yes I can, I see you already have the ingredients.",
                            "Would you like me to mix some for you now?",
                        )
                        726
                    }

            722 -> {
                npc("I can make it for you, just get me what's needed.")
                stage = 723
            }

            723 -> {
                player("What do you need to make it?")
                stage = 724
            }

            724 -> {
                npc(
                    "Well dearie, you need a base for the paste. That's a",
                    "mix of ash, flour and water. Then you need redberries",
                    "to colour it as you want. Bring me those four items",
                    "and I will make you some.",
                )
                stage = 725
            }

            725 -> end()
            726 -> {
                options("Yes please. Mix me some skin paste.", "No thank you, I don't need any skin paste right now.")
                stage = 727
            }

            727 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Yes please. Mix me some skin paste.")
                        stage = 731
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "No thank you, I don't need any skin paste right now.")
                        stage = 729
                    }
                }

            729 -> {
                npc("Okay dearie, that's always your choice.")
                stage = 730
            }

            730 -> end()
            731 -> {
                npc("That should be simple. Hand the things to Aggie then.")
                stage = 732
            }

            732 ->
                if (player.inventory.remove(*PASTE_SOLID_INGREDIENTS) &&
                    (player.inventory.remove(BUCKET_OF_WATER) || player.inventory.remove(JUG_OF_WATER))
                ) {
                    interpreter.sendDoubleItemMessage(
                        REDBERRIES,
                        POT_OF_FLOUR,
                        "You hand the ash, flour, water and redberries to Aggie. Aggie tips the ingredients into a cauldron and mutters some words.",
                    )
                    stage = 733
                }

            733 -> {
                npc("Tourniquet, Fenderbaum, Tottenham, Marshmallow, Marblearch.")
                stage = 734
            }

            734 ->
                if (player.inventory.add(PASTE)) {
                    interpreter.sendItemMessage(PASTE, "Aggie hands you the skin paste.")
                    stage = 735
                }

            735 -> {
                npc("There you go dearie, your skin potion. That will make", "you look good at the Varrock dances.")
                stage = 736
            }

            736 -> end()
            1 ->
                if (!isQuestComplete(player, Quests.SWEPT_AWAY)) {
                    when (buttonId) {
                        1 -> {
                            player("What could you make for me?")
                            stage = 10
                        }

                        2 -> {
                            player("Cool, do you turn people into frogs?")
                            stage = 20
                        }

                        3 -> {
                            player(FaceAnim.HAPPY, "Could we talk about brooms?")
                            stage = 800
                        }

                        4 -> {
                            player(FaceAnim.FURIOUS, "You mad old witch, you can't help me.")
                            stage = 30
                        }

                        5 -> {
                            player(FaceAnim.FURIOUS, "Can you make dyes for me please?")
                            stage = 40
                        }
                    }
                } else {
                    when (buttonId) {
                        1 -> {
                            player("What could you make for me?")
                            stage = 10
                        }

                        2 -> {
                            player("Cool, do you turn people into frogs?")
                            stage = 20
                        }

                        3 -> {
                            player(FaceAnim.FURIOUS, "You mad old witch, you can't help me.")
                            stage = 30
                        }

                        4 -> {
                            player(FaceAnim.FURIOUS, "Can you make dyes for me please?")
                            stage = 40
                        }
                    }
                }

            40 -> {
                npc(
                    FaceAnim.FURIOUS,
                    "What sort of dye would you like? Red, yellow or blue?",
                )
                stage = 41
            }

            41 -> {
                options(
                    "What do you need to make a red dye?",
                    "What do you need to make yellow dye?",
                    "What do you need to make blue dye?",
                )
                stage = 42
            }

            42 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.FURIOUS, "What do you need to make red dye?")
                        stage = 410
                    }

                    2 -> {
                        player(FaceAnim.FURIOUS, "What do you need to make yellow dye?")
                        stage = 420
                    }

                    3 -> {
                        player(FaceAnim.FURIOUS, "What do you need to make blue dye?")
                        stage = 430
                    }
                }

            430 -> {
                npc("2 woad leaves and 5 coins to you.")
                stage = 431
            }

            431 -> {
                player(FaceAnim.FURIOUS, "Okay, make me some blue dye please.")
                stage = 432
            }

            432 -> {
                if (player.inventory.containsItem(COINS) && player.inventory.containsItem(WOAD_LEAVES)) {
                    player.inventory.remove(COINS)
                    player.inventory.remove(WOAD_LEAVES)
                    player.inventory.add(BLUE_DYE)
                    make(BLUE_DYE.id)
                    sendItemDialogue(
                        player,
                        BLUE_DYE,
                        "You hand the woad leaves and payment to Aggie. Aggie produces a blue bottle and hands it to you.",
                    )
                } else {
                    interpreter.sendDialogue("You need 2 woad leaves and 5 coins.")
                }
                stage = 413
            }

            433 -> end()
            420 -> {
                npc(
                    "Yellow is a strange colour to get, comes from onion",
                    "skins. I need 2 onions and 5 coins to make yellow dye.",
                )
                stage = 421
            }

            421 -> {
                player(FaceAnim.FURIOUS, "Okay, make me some yellow dye please.")
                stage = 422
            }

            422 -> {
                if (player.inventory.containsItem(COINS) && player.inventory.containsItem(ONIONS)) {
                    player.inventory.remove(COINS)
                    player.inventory.remove(ONIONS)
                    player.inventory.add(YELLOW_DYE)
                    make(YELLOW_DYE.id)
                    sendItemDialogue(
                        player,
                        YELLOW_DYE,
                        "You hand the onions and payment to Aggie. Aggie produces a yellow bottle and hands it to you.",
                    )
                } else {
                    interpreter.sendDialogue("You need 2 onions and 5 coins.")
                }
                stage = 423
            }

            423 -> end()
            410 -> {
                npc("3 lots of redberries and 5 coins to you.")
                stage = 411
            }

            411 -> {
                player(FaceAnim.FURIOUS, "Okay, make me some red dye please.")
                stage = 412
            }

            412 -> {
                if (player.inventory.containsItem(COINS) && player.inventory.containsItem(REDBERRIES)) {
                    player.inventory.remove(COINS)
                    player.inventory.remove(REDBERRIES)
                    player.inventory.add(RED_DYE)
                    make(RED_DYE.id)
                    sendItemDialogue(
                        player,
                        RED_DYE,
                        "You hand the berries and payment to Aggie. Aggie produces a red bottle and hands it to you.",
                    )
                } else {
                    interpreter.sendDialogue("You need 3 redberries leaves and 5 coins.")
                }
                stage = 413
            }

            413 -> end()
            30 -> {
                npc("Oh, you like to call a witch names do you?")
                stage = 31
            }

            31 -> {
                val item = Item(Items.COINS_995, 20)
                stage =
                    if (player.inventory.remove(item)) {
                        sendItemDialogue(
                            player,
                            item,
                            "Aggie waves her hands about, and you seem to be 20 coins poorer.",
                        )
                        32
                    } else {
                        npc(
                            "You should be careful about insulting a witch. You",
                            "never know what shape you could wake up in.",
                        )
                        34
                    }
            }

            32 -> {
                npc(
                    "That's a fine for insulting a witch. You should learn",
                    "some respect.",
                )
                stage = 33
            }

            34 -> end()
            33 -> end()
            20 -> {
                npc(
                    "Oh, not for years, but if you meet a talking chicken,",
                    "you have probably met the professor in the manor north",
                    "of here. A few years ago it was flying fish. That",
                    "machine is a menace.",
                )
                stage = 11
            }

            10 -> {
                npc(
                    "I mostly make what I find pretty. I sometimes",
                    "make dye for the women's clothes to brighten the place",
                    "up. I can make red, yellow and blue dyes. If you'd like",
                    "some, just bring me the appropriate ingredients.",
                )
                stage = 11
            }

            11 -> end()

            800 -> {
                npc("Of course. What can I do for you?")
                stage++
            }

            801 -> {
                player(
                    "Maggie has asked me to help her enchant her broom.",
                    "She needs it to fishing a potion that she's brewing.",
                )
                stage++
            }

            802 -> {
                player("I was wondering if you could help me out.")
                stage++
            }

            803 -> {
                npc("Of course; Maggie's an old friend and we go back quite", "a way.")
                stage++
            }

            804 -> {
                npc("Now, in order to enchant the broom, we'll need a bit of", "space and privacy.")
                stage++
            }

            805 -> {
                npc(
                    "There's a little clearing us witches sometimes use.",
                    "Would you like me to teleport you there so that we can",
                    "get started?",
                )
                stage++
            }

            806 -> {
                options("Yes, I'm ready to go now.", "No. I'd like to wait a bit.")
                stage++
            }

            807 -> {
                when (buttonId) {
                    1 -> player("Yes, I'm ready to go now.").also { stage = 809 }
                    2 -> player("No. I'd like to wait a bit.").also { stage++ }
                }
            }
            808 -> end()
            809 -> npc("Okay, hold on to your hat!").also { stage++ }
            810 -> {
                end()
                lock(player, 6)
                GameWorld.Pulser.submit(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> openInterface(player, Components.FADE_TO_BLACK_120)
                                3 ->
                                    teleport(
                                        player,
                                        Location(3291, 4514, 0),
                                        TeleportManager.TeleportType.INSTANT,
                                    )
                                6 -> {
                                    openInterface(player, Components.FADE_FROM_BLACK_170)
                                    if (!player.musicPlayer.hasUnlocked(Music.MAGIC_AND_MYSTERY_572)) {
                                        player.musicPlayer.unlock(Music.MAGIC_AND_MYSTERY_572)
                                    }
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            900 -> npc("Oh, this is just a little place that some of us witches use", "on occasion. It's rather convenient for the occasional", "ritual or spell.").also { stage++ }
            901 -> npc("Not only is it infused with a bit of magical peace, but", "it's out of the way enough that we don't get a lot of", "unnecessary interruptions.").also { stage++ }
            902 -> player("Ah, right. Which leaves the question of what we're doing", "here.").also { stage++ }
            903 -> npc("You want that broom of yours enchanted, right?").also { stage++ }
            904 -> player("Right.").also { stage++ }
            905 -> npc("Well the best way to enchant that hunk of dead wood", "is to harness the power latent in this magical symbol", "here.").also { stage++ }
            906 -> npc("Do you see that pattern of 16 lines thrown out of sand", "on the ground?").also { stage++ }
            907 -> player("How could I miss it?").also { stage++ }
            908 -> npc("In order to enchant the broom, you need to sweep", "away 4 lines of those 16 lines, such that you leave only 4", "small triangles on the ground - and nothing else.").also { stage++ }
            909 -> npc("If you run into any trouble, let me know and I'll", "reconfigure the original pattern for you. I can also", "teleport you back to Draynor when you're ready to", "leave.").also { stage++ }
            910 -> {
                player("Okay, thanks. I'll give it a try.")
                end()
                setQuestStage(player, Quests.SWEPT_AWAY, 2)
                stage = END_DIALOGUE
            }
            911 -> npcl(FaceAnim.HAPPY, "You did it! The enchantment form the sand pattern has infused Maggie's broom. There's nothing more to do here howl come have a chat when you're ready and I'll take you back to Draynor with me.").also { stage++ }
            912 -> player("Wow that was impressive.").also { stage++ }
            913 -> npc("Yes, there is a lot of power in these types of magical", "symbols.").also { stage++ }
            914 -> options("Is there anything else that needs to be done here?", "Where are we?", "I'd like to go back to Draynor, please.").also { stage++ }
            915 -> when (buttonId) {
                1 -> player("Is there anything else that needs to be done here?").also { stage = 918 }
                2 -> player("Where are we?").also { stage = 919 }
                3 -> player("I'd like to go back to Draynor, please.").also { stage++ }

            }
            916 -> npc("Sure thing! Just hold on to your hat and you'll be back", "in Draynor before you can wiggle your nose.").also { stage++ }
            917 -> {
                end()
                lock(player, 6)
                GameWorld.Pulser.submit(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> openInterface(player, Components.FADE_TO_BLACK_120)
                                3 ->
                                    core.api.teleport(
                                        player,
                                        Location.create(3086, 3259, 0),
                                        TeleportManager.TeleportType.INSTANT,
                                    )

                                6 -> {
                                    unlock(player)
                                    openInterface(player, Components.FADE_FROM_BLACK_170)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            918 -> npc(FaceAnim.HALF_GUILTY, "Not everything you were supposed to do has been done.").also { stage = 914 }
            919 -> npc("Oh, this is just a little place that some of us witches use", "on occasion. It's rather convenient for the occasional", "ritual or spell.").also { stage++ }
            920 -> npc("Not only is it infused with a bit of magical peace, but", "it's out of the way enough that we don't get a lot of", "unnecessary interruptions.").also { stage = 914 }

            921 -> npcl(FaceAnim.HAPPY, "Would you like me to lay out the sand lines so that you can try again?").also { stage++ }
            922 -> options("Yes, please.", "No, thank you.").also { stage++ }
            923 -> when (buttonId) {
                1 -> {
                    npc("Okay, just one moment.")
                    lock(player, 9)
                    openInterface(player, Components.FADE_TO_BLACK_120)
                    runTask(player, 6) {
                        closeInterface(player)
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        resetLines(player)
                    }
                    stage = END_DIALOGUE
                }

                2 -> {
                    player("No, thank you.")
                    stage++
                }
            }
            924 -> npcl(FaceAnim.HAPPY, "All right. If you change your mind, just let me know.").also { stage = END_DIALOGUE }
        }
        return true
    }

    fun make(dye: Int) {
        npc.walkingQueue.reset()
        npc.faceLocation(CAULDRON_LOCATION)
        npc.animate(ANIMATION)
    }

    private fun hasIngredients(player: Player): Boolean {
        for (i in PASTE_SOLID_INGREDIENTS) {
            if (!player.inventory.containsItem(i)) {
                return false
            }
        }

        return player.inventory.containsItem(BUCKET_OF_WATER) || player.inventory.containsItem(JUG_OF_WATER)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.AGGIE_922, NPCs.AGGIE_8207)

    companion object {
        private val ANIMATION = Animation(Animations.AGGIE_MIXING_DYE_4352)
        private val ASHES = Item(Items.ASHES_592)
        private val POT_OF_FLOUR = Item(Items.POT_OF_FLOUR_1933)
        private val REDBERRIES_SINGLE = Item(Items.REDBERRIES_1951)
        private val PASTE_SOLID_INGREDIENTS = arrayOf(ASHES, REDBERRIES_SINGLE, POT_OF_FLOUR)
        private val BUCKET_OF_WATER = Item(Items.BUCKET_OF_WATER_1929)
        private val JUG_OF_WATER = Item(Items.JUG_OF_WATER_1937)
        private val CAULDRON_LOCATION = Location.create(3085, 3258, 0)
        private val COINS = Item(Items.COINS_995, 5)
        private val WOAD_LEAVES = Item(Items.WOAD_LEAF_1793, 2)
        private val ONIONS = Item(Items.ONION_1957, 2)
        private val REDBERRIES = Item(Items.REDBERRIES_1951, 3)
        private val PASTE = Item(Items.PASTE_2424)
        private val BLUE_DYE = Item(Items.BLUE_DYE_1767)
        private val YELLOW_DYE = Item(Items.YELLOW_DYE_1765)
        private val RED_DYE = Item(Items.RED_DYE_1763)
    }
}
