package content.region.asgarnia.dialogue.dwarvenmines

import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class DwarfDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Welcome to the Mining Guild.", "Can I help you with anything?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0, 11 -> {
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "What have you got in the Guild?",
                    "What do you dwarves do with the ore you mine?",
                    "Can you tell me about your skillcape?",
                    "No thanks, I'm fine.",
                )
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "What have you got in the guild?")
                        stage = 10
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "What do you dwarves do with the ore you mine?")
                        stage = 20
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "Can you tell me about your skillcape?")
                        stage = 40
                    }

                    4 -> {
                        player(FaceAnim.HALF_GUILTY, "No thanks, I'm fine.")
                        stage = 30
                    }
                }

            20 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "What do you think? We smelt it into bars, smith the metal",
                    "to make armour and weapons, then we exchange them for",
                    "goods and services.",
                )
                stage = 21
            }

            21 -> {
                player(FaceAnim.HALF_GUILTY, "I don't see many dwarves", "selling armour or weapons here.")
                stage = 22
            }

            22 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "No, this is only a mining outpost. We dwarves don't much",
                    "like to settle in human cities. Most of the ore is carted off",
                    "to Keldagrim, the great dwarven city. They've got a",
                    "special blast furnace up there - it makes smelting the ore",
                )
                stage = 12
            }

            10 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Ooh, it's WONDERFUL! There are lots of coal rocks,",
                    "and even a few mithril rocks in the guild,",
                    "all exclusively for people with at least level 60 mining.",
                    "There's no better mining site anywhere near here.",
                )
                stage = 11
            }

            12 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "so much easier. There are plenty of dwarven traders",
                    "working in Keldagrim. Anyway, can I help you with anything ",
                    "else?",
                )
                stage = 11
            }

            30, 44, 49 -> end()
            40 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Sure, this is a Skillcape of Mining. It shows my stature as",
                    "a master miner! It has all sorts of uses , if you",
                    "have a level of 99 mining I'll sell you one.",
                )
                stage = 41
            }

            41 ->
                if (player.getSkills().getStaticLevel(Skills.MINING) < 99) {
                    sendDialogueOptions(
                        player,
                        "What would you like to say?",
                        "What have you got in the Guild?",
                        "What do you dwarves do with the ore you mine?",
                        "Can you tell me about your skillcape?",
                        "No thanks, I'm fine.",
                    )
                    stage = 1
                } else {
                    options("I'd like to buy a Skillcape of Mining.", "Goodbye.")
                    stage = 43
                }

            43 ->
                when (buttonId) {
                    1 -> {
                        player("I'd like to buy a Skillcape of Mining.")
                        stage = 45
                    }

                    2 -> {
                        player("Goodbye.")
                        stage = 44
                    }
                }

            45 -> {
                npc(FaceAnim.OLD_NORMAL, "It will cost you 99,000 gold coins, are you sure?")
                stage = 46
            }

            46 -> {
                options("Yes.", "No.")
                stage = 47
            }

            47 ->
                when (buttonId) {
                    1 -> {
                        player("Yes.")
                        stage = 48
                    }

                    2 -> end()
                }

            48 -> {
                if (!player.inventory.containsItem(COINS)) {
                    npc(FaceAnim.OLD_NORMAL, "You need 99,000 gold coins in order to buy a Skillcape", "of mining.")
                    stage = 44
                    return true
                }
                if (player.inventory.freeSlots() < 2) {
                    player.packetDispatch.sendMessage("You don't have enough room in your inventory.")
                    end()
                    return true
                }
                if (!player.inventory.containsItem(COINS)) {
                    end()
                    return true
                }
                if (player.inventory.remove(COINS) &&
                    player.inventory.add(ITEMS[if (player.getSkills().masteredSkills > 1) 1 else 0], ITEMS[2])
                ) {
                    npc(FaceAnim.OLD_NORMAL, "Thanks!")
                    stage = 49
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DwarfDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DWARF_3295)
    }

    companion object {
        private val ITEMS = arrayOf(Item(9792), Item(9793), Item(9794))
        private val COINS = Item(995, 99000)
    }
}
