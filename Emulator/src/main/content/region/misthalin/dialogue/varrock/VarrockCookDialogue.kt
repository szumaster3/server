package content.region.misthalin.dialogue.varrock

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class VarrockCookDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "What do you want? I'm busy!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                setTitle(player, 3)
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "Can you sell me any food?",
                    "Can you give me any free food?",
                    "I don't want anything from this horrible kitchen.",
                ).also { stage++ }
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Can you sell me any food?")
                        stage = 10
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "Can you give me any free food?")
                        stage = 20
                    }

                    3 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "I don't want anything from this horrible kitchen.",
                        )
                        stage = 30
                    }
                }

            10 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I suppose I could sell you some cabbage, if you're willing to",
                    "pay for it. Cabbage is good for you.",
                )
                stage = 11
            }

            11 -> {
                interpreter.sendOptions(
                    "What would you like to say?",
                    "Alright, I'll buy a cabbage.",
                    "No thanks, I don't like cabbage.",
                )
                stage = 12
            }

            12 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Alright, I'll buy a cabbage.")
                        stage = 150
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "No thanks, I don't like cabbage.")
                        stage = 160
                    }
                }

            20 -> {
                npc(FaceAnim.HALF_GUILTY, "Can you give me any free money?")
                stage = 21
            }

            21 -> {
                player(FaceAnim.HALF_GUILTY, "Why should I give you free money?")
                stage = 22
            }

            22 -> {
                npc(FaceAnim.HALF_GUILTY, "Why should I give you free food?")
                stage = 23
            }

            23 -> {
                player(FaceAnim.HALF_GUILTY, "Oh, forget it.")
                stage = 24
            }

            24 -> end()
            30 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "How dare you? I put a lot of effort into cleaning this",
                    "kitchen. My daily sweat and elbow-grease keep this kitchen",
                    "clean!",
                )
                stage = 31
            }

            31 -> {
                player(FaceAnim.HALF_GUILTY, "Ewww!")
                stage = 32
            }

            32 -> {
                npc(FaceAnim.HALF_GUILTY, "Oh, just leave me alone.")
                stage = 33
            }

            33 -> end()
            150 ->
                if (removeItem(player, Item(Items.COINS_995, 1))) {
                    addItem(player, Items.CABBAGE_1965, 1)
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "It's a deal. Now, make sure you eat it all up. Cabbage is",
                        "good for you.",
                    )
                    stage = 151
                } else {
                    end()
                    sendMessage(player, "You need one coin to buy a cabbage.")
                }

            151 -> end()
            160 -> {
                npc(FaceAnim.HALF_GUILTY, "Bah! People these days only appreciate junk food.")
                stage = 161
            }

            161 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return VarrockCookDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COOK_5910)
    }
}
