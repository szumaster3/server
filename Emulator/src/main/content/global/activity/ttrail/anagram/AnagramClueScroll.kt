package content.global.activity.ttrail.anagram

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScrollPlugin
import content.global.activity.ttrail.TreasureTrailManager
import content.global.activity.ttrail.puzzle.PuzzleBox
import core.api.*
import core.api.ui.setInterfaceText
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Abstract class representing an anagram clue scroll in a treasure trail.
 */
abstract class AnagramClueScroll(
    name: String?,
    clueId: Int,
    val anagram: String?,
    val npcId: Int,
    level: ClueLevel?,
    val challenge: Int? = null
) : ClueScrollPlugin(name, clueId, level, Components.TRAIL_MAP09_345) {
    /**
     * Shows clue text to the player.
     *
     * @param player The player reading the clue.
     */
    override fun read(player: Player) {
        repeat(8) { index ->
            setInterfaceText(player, "", interfaceId, index + 1)
        }

        super.read(player)
        setInterfaceText(
            player,
            "<br><br><br><br>This anagram reveals<br>who to speak to next:<br><br><br>$anagram",
            interfaceId,
            1
        )
    }

    /**
     * Handles interaction when the player talks to an NPC regarding the puzzle.
     *
     * @param player The player interacting with the NPC.
     * @param npc The NPC the player is talking to.
     */
    fun getPuzzle(player: Player, npc: NPC) {
        val puzzle = PuzzleBox.fromItemId(challenge ?: return) ?: return
        val isComplete = PuzzleBox.hasCompletePuzzleBox(player, puzzle.key)

        when {
            !player.inventory.contains(puzzle.item.id, 1) -> givePuzzleBox(player, npc, puzzle)
            isComplete -> completePuzzle(player, npc, puzzle)
            else -> return
        }
    }

    /**
     * Gives the puzzle box to the player.
     */
    private fun givePuzzleBox(player: Player, npc: NPC, puzzle: PuzzleBox) {
        if (!player.inventory.remove(Item(clueId, 1))) return

        player.setAttribute("anagram_clue_active", clueId)
        val messages = listOf(
            "Oh, I have a puzzle for you to solve.",
            "Oh, I've been expecting you.",
            "The solving of this puzzle could be the key to your treasure."
        )
        sendNPCDialogue(player, npcId, messages.random(), FaceAnim.HALF_GUILTY)
        player.inventory.add(puzzle.item)

        addDialogueAction(player) { p, btn ->
            if (btn > 0) {
                sendItemDialogue(p, puzzle.item, "${npc.name} has given you a puzzle box!")
            }
        }
    }

    /**
     * Completes the puzzle and rewards the player.
     */
    private fun completePuzzle(player: Player, npc: NPC, puzzle: PuzzleBox) {
        if (!player.inventory.remove(puzzle.item)) return

        removeAttributes(player, "${puzzle.key}:puzzle:done", "${puzzle.key}:puzzle:data", "anagram_clue_active")
        sendNPCDialogue(player, npcId, "Well done, traveller.", FaceAnim.HALF_GUILTY)

        addDialogueAction(player) { _, _ ->
            val manager = TreasureTrailManager.getInstance(player)
            val clueScroll = getClueScrolls()[clueId]
            clueScroll?.reward(player)

            if (manager.isCompleted) {
                sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
                manager.clearTrail()
            } else {
                val newClue = getClue(clueScroll?.level)
                if (newClue != null) {
                    sendItemDialogue(player, newClue, "You receive another clue scroll.")
                    player.inventory.add(newClue)
                }
            }
        }
    }

    companion object {
        /**
         * Finds the first anagram clue scroll in the player inventory that is associated with the given NPC.
         *
         * @param player The player whose inventory will be searched.
         * @param npc The NPC to match the clue scroll against.
         * @return The matching [AnagramClueScroll] if found; otherwise, `null`.
         */
        fun getClueForNpc(player: Player, npc: NPC): AnagramClueScroll? {
            // Inventory
            val fromInventory = player.inventory.toArray()
                .filterNotNull()
                .mapNotNull { getClueScrolls()[it.id] as? AnagramClueScroll }
                .firstOrNull { it.npcId == npc.id }

            if (fromInventory != null) return fromInventory

            // Attributes
            val clueId = player.getAttribute("anagram_clue_active", -1)
            val fromAttribute = getClueScrolls()[clueId]
            return if (fromAttribute is AnagramClueScroll && fromAttribute.npcId == npc.id) {
                fromAttribute
            } else null
        }
    }
}
