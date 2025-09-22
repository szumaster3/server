package content.global.travel.ship

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Seaman dialogue.
 */
@Initializable
class SeamanDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (args.size > 1 && isQuestComplete(player, Quests.PIRATES_TREASURE)) {
            when {
                hasCharosRing() -> travel()
                hasKaramjaDiscount() -> pay(15)
                else -> pay(30)
            }
            return true
        }
        sendMessage(player, "You may only use the Pay-fare option after completing Pirate's Treasure.")
        npc(FaceAnim.HALF_GUILTY, "Do you want to go on a trip to Karamja?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npc(FaceAnim.HALF_GUILTY, "The trip will cost you 30 coins.")
                stage = 1
            }

            1 -> {
                val opts = if (hasCharosRing()) {
                    arrayOf("Yes, please.", "No, thank you.", "(Charm) Or I could pay you nothing at all...")
                } else {
                    arrayOf("Yes, please.", "No, thank you.")
                }
                options(*opts)
                stage = 2
            }

            2 -> when (buttonId) {
                1 -> {
                    player(FaceAnim.HALF_GUILTY, "Yes, please.")
                    stage = if (hasKaramjaDiscount()) 3 else 4
                }
                2 -> {
                    player(FaceAnim.HALF_GUILTY, "No, thank you.")
                    stage = 7
                }
                3 -> {
                    player(FaceAnim.HALF_GUILTY, "Or I could pay you nothing at all...")
                    stage = 5
                }
            }

            3 -> pay(15)
            4 -> pay(30)

            5 -> {
                npc(FaceAnim.HALF_GUILTY, "Mmmm ... Nothing at all you say ...")
                stage = 6
            }

            6 -> {
                npc(FaceAnim.HALF_GUILTY, "Yes, why not - jump aboard then.")
                if (!hasDiaryTaskComplete(player, DiaryType.FALADOR, 1, 10)) {
                    finishDiaryTask(player, DiaryType.FALADOR, 1, 10)
                }
                travel()
                stage = 7
            }

            7 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SeamanDialogue(player)

    private fun pay(price: Int) {
        if (!removeItem(player, Item(Items.COINS_995, price))) {
            val msg = if (price == 15) {
                "You do not have enough coins to pay passage. You need 15 coins having earned the Karamja gloves."
            } else {
                "You do not have enough money for that."
            }
            sendDialogue(player, msg)
            sendMessage(player, "You cannot afford that.")
            stage = 7
            return
        }

        if (hasKaramjaDiscount()) {
            sendMessages(player, "The Seaman smiles as he recognises you as having earned Karamja gloves", "and lets you pass for half price - 15 coins.")
        } else {
            sendMessage(player, "You pay $price coins and board the ship.")
        }

        travel()
        stage = 7
    }

    private fun travel() {
        end()
        CharterShip.PORT_SARIM_TO_KARAMJA.sail(player)
    }

    private fun hasCharosRing(): Boolean = player.equipment[EquipmentContainer.SLOT_RING]?.id == Items.RING_OF_CHAROSA_6465

    private fun hasKaramjaDiscount(): Boolean = isDiaryComplete(player, DiaryType.KARAMJA, 0) || player!!.achievementDiaryManager.hasGlove()

    override fun getIds(): IntArray =
        intArrayOf(NPCs.CAPTAIN_TOBIAS_376, NPCs.SEAMAN_LORRIS_377, NPCs.SEAMAN_THRESNOR_378)
}
