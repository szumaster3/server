package content.region.kandarin.handlers.guilds.ranging

import core.api.getStatLevel
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Tower advisor dialogue.
 */
@Initializable
class TowerAdvisorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello there, what do you do here?").also { stage++ }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Hi. We are in charge of this practice area.").also { stage++ }
            1 -> player("This is a practice area?").also { stage++ }
            2 ->
                npc(
                    "Surrounding us are four towers. Each tower contains",
                    "trained archers of a different level. You'll notice",
                    "it's quite a distance, so you'll need a longbow.",
                ).also {
                    stage++
                }
            3 -> {
                val rangeLevel: Int = getStatLevel(player, Skills.RANGE)
                when {
                    // north
                    rangeLevel < 50 ->
                        npc(
                            "As you're not very skilled, I advise you to practice",
                            "on the north tower. That'll provide the best",
                            "challenge for you.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    // east
                    rangeLevel < 60 ->
                        npc(
                            "You appear to be somewhat skilled with a bow, so I",
                            "advise you to practice on the south tower. That'll",
                            "provide the best challenge for you.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    // south
                    rangeLevel < 70 ->
                        npc(
                            "You appear to be fairly skilled with a bow, so I",
                            "advise you to practice on the south tower. That'll",
                            "provide the best challenge for you.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    // west
                    else ->
                        npc(
                            "Looks like you're very skilled, so I advise you to",
                            "practice on the west tower. That'll provide the best",
                            "challenge for you.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.TOWER_ADVISOR_684,
            NPCs.TOWER_ADVISOR_685,
            NPCs.TOWER_ADVISOR_686,
            NPCs.TOWER_ADVISOR_687,
        )
    }
}
