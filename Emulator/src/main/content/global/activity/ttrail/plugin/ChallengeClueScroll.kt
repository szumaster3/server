package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScroll
import content.global.activity.ttrail.TreasureTrailManager
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents a challenge clue scroll.
 * @author szu
 */
abstract class ChallengeClueScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    val question: String?,
    val npc: Int?,
    val answer: Int?,
    vararg borders: ZoneBorders,
) : ClueScroll(name, clueId, level, Components.TRAIL_MAP09_345, borders) {

    /**
     * Shows clue question to the player.
     *
     * @param player The player reading the clue.
     */
    override fun read(player: Player) {
        for (i in 1..8) {
            sendString(player, "", interfaceId, i)
        }
        super.read(player)
        sendString(player,"<br><br><br><br><br>" + question?.replace("<br>", "<br><br>"), interfaceId, 1)
    }

    /**
     * Handles player interaction with the NPC for this clue.
     */
    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        val player = e?.asPlayer() ?: return false
        val npc = target?.asNpc() ?: return false

        val clueScroll = getClueForNpc(player, npc)
        if (clueScroll != null) {
            ChallengeDialogue(player, npc, clueScroll)
            return true
        }

        return false
    }

    companion object {
        /**
         * Gets the challenge clue scroll item in inventory for the given NPC.
         */
        fun getClueForNpc(player: Player, npc: NPC): ChallengeClueScroll? =
            player.inventory.toArray()
                .filterNotNull()
                .mapNotNull { getClueScrolls()[it.id] }
                .filterIsInstance<ChallengeClueScroll>()
                .firstOrNull { it.npc == npc.id }

        /**
         * Opens dialogue for player answering the clue question with the NPC.
         */
        class ChallengeDialogue(
            private val p: Player,
            private val scrollNpc: NPC,
            private val clue: ChallengeClueScroll
        ) : DialogueFile() {

            private val facialExpression = if (scrollNpc.id in arrayOf(
                    NPCs.UGLUG_NAR_2039,
                    NPCs.GNOME_COACH_2802,
                    NPCs.GNOME_BALL_REFEREE_635,
                    NPCs.GNOME_TRAINER_162
                )
            ) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_ASKING

            override fun handle(componentID: Int, buttonID: Int) {
                val manager = TreasureTrailManager.getInstance(p)
                val clueScroll = getClueScrolls()[clue.clueId]
                val anagramClue = AnagramScroll.getClueForNpc(p, scrollNpc)

                when (stage) {
                    0 -> {
                        npc(scrollNpc.id, facialExpression, "Please enter the answer to the question.")
                        stage = 1
                    }
                    1 -> {
                        sendInputDialogue(p, numeric = true, prompt = "Enter your answer") { input ->
                            val userAnswer = input as? Int
                            if (userAnswer == null || userAnswer != clue.answer) {
                                npc(scrollNpc.id, "That isn't right, keep trying.")
                                end()
                                return@sendInputDialogue
                            }

                            if (freeSlots(p) == 0) {
                                sendNPCDialogue(p, scrollNpc.id, "Your inventory is full, make some room first.", facialExpression)
                                end()
                                return@sendInputDialogue
                            }

                            npc(scrollNpc.id, arrayOf("Here is your reward!", "Spot on!").random())
                            removeItem(p, clue.clueId)
                            anagramClue?.let { removeItem(p, it.clueId) }

                            removeAttributes(p, "anagram_clue_active")
                            clue.reward(p)

                            stage = 2
                        }
                    }
                    2 -> {
                        end()
                        if (manager.isCompleted) {
                            sendItemDialogue(p, Items.CASKET_405, "You've found a casket!")
                            manager.clearTrail()
                        } else {
                            val newClue = clue.level?.let { getClue(it) }
                            if (newClue != null) {
                                sendItemDialogue(p, newClue, "You receive another clue scroll.")
                                addItem(p, newClue.id, 1)
                            }
                        }
                    }
                }
            }
        }
    }
}
