package content.region.asgarnia.falador.quest.squire.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Thurgo dialogue.
 */
@Initializable
class ThurgoDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_KNIGHTS_SWORD)
        removeAttribute(player, "thurgo:1")
        when (quest!!.getStage(player)) {
            else -> {
                sendDialogueOptions(player, "Ask a Question", "Skillcape of Smithing.", "Something else.").also {
                    stage =
                        0
                }
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (stage == 0 && buttonId == 1 || player.getAttribute("thurgo:1", false)) {
            when (stage) {
                0 ->
                    when (buttonId) {
                        1 ->
                            player(FaceAnim.HALF_GUILTY, "That's an unusual cape you're wearing, what is it?").also {
                                setAttribute(player, "thurgo:1", true)
                                stage = 10
                            }

                        2 ->
                            sendDialogue(player, "Thurgo doesn't appear to be interested in talking.").also {
                                stage = END_DIALOGUE
                            }
                    }

                10 -> {
                    npc(
                        FaceAnim.OLD_NORMAL,
                        "It's a Skillcape of Smithing. Shows that I am a master",
                        "blacksmith, but of course that's only to be expected. I",
                        "am an Imcando dwarf after all and everybody knows",
                        "we're the best blacksmiths.",
                    )
                    stage = 11
                }

                11 -> {
                    if (getStatLevel(player, Skills.SMITHING) == 99) {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Wow! An adventurer who is as skilled as me",
                            "would you like to purchase a Skillcape of",
                            "smithing for 99,000 coins?",
                        )
                        stage = 12
                        return true
                    }
                    end()
                }

                12 -> options("Yes, please.", "No, thanks.").also { stage++ }
                13 ->
                    when (buttonId) {
                        1 -> player(FaceAnim.HALF_GUILTY, "Yes, please.").also { stage++ }
                        2 -> player(FaceAnim.HALF_GUILTY, "No, thanks.").also { stage = END_DIALOGUE }
                    }

                14 -> {
                    end()
                    if (freeSlots(player) < 2) {
                        player(FaceAnim.HALF_GUILTY, "Sorry, I don't have enough room in my inventory.")
                        return true
                    }
                    if (!removeItem(player, COINS)) {
                        player(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
                        return true
                    } else {
                        player.inventory.add(if (player.getSkills().masteredSkills >= 1) ITEMS[1] else ITEMS[0])
                        player.inventory.add(ITEMS[2])
                        npc(FaceAnim.OLD_NORMAL, "There you go! You're truley a master of Smithing.")
                    }
                }
            }
            return true
        }
        when (quest!!.getStage(player)) {
            60 ->
                when (stage) {
                    0 -> player(FaceAnim.HALF_GUILTY, "About that sword...").also { stage++ }
                    1 ->
                        if (player.inventory.contains(667, 1) ||
                            player.equipment.contains(667, 1) ||
                            player.bank.contains(667, 1)
                        ) {
                            player(FaceAnim.HALF_GUILTY, "Thanks for all your help in getting it for me!").also {
                                stage = 9
                            }
                        } else {
                            npc(
                                FaceAnim.OLD_NORMAL,
                                "How are you doing finding those sword materials?",
                            ).also { stage++ }
                        }

                    2 ->
                        if (player.inventory.containsItem(BLURITE_ORE) && player.inventory.containsItem(IRON_BARS)) {
                            player(FaceAnim.HALF_GUILTY, "I have them right here.").also { stage = 5 }
                            stage = 5
                        } else {
                            if (player.inventory.containsItem(BLURITE_ORE)) {
                                player(FaceAnim.HALF_GUILTY, "I don't have enough iron bars...").also { stage++ }
                                return true
                            }
                            player(FaceAnim.HALF_GUILTY, "I don't have any blurite ore yet...").also { stage++ }
                            stage = 3
                        }

                    3 -> npc(FaceAnim.OLD_NORMAL, "Better go get some then, huh?").also { stage = END_DIALOGUE }
                    5 ->
                        sendDialogueLines(
                            player,
                            "You give the blurite ore and two iron bars to Thurgo. Thurgo starts",
                            "to make the sword. Thurgo hands you a sword.",
                        ).also {
                            stage++
                        }
                    6 ->
                        if (player.inventory.remove(BLURITE_ORE) && player.inventory.remove(IRON_BARS)) {
                            player(FaceAnim.HALF_GUILTY, "Thank you very much!")
                            player.inventory.add(BLURITE_SWORD)
                            stage = 7
                        }
                    7 ->
                        npc(FaceAnim.OLD_NORMAL, "Just remember to call in with more pie some time!").also {
                            stage =
                                END_DIALOGUE
                        }
                    9 -> npc(FaceAnim.OLD_NORMAL, "No worries mate.").also { stage = END_DIALOGUE }
                }

            50 ->
                when (stage) {
                    0 ->
                        if (player.inventory.contains(666, 1)) {
                            player(
                                FaceAnim.HALF_GUILTY,
                                "I have found a picture of the sword I would like you to",
                                "make.",
                            ).also {
                                stage =
                                    5
                            }
                        } else {
                            player(FaceAnim.HALF_GUILTY, "About that sword...").also { stage++ }
                        }

                    1 -> npc(FaceAnim.OLD_NORMAL, "Have you got a picture of the sword for me yet?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Sorry, not yet.").also { stage++ }
                    3 -> npc(FaceAnim.OLD_NORMAL, "Well, come back when you do.").also { stage = END_DIALOGUE }
                    5 ->
                        sendDialogue(
                            player,
                            "You give the portrait to Thurgo. Thurgo studies the portrait.",
                        ).also { stage++ }
                    6 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Ok. You'll need to get me some stuff in order for me",
                            "to make this.",
                        ).also { stage++ }
                    7 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "I'll need two iron bars to make the sword to start with.",
                            "I'll also need an ore called blurite. It's useless for",
                            "making actual weapons for fighting with except",
                            "crossbows, but I'll need some as decoration for the hilt.",
                        ).also {
                            stage++
                        }
                    8 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "It is fairly rare sort of ore... The only place I know",
                            "where to get it is under this cliff here...",
                        ).also {
                            stage++
                        }
                    9 -> npc(FaceAnim.OLD_NORMAL, "But it is guarded by a very powerful ice giant.").also { stage++ }
                    10 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Most of the rocks in that cliff are pretty useless, and",
                            "don't contain much of anything, but there's",
                            "DEFINITELY some blurite in there.",
                        ).also {
                            stage++
                        }
                    11 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "You'll need a little bit of mining experience to be able to",
                            "find it.",
                        ).also {
                            stage++
                        }
                    12 -> player(FaceAnim.HALF_GUILTY, "Ok. I'll go and find them then.").also { stage++ }
                    13 ->
                        if (removeItem(player, Item(Items.PORTRAIT_666))) {
                            quest!!.setStage(player, 60)
                            end()
                        } else {
                            end()
                        }
                }

            40 ->
                when (stage) {
                    0 -> player(FaceAnim.HALF_GUILTY, "About that sword...").also { stage++ }
                    1 -> npc(FaceAnim.OLD_NORMAL, "Have you got a picture of the sword for me yet?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Sorry, not yet.").also { stage++ }
                    3 -> npc(FaceAnim.OLD_NORMAL, "Well, come back when you do.").also { stage = END_DIALOGUE }
                }

            30 ->
                when (stage) {
                    0 -> player(FaceAnim.HALF_GUILTY, "Can you make me a special sword?").also { stage++ }
                    1 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Well, after bringing me my favorite food I guess I",
                            "should give it a go. What sort of sword is it?",
                        ).also {
                            stage++
                        }

                    2 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "I need you to make a sword for one of Falador's",
                            "knights. He had one which was passed down through five",
                            "generations, but his squire has lost it. So we need an",
                            "identical one to replace it.",
                        ).also {
                            stage++
                        }

                    3 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "A Knight's sword eh? Well I'd need to know exactly",
                            "how it looked before I could make a new one.",
                        ).also {
                            stage++
                        }

                    4 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "All the Faladian knights used to have swords with unique",
                            "designs according to their position. Could you bring me",
                            "a picture or something?",
                        ).also {
                            stage++
                        }

                    5 ->
                        player(FaceAnim.HALF_GUILTY, "I'll go and ask if his squire and see if I can find one.").also {
                            quest!!.setStage(player, 40)
                            end()
                        }
                }

            20 ->
                when (stage) {
                    0 ->
                        if (player.inventory.containsItem(REDBERRY_PIE)) {
                            options(
                                "Hello. Are you an Imcando dwarf?",
                                "Would you like some redberry pie?",
                            ).also { stage++ }
                        } else {
                            player(FaceAnim.HALF_GUILTY, "Hello. Are you an Imcando dwarf?").also { stage = 10 }
                        }

                    1 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HALF_GUILTY, "Hello. Are you an Imcando dwarf?").also { stage = 10 }
                            2 -> player(FaceAnim.HALF_GUILTY, "Would you like some redberry pie?").also { stage = 20 }
                        }

                    10 -> npc(FaceAnim.OLD_NORMAL, "Maybe. Who wants to know?").also { stage++ }
                    11 ->
                        if (!player.inventory.containsItem(REDBERRY_PIE)) {
                            end()
                        } else {
                            options(
                                "Would you like some redberry pie?",
                                "Can you make me a special sword?",
                            ).also { stage++ }
                        }

                    12 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HALF_GUILTY, "Would you like some redberry pie?").also { stage = 20 }
                            2 -> player(FaceAnim.HALF_GUILTY, "Can you make me a special sword?").also { stage++ }
                        }

                    13 ->
                        npc(FaceAnim.OLD_NORMAL, "No, I don't do that anymore. I'm getting old.").also {
                            stage =
                                END_DIALOGUE
                        }

                    20 -> sendDialogue(player, "You see Thurgo's eyes light up.").also { stage++ }
                    21 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "I'd never say no to a redberry pie! They're GREAT",
                            "stuff!",
                        ).also { stage++ }

                    22 ->
                        sendDialogueLines(
                            player,
                            "You hand over the pie. Thurgo eats the pie. Thurgo pats his",
                            "stomach.",
                        ).also {
                            stage++
                        }

                    23 ->
                        if (!player.inventory.remove(REDBERRY_PIE)) {
                            end()
                        } else {
                            quest!!.setStage(player, 30)
                            npc(
                                FaceAnim.OLD_NORMAL,
                                "By Guthix! THAT was good pie! Anyone who makes pie",
                                "like THAT has got to be alright!",
                            )
                        }
                }

            else ->
                when (stage) {
                    0 ->
                        when (buttonId) {
                            1 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "That's an unusual cape you're wearing, what is it?",
                                ).also {
                                    stage =
                                        10
                                }
                            2 ->
                                sendDialogue(player, "Thurgo doesn't appear to be interested in talking.").also {
                                    stage =
                                        END_DIALOGUE
                                }
                        }

                    10 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "It's a Skillcape of Smithing. Shows that I am a master",
                            "blacksmith, but of course that's only to be expected. I",
                            "am an Imcando dwarf after all and everybody knows",
                            "we're the best blacksmiths.",
                        ).also {
                            stage++
                        }

                    11 -> {
                        if (getStatLevel(player, Skills.SMITHING) == 99) {
                            npc(
                                FaceAnim.OLD_NORMAL,
                                "Wow! An adventurer who is as skilled as me",
                                "would you like to purchase a Skillcape of",
                                "smithing for 99,000 coins?",
                            ).also {
                                stage++
                            }
                            return true
                        }
                        end()
                    }

                    12 -> options("Yes, please.", "No, thanks.").also { stage++ }
                    13 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HALF_GUILTY, "Yes, please.").also { stage = 14 }
                            2 -> player(FaceAnim.HALF_GUILTY, "No, thanks.").also { stage = END_DIALOGUE }
                        }

                    14 -> {
                        end()
                        if (freeSlots(player) < 2) {
                            player(FaceAnim.HALF_GUILTY, "Sorry, I don't have enough room in my inventory.")
                            return true
                        } else if (!player.inventory.remove(COINS)) {
                            player(FaceAnim.HALF_GUILTY, "Sorry, I don't seem to have enough coins.")
                            return true
                        } else {
                            player.inventory.add(if (player.getSkills().masteredSkills >= 1) ITEMS[1] else ITEMS[0])
                            player.inventory.add(ITEMS[2])
                            npc(FaceAnim.OLD_NORMAL, "There you go! You're truley a master of Smithing.")
                        }
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.THURGO_604)

    companion object {
        private var quest: Quest? = null
        private val ITEMS = arrayOf(Item(9795), Item(9796), Item(9797))
        private val COINS = Item(995, 99000)
        private val REDBERRY_PIE = Item(2325)
        private val BLURITE_ORE = Item(668)
        private val IRON_BARS = Item(2351, 2)
        private val BLURITE_SWORD = Item(667)
    }
}
