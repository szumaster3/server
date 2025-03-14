package content.region.misthalin.dialogue.draynor

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class MartinTheMasterGardenerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options("Skillcape of Farming.", "Talk about farming problems and fairies.", "Vampire slayer")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "What is that cape that you're wearing?")
                        stage = 10
                    }

                    2 -> {
                        npc(FaceAnim.HALF_GUILTY, "I can't chat now, I have too many things to worry", "about.")
                        stage = 20
                    }

                    3 -> {
                        npc(FaceAnim.HALF_GUILTY, "I can't chat now, I have too many things to worry", "about.")
                        stage = 20
                    }
                }

            20 -> end()
            10 -> {
                npc(
                    FaceAnim.HALF_GUILTY,
                    "This is a Skillcape of Farming, isn't it incredbile? It's a",
                    "symbol of my ability as the finest farmer in the land!",
                )
                stage = 11
            }

            11 ->
                if (player.getSkills().getStaticLevel(Skills.FARMING) == 99) {
                    npc(
                        "Ah! I see you have mastered the skill of Farming,",
                        "would you like to purchase a Farming cape for",
                        "a fee of 99000 coins?",
                    )
                    stage = 12
                } else {
                    end()
                }

            12 -> {
                options("Yes, please.", "No, thanks.")
                stage = 13
            }

            13 ->
                when (buttonId) {
                    1 -> {
                        player("Yes, please.")
                        stage = 14
                    }

                    2 -> end()
                }

            14 -> {
                if (purchase(player, Skills.FARMING)) {
                    npc("Have fun with it.")
                    stage = 15
                }
                stage = 15
            }

            15 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MARTIN_THE_MASTER_GARDENER_3299)
    }
}
