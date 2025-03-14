package content.region.kandarin.handlers.guilds.fishing

import core.game.dialogue.Dialogue
import core.game.global.Skillcape
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Master Fisher dialogue.
 */
@Initializable
class MasterFisherDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        if (!Skillcape.isMaster(player, Skills.FISHING)) {
            npc("Hello, I'm afraid only the top fishers are allowed to use", "our premier fishing facilities.")
        } else {
            npc(
                "Hello, only the top fishers are allowed to use",
                "our premier fishing facilities and you seem",
                "to meet the criteria. Enjoy!",
            )
        }
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (Skillcape.isMaster(player, Skills.FISHING)) {
                    player("Can I buy a Skillcape of Fishing?").also { stage = 3 }
                } else {
                    player("Can you tell me about that skillcape you're wearing?").also { stage++ }
                }
            }
            1 ->
                npc(
                    "I'm happy to, my friend. This beautiful cape was",
                    "presented to me in recognition of my skills and",
                    "experience as a fisherman and I was asked to be the",
                    "head of this guild at the same time. As the best",
                ).also {
                    stage++
                }
            2 ->
                npc(
                    "fisherman in the guild it is my duty to control who has",
                    "access to the guild and to say who can buy similar",
                    "skillcapes.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            3 -> npc("Certainly! Right when you pay me 99000 coins.").also { stage++ }
            4 -> options("Okay, here you go.", "No, thanks.").also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> player("Okay, here you go.").also { stage++ }
                    2 -> player("No, thanks.").also { stage = END_DIALOGUE }
                }

            6 -> {
                if (Skillcape.purchase(player, Skills.FISHING)) {
                    npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MASTER_FISHER_308)
    }
}
