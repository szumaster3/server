package content.region.asgarnia.falador.dwarven_mines.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Dwarf (Mining guild) dialogue.
 */
@Initializable
class DwarfSkillcapeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Welcome to the Mining Guild.", "Can I help you with anything?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendOptions(player, "What would you like to say?", "What have you got in the Guild?", "What do you dwarves do with the ore you mine?", "Can you tell me about your skillcape?", "No thanks, I'm fine.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "What have you got in the guild?").also { stage = 10 }
                2 -> player(FaceAnim.HALF_GUILTY, "What do you dwarves do with the ore you mine?").also { stage = 20 }
                3 -> player(FaceAnim.HALF_GUILTY, "Can you tell me about your skillcape?").also { stage = 40 }
                4 -> player(FaceAnim.HALF_GUILTY, "No thanks, I'm fine.").also { stage = END_DIALOGUE }
            }
            10 -> npc(FaceAnim.OLD_NORMAL, "Ooh, it's WONDERFUL! There are lots of coal rocks,", "and even a few mithril rocks in the guild,", "all exclusively for people with at least level 60 mining.", "There's no better mining site anywhere near here.").also { stage = 0 }
            20 -> npc(FaceAnim.OLD_NORMAL, "What do you think? We smelt it into bars, smith the metal", "to make armour and weapons, then we exchange them for", "goods and services.").also { stage++ }
            21 -> player(FaceAnim.HALF_GUILTY, "I don't see many dwarves", "selling armour or weapons here.").also { stage++ }
            22 -> npc(FaceAnim.OLD_NORMAL, "No, this is only a mining outpost. We dwarves don't much", "like to settle in human cities. Most of the ore is carted off", "to Keldagrim, the great dwarven city. They've got a", "special blast furnace up there - it makes smelting the ore", "so much easier. There are plenty of dwarven traders working in Keldagrim.").also { stage++ }
            23 -> npcl(FaceAnim.OLD_NORMAL, "Anyway, can I help you with anything else?").also { stage = 0 }
            40 -> npc(FaceAnim.OLD_NORMAL, "Sure, this is a Skillcape of Mining. It shows my stature as", "a master miner! It has all sorts of uses, if you", "have a level of 99 mining I'll sell you one.").also { stage++ }
            41 -> if (getStatLevel(player, Skills.MINING) < 99) {
                sendOptions(player, "What would you like to say?", "What have you got in the Guild?", "What do you dwarves do with the ore you mine?", "Can you tell me about your skillcape?", "No thanks, I'm fine.").also { stage = 0 }
            } else {
                showTopics(
                    Topic("I'd like to buy a Skillcape of Mining.", 42),
                    Topic("Goodbye.", END_DIALOGUE)
                )
            }
            42 -> npc(FaceAnim.OLD_NORMAL, "It will cost you 99,000 gold coins, are you sure?").also { stage++ }
            43 -> showTopics(
                Topic("Yes.",44),
                Topic("No.", END_DIALOGUE)
            )
            44 -> {
                end()
                val coins = Item(Items.COINS_995, 99000)
                val miningCapeId = arrayOf(Item(Items.MINING_CAPE_9792), Item(Items.MINING_CAPET_9793), Item(Items.MINING_HOOD_9794))
                when {
                    !inInventory(player, Items.COINS_995, 99000) ->
                        npc(FaceAnim.OLD_NORMAL, "You need 99,000 gold coins in order to buy a Skillcape of mining.")

                    freeSlots(player) < 2 ->
                        sendMessage(player,"You don't have enough room in your inventory.")

                    else -> {
                        removeItem(player, coins)
                        player.inventory.add(miningCapeId[if (player.getSkills().masteredSkills > 1) 1 else 0], miningCapeId[2])
                        npc(FaceAnim.OLD_NORMAL, "Thanks!")
                    }
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DwarfSkillcapeDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.DWARF_3295)

}
