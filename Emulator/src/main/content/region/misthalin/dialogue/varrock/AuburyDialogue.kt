package content.region.misthalin.dialogue.varrock

import content.global.travel.EssenceTeleport.teleport
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AuburyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val quest = getQuestStage(player, Quests.RUNE_MYSTERIES)
        if (quest == 40) {
            npc(
                "My gratitude to you adventurer for bringing me these",
                "research notes. I notice that you brought the head",
                "wizard a special talisman that was the key to our finally",
                "unlocking the puzzle.",
            )
            stage = 900
            return true
        }
        if (isMaster(player, Skills.RUNECRAFTING)) {
            options("Can I buy a Skillcape of Runecrafting?", "Something else")
            stage = 450
        } else {
            npc("Do you want to buy some runes?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = getQuestStage(player, Quests.RUNE_MYSTERIES)
        when (stage) {
            0 -> {
                if (quest == 30) {
                    options(
                        "Yes please!",
                        "Oh, it's a rune shop. No thank you, then.",
                        "I have been sent here with a package for you.",
                    )
                    stage = 800
                    return true
                }
                if (quest == 40) {
                    npc(
                        "My gratitude to you adventurer for bringing me these",
                        "research notes. I notice that you brought the head",
                        "wizard a special talisman that was the key to our finally",
                        "unlocking the puzzle.",
                    )
                    stage = 900
                    return true
                }
                if (quest == 50) {
                    npc(
                        "I suggest you take those research notes of mine back",
                        "to the head wizard at the Wizards' Tower.",
                    )
                    stage = 950
                    return true
                }
                stage =
                    if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                        options("Yes please!", "Oh, it's a rune shop. No thank you, then.")
                        100
                    } else {
                        options("Yes, please.", "No thanks.", "Can you teleport me to the rune essence?")
                        1
                    }
            }

            900 -> {
                npc(
                    "Combined with the information I had already collated",
                    "regarding the Rune Essence, I think we have finally",
                    "unlocked the power to",
                )
                stage = 901
            }

            901 -> {
                npc(
                    "...no. I am getting ahead of myself. Please take this",
                    "summary of my research back to the head wizard at",
                    "the Wizards' Tower. I trust his judgement on whether",
                    "to let you in on our little secret or not.",
                )
                stage = 902
            }

            902 -> {
                setQuestStage(player, Quests.RUNE_MYSTERIES, 50)
                if (!player.inventory.add(NOTES)) {
                    GroundItemManager.create(GroundItem(NOTES, player.location, player))
                }
                sendItemDialogue(player, NOTES, "Aubury gives you his research notes.")
                stage = 903
            }

            903 -> end()
            950 -> {
                if (!player.bank.containsItem(NOTES) && !player.inventory.containsItem(NOTES)) {
                    player("I can't... I lost them...")
                    stage = 955
                    return true
                }
                player("Ok then, I will do that.")
                stage = 951
            }

            951 -> {
                npc("Unless you were talking to me because you wished to", "buy some runes?")
                stage = 952
            }

            952 -> {
                options("Yes please!", "Oh, it's a rune shop. No thank you, then.")
                stage = 100
            }

            955 -> {
                npc(
                    "Well, luckily I have duplicates. It's a good thing they",
                    "are written in code, I would not want the wrong kind",
                    "of person to get access to the information contained",
                    "within.",
                )
                stage = 956
            }

            956 -> {
                if (!player.inventory.add(NOTES)) {
                    GroundItemManager.create(GroundItem(NOTES, player.location, player))
                }
                sendItemDialogue(player, NOTES, "Aubury gives you his research notes.")
                stage = 957
            }

            957 -> end()
            800 ->
                when (buttonId) {
                    1 -> {
                        npc.openShop(player)
                        end()
                    }

                    2 -> {
                        player("Oh, it's a rune shop. No thank you, then.")
                        stage = 105
                    }

                    3 -> {
                        player(
                            "I have been sent here with a package for you. It's from",
                            "the head wizard at the Wizards' Tower.",
                        )
                        stage = 801
                    }
                }

            801 -> {
                player(
                    "I have been sent here with a package for you. It's from",
                    "the head wizard at the Wizards' Tower.",
                )
                stage = 802
            }

            802 -> {
                npc(
                    "Really? But... surely he can't  have..? Please, let me",
                    "have it, it must be extremely important for him to have",
                    "sent a stranger.",
                )
                stage = 803
            }

            803 -> {
                if (!player.inventory.containsItem(PACKAGE)) {
                    player("Uh... yeah... about that... I kind of don't have it with", "me...")
                    stage = 804
                    return true
                }
                sendDialogue("You hand Aubury the research package.")
                stage = 807
            }

            804 -> {
                npc("What kind of person tells me they have a delivery for", "me, but not with them? Honestly.")
                stage = 805
            }

            805 -> {
                npc("Come back when you do.")
                stage = 806
            }

            806 -> end()
            807 ->
                if (removeItem(player, PACKAGE)) {
                    setQuestStage(player, Quests.RUNE_MYSTERIES, 40)
                    npc(
                        "This... this is incredible. Please, give me a few moments",
                        "to quickly look over this, and then talk to me again.",
                    )
                    stage = 808
                }

            808 -> end()
            1 ->
                when (interfaceId) {
                    230 ->
                        when (buttonId) {
                            1 -> {
                                npc.openShop(player)
                                end()
                            }

                            2 -> {
                                player("No thanks.")
                                stage = 10
                            }

                            3 -> {
                                player("Can you teleport me to the rune essence?")
                                stage = 11
                            }
                        }
                }

            100 ->
                when (buttonId) {
                    1 -> {
                        openNpcShop(player, npc.id)
                        end()
                    }

                    2 -> {
                        player("Oh, it's a rune shop. No thank you, then.")
                        stage = 105
                    }
                }

            105 -> end()
            106 -> end()
            10 -> end()
            11 -> {
                teleport(npc, player)
                end()
            }

            450 ->
                when (buttonId) {
                    1 -> {
                        player("Can I buy a Skillcape of Runecrafting?")
                        stage = 2
                    }

                    2 -> {
                        npc("Do you want to buy some runes?")
                        stage = 0
                    }
                }

            2 -> {
                npc("Certainly! Right when you give me 99000 coins.")
                stage = 3
            }

            3 -> {
                options("Okay, here you go.", "No")
                stage = 4
            }

            4 ->
                when (buttonId) {
                    1 -> {
                        player("Okay, here you go.")
                        stage = 5
                    }

                    2 -> end()
                }

            5 -> {
                if (purchase(player, Skills.RUNECRAFTING)) {
                    npc("There you go! Enjoy.")
                }
                stage = 6
            }

            6 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return AuburyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AUBURY_553)
    }

    companion object {
        private val PACKAGE = Item(Items.RESEARCH_PACKAGE_290)
        private val NOTES = Item(Items.NOTES_291)
    }
}
