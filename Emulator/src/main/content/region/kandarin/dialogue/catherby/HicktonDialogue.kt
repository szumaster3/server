package content.region.kandarin.dialogue.catherby

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class HicktonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Welcome to Hickton's Archery Emporium. Do you", "want to see my wares?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                stage =
                    if (isMaster(player, Skills.FLETCHING)) {
                        options(
                            "Can I buy a Skillcape of Fletching?",
                            "Yes, please.",
                            "No, I prefer to bash things close up.",
                        )
                        100
                    } else {
                        options("Yes, please.", "No, I prefer to bash things close up.")
                        1
                    }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        npc.openShop(player)
                    }

                    2 -> {
                        player(FaceAnim.EVIL_LAUGH, "No, I prefer to bash things close up.")
                        stage = 20
                    }
                }

            20 -> end()
            100 ->
                when (buttonId) {
                    1 -> {
                        player("Can I buy a Skillcape of Fletching?")
                        stage = -99
                    }

                    2 -> {
                        end()
                        npc.openShop(player)
                    }

                    3 -> {
                        player(FaceAnim.EVIL_LAUGH, "No, I prefer to bash things close up.")
                        stage = 20
                    }
                }

            -99 -> {
                npc("You will have to pay a fee of 99,000 gp.")
                stage = 101
            }

            101 -> {
                options("Yes, here you go.", "No, thanks.")
                stage = 102
            }

            102 ->
                when (buttonId) {
                    1 -> {
                        player("Yes, here you go.")
                        stage = 103
                    }

                    2 -> end()
                }

            103 -> {
                if (purchase(player, Skills.FLETCHING)) {
                    npc("There you go! Enjoy.")
                }
                stage = 104
            }

            104 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HicktonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HICKTON_575)
    }
}
