package content.global.travel.charter

import core.api.*
import core.api.quest.isQuestComplete
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SeamanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size > 1 && isQuestComplete(player, Quests.PIRATES_TREASURE)) {
            if (player.equipment[EquipmentContainer.SLOT_RING] != null &&
                player.equipment[EquipmentContainer.SLOT_RING].id == Items.RING_OF_CHAROSA_6465
            ) {
                travel()
            } else if (isDiaryComplete(player, DiaryType.KARAMJA, 0)) {
                pay(15)
            } else {
                pay(30)
            }
            return true
        } else {
            sendMessage(player, "You may only use the Pay-fare option after completing Pirate's Treasure.")
        }
        npc(FaceAnim.HALF_GUILTY, "Do you want to go on a trip to Karamja?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "The trip will cost you 30 coins.").also { stage++ }
            1 -> {
                var charos = false
                if (player.equipment[EquipmentContainer.SLOT_RING] != null) {
                    charos = player.equipment[EquipmentContainer.SLOT_RING].id == Items.RING_OF_CHAROSA_6465
                }
                if (charos) {
                    options("Yes, please.", "No, thank you.", "(Charm) Or I could pay you nothing at all...")
                } else {
                    options("Yes, please.", "No, thank you.")
                }
                stage = 2
            }

            2 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Yes, please.")
                        stage =
                            if (isDiaryComplete(player, DiaryType.KARAMJA, 0)) {
                                9
                            } else {
                                11
                            }
                    }

                    2 -> player(FaceAnim.HALF_GUILTY, "No, thank you.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Or I could pay you nothing at all...").also { stage = 5 }
                }

            5 -> npc(FaceAnim.HALF_GUILTY, "Mmmm ... Nothing at all you say ...").also { stage++ }

            6 -> {
                interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "Yes, why not - jump aboard then.")
                if (!hasDiaryTaskComplete(player, DiaryType.FALADOR, 1, 10)) {
                    finishDiaryTask(player, DiaryType.FALADOR, 1, 10)
                }
                stage = 30
            }

            9 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Wait a minute... Aren't those Karamja gloves?",
                    "Thought I'd seen you helping around the island.",
                    "You can go on half price - 15 coins.",
                ).also {
                    stage++
                }

            10 -> pay(15)
            11 -> pay(30)
            20 -> end()
            30 -> travel()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SeamanDialogue(player)
    }

    fun pay(price: Int) {
        if (!removeItem(player, Item(Items.COINS_995, price))) {
            player(FaceAnim.HALF_GUILTY, "Sorry, I don't have enough coins for that.")
            stage = 20
        } else {
            sendMessage(player, "You pay $price coins and board the ship.")
            travel()
        }
    }

    fun travel() {
        end()
        Charter.PORT_SARIM_TO_KARAMJA.sail(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_TOBIAS_376, NPCs.SEAMAN_LORRIS_377, NPCs.SEAMAN_THRESNOR_378)
    }
}
