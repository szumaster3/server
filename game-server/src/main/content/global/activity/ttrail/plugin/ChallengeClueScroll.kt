package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScroll
import content.global.activity.ttrail.TreasureTrailManager
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

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
            player.packetDispatch.sendString("", interfaceId, i)
        }
        super.read(player)
        player.packetDispatch.sendString("<br><br><br><br><br>" + question?.replace("<br>", "<br><br>"), interfaceId, 1)
    }

    /**
     * Handles player interaction with the NPC for this clue.
     */
    override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
        val player = e?.asPlayer() ?: return false
        val npc = target?.asNpc() ?: return false

        val clueScroll = getClueForNpc(player, npc)
        if (clueScroll != null) {
            handleNPC(player, npc, clueScroll)
            return true
        }

        return false
    }

    companion object {
        /**
         * Finds a challenge clue scroll in the player's inventory for the given NPC.
         */
        fun getClueForNpc(player: Player, npc: NPC): ChallengeClueScroll? =
            player.inventory.toArray().filterNotNull().mapNotNull { getClueScrolls()[it.id] }
                .filterIsInstance<ChallengeClueScroll>().firstOrNull { it.npc == npc.id }

        /**
         * Opens dialogue for player answering the clue question with the NPC.
         */
        fun handleNPC(player: Player, npc: NPC, clue: ChallengeClueScroll) {
            dialogue(player) {
                val facialExpression = if (npc.id in intArrayOf(
                        NPCs.UGLUG_NAR_2039,
                        NPCs.GNOME_COACH_2802,
                        NPCs.GNOME_BALL_REFEREE_635,
                        NPCs.GNOME_TRAINER_162
                    )
                ) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_ASKING

                val randomAnswer = arrayOf("Here is your reward!", "Spot on!").random()

                npc(npc.id, facialExpression, "Please enter the answer to the question.")

                input(numeric = true, prompt = "Enter your answer") { value, resume ->
                    val answer = value as? Int
                    if (answer == null || answer != clue.answer) {
                        npc(npc.id, "That isn't right, keep trying.")
                        resume(false)
                        return@input
                    }

                    if (freeSlots(player) == 0) {
                        sendNPCDialogue(player, npc.id, "Your inventory is full, make some room first.", facialExpression)
                        resume(false)
                        return@input
                    }

                    npc(npc.id, randomAnswer)
                    resume(true)

                    val manager = TreasureTrailManager.getInstance(player)
                    val clueScroll = getClueScrolls()[clue.clueId]
                    val anagramClue = AnagramScroll.getClueForNpc(player, npc)

                    if (clueScroll != null) {
                        removeItem(player, clueScroll.clueId)
                        anagramClue?.let { removeItem(player, it.clueId) }

                        removeAttributes(player, "anagram_clue_active")
                        clueScroll.reward(player)

                        if (manager.isCompleted) {
                            sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
                            manager.clearTrail()
                        } else {
                            val newClue = clueScroll.level?.let { getClue(it) }
                            if (newClue != null) {
                                sendItemDialogue(player, newClue, "You receive another clue scroll.")
                                addItem(player, newClue.id, 1)
                            }
                        }
                    }
                }
            }
        }
    }
}
