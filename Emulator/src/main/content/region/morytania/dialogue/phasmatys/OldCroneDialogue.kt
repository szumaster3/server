package content.region.morytania.dialogue.phasmatys

import content.data.GameAttributes
import content.region.kandarin.quest.swept.handlers.SweptUtils
import content.region.misthalin.quest.anma.dialogue.OldCroneAnimalDialogue
import content.region.morytania.quest.ahoy.dialogue.OldCroneAhoyDialogueFile
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

/**
 * Represents the Old Crone dialogue.
 *
 * Relations:
 * - [Animal Magnetism][content.region.misthalin.quest.anma.AnimalMagnetism]
 * - [Swept away][content.region.kandarin.quest.swept.SweptAway]
 * - [Ghosts Ahoy][content.region.morytania.quest.ahoy.GhostsAhoy]
 */
@Initializable
class OldCroneDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        val npc = args.getOrNull(0) as? NPC ?: return false
        this.npc = npc

        val animalMagnetism = getQuestStage(player, Quests.ANIMAL_MAGNETISM)
        val ghostsAhoy = getQuestStage(player, Quests.GHOSTS_AHOY)
        val magicLevel = getDynLevel(player, Skills.MAGIC)

        val sweptAwayComplete = isQuestComplete(player, Quests.SWEPT_AWAY)
        val hasBroomstick = inInventory(player, Items.BROOMSTICK_14057)
        val labelsComplete = getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE, false)

        val sweptAwayReady = sweptAwayComplete && hasBroomstick && magicLevel >= MIN_MAGIC_LEVEL_FOR_BROOM && labelsComplete

        when {
            // Ghosts Ahoy >= 3 and Animal Magnetism started or complete
            ghostsAhoy >= QUEST_STAGE_GHOSTS_AHOY_REQUIRED &&
                    (animalMagnetism < QUEST_COMPLETE || isQuestComplete(player, Quests.ANIMAL_MAGNETISM)) -> {
                openDialogue(player, OldCroneAhoyDialogueFile())
            }

            // Both quests in progress
            animalMagnetism in ANIMAL_MAGNETISM_ACTIVE_RANGE && ghostsAhoy >= QUEST_STAGE_GHOSTS_AHOY_REQUIRED -> {
                options(
                    "Talk about quest. (Animal Magnetism)",
                    "Talk about quest. (Ghosts Ahoy)",
                    "Nevermind."
                ).also { stage = 1 }
            }

            // Only Animal Magnetism in progress
            animalMagnetism in ANIMAL_MAGNETISM_ACTIVE_RANGE -> {
                openDialogue(player, OldCroneAnimalDialogue())
            }

            // Swept Away ready
            sweptAwayReady -> {
                options(
                    "Hello, old woman.",
                    "Could you enchant this broom for me?"
                ).also { stage = 2 }
            }

            // Default dialogue
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
            0 -> npc(FaceAnim.HALF_GUILTY, "I lived here when this was all just fields, you know.").also {
                stage = END_DIALOGUE
            }

            1 -> when (buttonId) {
                1 -> end().also {
                    openDialogue(
                        player,
                        OldCroneAnimalDialogue(),
                    )
                }

                2 -> end().also {
                    openDialogue(player, OldCroneAhoyDialogueFile())
                }

                3 -> end()
            }

            2 -> when (buttonId) {
                1 -> npc(FaceAnim.HALF_GUILTY, "I lived here when this was all just fields, you know.").also { stage = END_DIALOGUE }

                2 -> player("Could you enchant this broom for me?").also { stage++ }
            }

            3 -> npc(
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

    companion object {
        private const val QUEST_STAGE_GHOSTS_AHOY_REQUIRED = 3
        private const val QUEST_COMPLETE = 16
        private val ANIMAL_MAGNETISM_ACTIVE_RANGE = 16..18
        private const val MIN_MAGIC_LEVEL_FOR_BROOM = 53
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OLD_CRONE_1695)
}
