package content.region.misthalin.dialogue.draynor

import core.api.*
import core.api.quest.isQuestComplete
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
import org.rs.consts.Components
import org.rs.consts.Music
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AggieDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
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
        quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        npc("What can I help you with?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
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
                if (!isQuestComplete(player, Quests.SWEPT_AWAY)) {
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
                    make(1767)
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
                    make(1765)
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
                    make(1763)
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
                val item = Item(995, 20)
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
                lock(player, 4)
                GameWorld.Pulser.submit(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> openInterface(player, Components.FADE_TO_BLACK_120)
                                1 ->
                                    teleport(
                                        player,
                                        Location.create(3291, 4514, 0),
                                        TeleportManager.TeleportType.NORMAL,
                                    )
                                6 -> {
                                    unlock(player)
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
        }
        return true
    }

    fun make(dye: Int) {
        stopWalk(npc!!)
        npc.animate(ANIMATION)
        npc.faceLocation(CAULDRON_LOCATION)
    }

    private fun hasIngredients(player: Player): Boolean {
        for (i in PASTE_SOLID_INGREDIENTS) {
            if (!player.inventory.containsItem(i)) {
                return false
            }
        }

        return player.inventory.containsItem(BUCKET_OF_WATER) || player.inventory.containsItem(JUG_OF_WATER)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AGGIE_922)
    }

    companion object {
        private val ANIMATION = Animation(4352)
        private val ASHES = Item(592)
        private val POT_OF_FLOUR = Item(1933)
        private val REDBERRIES_SINGLE = Item(1951)
        private val PASTE_SOLID_INGREDIENTS = arrayOf(ASHES, REDBERRIES_SINGLE, POT_OF_FLOUR)
        private val BUCKET_OF_WATER = Item(1929)
        private val JUG_OF_WATER = Item(1937)
        private val CAULDRON_LOCATION = Location.create(3085, 3258, 0)
        private val COINS = Item(995, 5)
        private val WOAD_LEAVES = Item(1793, 2)
        private val ONIONS = Item(1957, 2)
        private val REDBERRIES = Item(1951, 3)
        private val PASTE = Item(2424)
        private val BLUE_DYE = Item(1767)
        private val YELLOW_DYE = Item(1765)
        private val RED_DYE = Item(1763)
    }
}
