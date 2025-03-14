package content.global.dialogue

import content.global.skill.construction.HouseLocation
import content.global.skill.construction.HousingStyle
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class EstateAgentDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hello. Welcome to the " + settings!!.name + " Housing Agency! What", "can I do for you?")
        stage = START_DIALOGUE
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                if (player.houseManager.hasHouse()) {
                    options(
                        "Can you move my house please?",
                        "Can you redecorate my house please?",
                        "Could I have a Construction guidebook?",
                        "Tell me about houses.",
                        "Tell me about that skillcape you're wearing.",
                    ).also {
                        stage++
                    }
                } else {
                    options("How can I get a house?", "Tell me about houses.").also { stage = 2 }
                }
            }

            1 ->
                when (buttonId) {
                    1 -> player("Can you move my house please?").also { stage = 10 }
                    2 -> player("Can you redecorate my house please?").also { stage = 30 }
                    3 -> player("Could I have a Construction guidebook?").also { stage = 50 }
                    4 -> player("Tell me about houses!").also { stage = 60 }
                    5 -> {
                        player("Tell me about that skillcape you're wearing!")
                        stage = if (Skillcape.isMaster(player, Skills.CONSTRUCTION)) 102 else 100
                    }
                }
            2 ->
                when (buttonId) {
                    1 -> player("How can I get a house?").also { stage++ }
                    2 -> player("Tell me about houses.").also { stage = 60 }
                }
            3 ->
                npc(
                    "I can sell you a starting house in Rimmington for",
                    "1000 coins. As you increase your construction skill you",
                    "will be able to have your house moved to other areas",
                    "and redecorated in other styles.",
                ).also {
                    stage++
                }
            4 -> npc("Do you want to buy a starter house?").also { stage++ }
            5 -> options("Yes please!", "No thanks.").also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> player("Yes please!").also { stage++ }
                    2 -> player("No thanks.").also { stage = END_DIALOGUE }
                }
            7 -> {
                if (!removeItem(player, Item(995, 1000))) {
                    end()
                    npc("You don't have enough money to buy a house,", "come back when you can afford one.")
                } else {
                    player.houseManager.createNewHouseAt(HouseLocation.RIMMINGTON)
                    npc(
                        "Thank you. Go through the Rimmington house portal",
                        "and you will find your house ready for you to start",
                        "building in it.",
                    )
                    stage = END_DIALOGUE
                }
            }

            10 -> npc("Certainly. Where would you like it moved to?").also { stage++ }
            11 ->
                options(
                    "Rimmington (5,000)",
                    "Taverley (5,000)",
                    "Pollnivneach (7,500)",
                    "Rellekka (10,000)",
                    "More...",
                ).also {
                    stage++
                }

            12 ->
                when (buttonId) {
                    1 -> player("To Rimmington please!").also { stage = 15 }
                    2 -> player("To Taverly please!").also { stage = 16 }
                    3 -> player("To Pollnivneach please!").also { stage = 17 }
                    4 -> player("To Rellekka please!").also { stage = 18 }
                    5 -> options("Brimhaven (15,000)", "Yanille (25,000)", "...Previous", "Back").also { stage = 13 }
                }

            13 ->
                when (buttonId) {
                    1 -> player("To Brimhaven please!").also { stage = 19 }
                    2 -> player("To Yanille please!").also { stage = 20 }
                    3 ->
                        options(
                            "Rimmington (5,000)",
                            "Taverley (5,000)",
                            "Pollnivneach (7,500)",
                            "Rellekka (10,000)",
                            "More...",
                        ).also {
                            stage =
                                12
                        }
                }
            15 -> configureMove(HouseLocation.RIMMINGTON)
            16 -> configureMove(HouseLocation.TAVERLY)
            17 -> configureMove(HouseLocation.POLLNIVNEACH)
            18 -> configureMove(HouseLocation.RELLEKKA)
            19 -> configureMove(HouseLocation.BRIMHAVEN)
            20 -> configureMove(HouseLocation.YANILLE)
            30 ->
                npc(
                    "Certainly. My magic can rebuild the house in a",
                    "completely new style! What style would you like?",
                ).also {
                    stage++
                }
            31 ->
                options(
                    "Basic wood (5,000)",
                    "Basic stone (5,000)",
                    "Whitewashed stone (7,500)",
                    "Fremennik-style wood (10,000)",
                    "More...",
                ).also {
                    stage++
                }
            32 ->
                when (buttonId) {
                    1 -> player("Basic wood please!").also { stage = 35 }
                    2 -> player("Basic stone please!").also { stage = 36 }
                    3 -> player("Whitewashed stone please!").also { stage = 37 }
                    4 -> player("Fremennik-style wood please!").also { stage = 38 }
                    5 ->
                        options(
                            "Tropical wood (15,000)",
                            "Fancy stone (25,000)",
                            "Previous...",
                            "Back",
                        ).also { stage++ }
                }
            33 ->
                when (buttonId) {
                    1 -> player("Tropical wood please!").also { stage = 39 }
                    2 -> player("Fancy stone please!").also { stage = 40 }
                    3 ->
                        options(
                            "Basic wood (5,000)",
                            "Basic stone (5,000)",
                            "Whitewashed stone (7,500)",
                            "Fremennik-style wood (10,000)",
                            "More...",
                        ).also {
                            stage =
                                32
                        }
                }
            35 -> redecorate(HousingStyle.BASIC_WOOD)
            36 -> redecorate(HousingStyle.BASIC_STONE)
            37 -> redecorate(HousingStyle.WHITEWASHED_STONE)
            38 -> redecorate(HousingStyle.FREMENNIK_STYLE_WOOD)
            39 -> redecorate(HousingStyle.TROPICAL_WOOD)
            40 -> redecorate(HousingStyle.FANCY_STONE)
            50 -> {
                if (player.hasItem(CONSTRUCTION_GUIDE_8463)) {
                    npc("You've already got one!")
                } else {
                    npc("Certainly.")
                    player.inventory.add(CONSTRUCTION_GUIDE_8463)
                }
                stage = END_DIALOGUE
            }
            60 ->
                npc(
                    "It all came out of the wizards' experiments. They found",
                    "a way to fold space, so that they could pack many",
                    "acres of land into an area only a foot across.",
                ).also {
                    stage++
                }
            61 ->
                npc(
                    "They created several folded-space regions across",
                    "" + settings!!.name + ". Each one contains hundreds of small plots",
                    "where people can build houses.",
                ).also { stage++ }
            62 ->
                player(
                    "Ah, so that's how everyone can have a house without",
                    "them cluttering up the world!",
                ).also { stage++ }
            63 ->
                npc(
                    "Quite. The wizards didn't want to get bogged down",
                    "in the business side of things so they ",
                    "hired me to sell the houses.",
                ).also {
                    stage++
                }
            64 ->
                npc(
                    "There are various other people across " + settings!!.name + " who can",
                    "help you furnish your house. You should start buying",
                    "planks from the sawmill operator in Varrock.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            100 ->
                npc(
                    "As you may know, skillcapes are only available to masters",
                    "in a skill. I have spent my entire life building houses and",
                    "now I spend my time selling them! As a sign of my abilities",
                    "I wear this Skillcape of Construction. If you ever have",
                ).also {
                    stage++
                }
            101 ->
                npc(
                    "enough skill to build a demonic throne, come and talk to",
                    "me and I'll sell you a skillcape like mine.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            102 ->
                npc(
                    FaceAnim.JOLLY,
                    "I see you have recently achieved 99 construction.",
                    "Would you like to buy a cape for 99,0000 gp?",
                ).also {
                    stage++
                }
            103 -> options("Yes, I'll pay the 99k", "No thanks, maybe later.").also { stage++ }
            104 ->
                when (buttonId) {
                    1 -> {
                        if (Skillcape.purchase(player, Skills.CONSTRUCTION)) {
                            npc("Here you go lad, enjoy!")
                        }
                        stage = END_DIALOGUE
                    }

                    2 -> player("No thanks, maybe later.").also { stage = END_DIALOGUE }
                }

            END_DIALOGUE -> end()
        }
        return true
    }

    private fun configureMove(location: HouseLocation) {
        when {
            !location.hasLevel(player) -> {
                npc(
                    "I'm afraid you don't have a high enough construction",
                    "level to move there. You need to have level " + location.levelRequirement + ".",
                )
                stage = 11
                return
            }

            location == player.houseManager.location -> {
                npc("Your house is already there!")
                stage = 11
                return
            }

            !inInventory(player, Items.COINS_995, location.cost) -> {
                npc("Hmph. Come back when you have " + location.cost + " coins.")
                stage = END_DIALOGUE
                return
            }

            else -> {
                player.inventory.remove(Item(Items.COINS_995, location.cost))
                player.houseManager.location = location
                npc(
                    "Your house has been moved to " +
                        location.name.lowercase().replaceFirstChar {
                            c ->
                            c.uppercase()
                        } +
                        ".",
                )
                if (inBorders(player, getRegionBorders(REGION_VARROCK_NE)) &&
                    !hasDiaryTaskComplete(player, DiaryType.VARROCK, 0, 11)
                ) {
                    finishDiaryTask(player, DiaryType.VARROCK, 0, 11)
                }
                stage = END_DIALOGUE
            }
        }
    }

    private fun redecorate(style: HousingStyle) {
        when {
            !style.hasLevel(player) -> {
                npc("You need a construction level of " + style.levelRequirement + " to buy this style.")
                stage = 31
                return
            }

            style == player.houseManager.style -> {
                npc("Your house is already in that style!")
                stage = 31
                return
            }

            !inInventory(player, Items.COINS_995, style.cost) -> {
                npc("Hmph. Come back when you have " + style.cost + " coins.")
                stage = END_DIALOGUE
                return
            }

            else -> {
                player.inventory.remove(Item(Items.COINS_995, style.cost))
                player.houseManager.redecorate(style)
                npc("Your house has been redecorated.")
                if (inBorders(player, getRegionBorders(REGION_VARROCK_NE)) &&
                    !hasDiaryTaskComplete(player, DiaryType.VARROCK, 2, 7)
                ) {
                    finishDiaryTask(player, DiaryType.VARROCK, 2, 7)
                }
                stage = END_DIALOGUE
            }
        }
    }

    override fun newInstance(player: Player): Dialogue {
        return EstateAgentDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ESTATE_AGENT_4247)
    }

    private val CONSTRUCTION_GUIDE_8463 = Item(Items.CONSTRUCTION_GUIDE_8463, 1)
    private val REGION_VARROCK_NE = 12854
}
