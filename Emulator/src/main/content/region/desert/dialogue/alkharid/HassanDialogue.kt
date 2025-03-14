package content.region.desert.dialogue.alkharid

import core.api.quest.finishQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HassanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        when (quest!!.getStage(player)) {
            100 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You are a friend of the town of Al-Kharid. If we have",
                    "more tasks to complete, we will ask you. Please, keep in",
                    "contact. Good employees are not easy to find.",
                ).also { stage = 0 }

            60 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You have the eternal gratitude of the Emir for",
                    "rescuing his son. I am authorised to pay you 700",
                    "coins.",
                ).also { stage = 0 }

            40, 50, 20, 30 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I understand the Spymaster has hired you. I will pay",
                    "the reward only when the Prince is rescued.",
                )

            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Have you found the spymaster, Osman, yet? You",
                    "cannot proceed in your task without reporting to him.",
                )

            else -> npc(FaceAnim.HALF_GUILTY, "Greetings I am Hassan, Chancellor to the Emir of Al-", "Kharid.")
        }
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            100 -> end()
            60 -> {
                end()
                finishQuest(player, Quests.PRINCE_ALI_RESCUE)
            }

            30, 40, 50, 20 -> end()
            10 -> end()
            0 ->
                when (stage) {
                    0 ->
                        options(
                            "Can I help you? You must need some help here in the desert.",
                            "It's just too hot here. How can you stand it?",
                            "Do you mind if I just kill your warriors?",
                        ).also { stage = 0 }

                    1 ->
                        when (buttonId) {
                            1 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "Can I help you? You must need some help here in the",
                                    "desert.",
                                ).also { stage = 10 }

                            2 ->
                                player(FaceAnim.HALF_GUILTY, "It's just too hot here. How can you stand it?").also {
                                    stage = 20
                                }

                            3 ->
                                player(FaceAnim.HALF_GUILTY, "Do you mind if I just kill your warriors?").also {
                                    stage =
                                        30
                                }
                        }

                    10 -> {
                        quest!!.start(player)
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "I need the services of someone yes. If you are",
                            "interested, see the spymaster, Osman. I manage the",
                            "finances here. Come to me when you need payment.",
                        ).also { stage = END_DIALOGUE }
                    }

                    20 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "We manage, in our humble way. We are a wealthy",
                            "town and we have water. It cures many thirsts.",
                        ).also { stage++ }

                    21 -> core.api.sendDialogue(player, "The chancellor hands you some water.").also { stage++ }
                    22 -> {
                        if (!player.inventory.add(Item(Items.JUG_OF_WATER_1937))) {
                            GroundItemManager.create(Item(Items.JUG_OF_WATER_1937), player)
                        }
                        end()
                    }

                    30 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "You are welcome. They are not expensive. We have",
                            "them here to stop the elite guard being bothered. They",
                            "are a little harder to kill.",
                        ).also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HassanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HASSAN_923)
    }
}
