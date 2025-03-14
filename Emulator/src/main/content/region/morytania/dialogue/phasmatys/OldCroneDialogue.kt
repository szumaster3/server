package content.region.morytania.dialogue.phasmatys

import content.data.GameAttributes
import content.region.kandarin.quest.swept.handlers.SweptUtils
import content.region.misthalin.quest.anma.dialogue.OldCroneDialogue
import content.region.morytania.quest.ahoy.dialogue.OldCroneDialogueFile
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class OldCroneDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val animalMagnetism = getQuestStage(player, Quests.ANIMAL_MAGNETISM)
        val sweptAway =
            isQuestComplete(player, Quests.SWEPT_AWAY) &&
                    inInventory(player, Items.BROOMSTICK_14057) &&
                    getDynLevel(player, Skills.MAGIC) >= 53 &&
                    getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE, false)

        npc = args[0] as NPC

        when {
            // If Ghosts Ahoy quest stage >= 3 and Animal Magnetism quest is either incomplete or completed
            getQuestStage(player, Quests.GHOSTS_AHOY) >= 3 && (
                    animalMagnetism < 16 || isQuestComplete(player, Quests.ANIMAL_MAGNETISM)
                    ) -> {
                openDialogue(player, OldCroneDialogueFile())
            }

            // If Animal Magnetism quest stage is between 16 and 18 and Ghosts Ahoy quest stage >= 3
            animalMagnetism in 16..18 && getQuestStage(player, Quests.GHOSTS_AHOY) >= 3 -> {
                options(
                    "Talk about quest. (Animal Magnetism)",
                    "Talk about quest. (Ghosts Ahoy)",
                    "Nevermind."
                ).also { stage = 1 }
            }

            // If Animal Magnetism quest stage is between 16 and 18
            animalMagnetism in 16..18 -> {
                openDialogue(player, OldCroneDialogue())
            }

            // If Swept Away quest conditions are met
            sweptAway -> {
                options(
                    "Hello, old woman.",
                    "Could you enchant this broom for me?"
                ).also { stage = 2 }
            }

            // Default case if none of the above conditions are met
            else -> {
                player("Hello, old woman.")
            }
        }

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(FaceAnim.HALF_GUILTY, "I lived here when this was all just fields, you know.").also {
                    stage = END_DIALOGUE
                }

            1 ->
                when (buttonId) {
                    1 ->
                        openDialogue(
                            player,
                            OldCroneDialogue(),
                        )

                    2 -> openDialogue(player, OldCroneDialogueFile())
                    3 -> end()
                }

            2 ->
                when (buttonId) {
                    1 ->
                        npc(FaceAnim.HALF_GUILTY, "I lived here when this was all just fields, you know.").also {
                            stage = END_DIALOGUE
                        }

                    2 -> player("Could you enchant this broom for me?").also { stage++ }
                }

            3 ->
                npc(
                    "Oh, this must be Maggie's broom. I suppose I could",
                    "enchant it, just this once, for old timers' sake. Just one",
                    "moment...",
                ).also {
                    stage++
                }
            4 -> {
                end()
                lock(player, 1)
                visualize(player, -1, SweptUtils.BROOM_ENCHANTMENT_GFX)
                removeAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE)
                sendDoubleItemDialogue(player, -1, Items.BROOMSTICK_14057, "You receive 7,139 Magic experience.")
                rewardXP(player, Skills.MAGIC, 7139.0)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OLD_CRONE_1695)
    }
}
