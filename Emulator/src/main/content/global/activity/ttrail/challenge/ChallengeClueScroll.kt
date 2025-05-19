package content.global.activity.ttrail.challenge

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScrollPlugin
import content.global.activity.ttrail.TreasureTrailManager
import content.global.activity.ttrail.anagram.AnagramClueScroll
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Representing a challenge clue scroll.
 *
 * @param question  the question associated with the clue.
 * @param npc       the NPC related to the clue.
 * @param answer    the answer to the clue.
 * @param name      the name of the clue scroll.
 * @param clueId    the unique identifier of the clue.
 * @param level     the difficulty level of the clue.
 * @param borders   the zone borders where the clue is located.
 */
abstract class ChallengeClueScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    val question: String?,
    val npc: Int?,
    val answer: Int?,
    vararg borders: ZoneBorders,
) : ClueScrollPlugin(name, clueId, level, Components.TRAIL_MAP09_345, *borders) {

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
        player.packetDispatch.sendString(question?.replace("<br>", "<br><br>"), interfaceId, 1)
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
            player.inventory.toArray()
                .filterNotNull()
                .mapNotNull { getClueScrolls()[it.id] }
                .filterIsInstance<ChallengeClueScroll>()
                .firstOrNull { it.npc == npc.id }

        /**
         * Opens dialogue for player answering the clue question with the NPC.
         */
        fun handleNPC(player: Player, npc: NPC, clue: ChallengeClueScroll) {
            openDialogue(
                player,
                object : DialogueFile() {
                    private val facialExpression = if (npc.id in intArrayOf(NPCs.UGLUG_NAR_2039, NPCs.GNOME_COACH_2802, NPCs.GNOME_BALL_REFEREE_635)) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_ASKING
                    private val randomAnswer = arrayOf("Here is your reward!", "Spot on!").random()

                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            0 -> {
                                sendNPCDialogue(player, npc.id, "Please enter the answer to the question.", facialExpression)
                                stage = 1
                            }
                            1 -> {
                                sendInputDialogue(player, true, "Enter amount:") { value: Any ->
                                    val answer = (value as? Int) ?: return@sendInputDialogue
                                    if (answer == clue.answer) {
                                        sendNPCDialogue(player, npc.id, randomAnswer, facialExpression)
                                        stage = 2
                                    } else {
                                        sendNPCDialogue(player, npc.id, "That's not the correct answer.", facialExpression)
                                        stage = END_DIALOGUE
                                    }
                                }
                            }
                            2 -> {
                                end()
                                if (freeSlots(player) == 0) {
                                    sendNPCDialogue(player, npc.id, "Your inventory is full, make some room first.", facialExpression)
                                    return
                                }
                                val manager = TreasureTrailManager.getInstance(player)
                                val clueScroll = getClueScrolls()[clue.clueId]
                                val anagramClue = AnagramClueScroll.getClueForNpc(player, npc)
                                if (clueScroll != null && removeItem(player, clueScroll.clueId) && removeItem(player, anagramClue?.clueId)) {
                                    removeAttributes(player, "anagram_clue_active")
                                    clueScroll.reward(player)
                                    if (manager.isCompleted) {
                                        sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
                                        manager.clearTrail()
                                    } else {
                                        val newClue = getClue(clueScroll.level)
                                        if (newClue != null) {
                                            sendItemDialogue(player, newClue, "You receive another clue scroll.")
                                            player.inventory.add(newClue)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}
