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
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents an anagram clue scroll.
 * @author szu
 */
abstract class AnagramScroll(
    name: String?,
    clueId: Int,
    private val anagram: String?,
    val npcId: Int,
    level: ClueLevel?,
    val challenge: Int? = null
) : ClueScroll(name, clueId, level, Components.TRAIL_MAP09_345) {

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        val player = e.asPlayer()
        val npc = target.asNpc()
        return handleClue(player, npc)
    }

    override fun read(player: Player) {
        repeat(8) { sendString(player, "", interfaceId, it + 1) }

        super.read(player)
        sendString(
            player,
            "<br><br><br><br>This anagram reveals<br>who to speak to next:<br><br><br>$anagram",
            interfaceId,
            1
        )
    }

    companion object {
        /**
         * Gets the active anagram scroll for the given NPC.
         *
         * First checks the player's inventory, then active attributes.
         *
         * @param player The player to check.
         * @param npc The NPC being interacted with.
         * @return The matching [AnagramScroll].
         */
        fun getClueForNpc(player: Player, npc: NPC): AnagramScroll? {
            val fromInventory = player.inventory.toArray()
                .filterNotNull()
                .mapNotNull { getClueScrolls()[it.id] as? AnagramScroll }
                .firstOrNull { it.npcId == npc.id }

            if (fromInventory != null) return fromInventory

            val clueId = player.getAttribute("anagram_clue_active", -1)
            val fromAttr = getClueScrolls()[clueId]

            return if (fromAttr is AnagramScroll && fromAttr.npcId == npc.id) fromAttr else null
        }

        /**
         * Handles interaction logic when a player talks to a correct NPC.
         *
         * @param player The player.
         * @param npc The NPC being interacted with.
         * @return true if handled, false otherwise.
         */
        fun handleClue(player: Player, npc: NPC): Boolean {
            val clue = getClueForNpc(player, npc) ?: return false

            val facialExpression = when (npc.id) {
                NPCs.UGLUG_NAR_2039, NPCs.GNOME_COACH_2802, NPCs.GNOME_BALL_REFEREE_635 -> FaceAnim.OLD_DEFAULT
                else -> FaceAnim.HALF_ASKING
            }

            if (freeSlots(player) == 0) {
                sendNPCDialogue(player, npc.id, "Your inventory is full, make some room first.", facialExpression)
                return false
            }

            val challengeId = clue.challenge ?: return false
            val puzzle = PuzzleBox.fromItemId(challengeId)

            return if (puzzle != null) {
                handlePuzzleBox(player, npc, clue, puzzle, facialExpression)
            } else {
                handleChallengeScroll(player, npc, clue, facialExpression)
            }
        }

        /**
         * Handles puzzle box interaction logic.
         * Gives the player a puzzle box if they don't have it,
         * or processes the reward if the puzzle is completed.
         */
        private fun handlePuzzleBox(
            player: Player,
            npc: NPC,
            clue: AnagramScroll,
            puzzle: PuzzleBox,
            facial: FaceAnim
        ): Boolean {
            val hasPuzzle = player.inventory.contains(puzzle.item.id, 1)
            val isComplete = PuzzleBox.hasCompletePuzzleBox(player, puzzle.key)

            if (hasPuzzle && !isComplete) return false

            if (!hasPuzzle) {
                if (!player.inventory.remove(Item(clue.clueId))) return false
                player.setAttribute("anagram_clue_active", clue.clueId)
                player.inventory.add(puzzle.item)

                val message = getPuzzleDialogue(npc.id)
                sendNPCDialogue(player, npc.id, message, facial)

                addDialogueAction(player) { p, _ ->
                    sendItemDialogue(p, puzzle.item, "${npc.name} has given you a puzzle box!")
                }
                return true
            }

            // If puzzle is completed.
            if (!player.inventory.remove(puzzle.item)) return false

            removeAttributes(
                player,
                "${puzzle.key}:puzzle:done",
                "${puzzle.key}:puzzle:data",
                "anagram_clue_active"
            )

            val message = getPuzzleCompleteDialogue(npc.id)
            sendNPCDialogue(player, npc.id, message, facial)

            addDialogueAction(player) { p, _ ->
                val manager = TreasureTrailManager.getInstance(p)
                val clueScroll = getClueScrolls()[clue.clueId]
                clueScroll?.reward(p)

                if (manager.isCompleted) {
                    sendItemDialogue(p, Items.CASKET_405, "You've found a casket!")
                    manager.clearTrail()
                } else {
                    clueScroll?.level?.let { getClue(it) }?.let { newClue ->
                        sendItemDialogue(p, newClue, "You receive another clue scroll.")
                        addItem(p, newClue.id, 1)
                    }
                }
            }

            return true
        }

        /**
         * Handles logic when the clue gives a challenge scroll instead of a puzzle box.
         * Displays a dialogue and gives the player the challenge item.
         */
        private fun handleChallengeScroll(
            player: Player,
            npc: NPC,
            clue: AnagramScroll,
            facial: FaceAnim
        ): Boolean {
            val challengeId = clue.challenge ?: return false
            if (player.inventory.contains(challengeId, 1)) return false

            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> {
                            sendNPCDialogue(player, npc.id, "Ah! Here you go!", facial)
                            stage++
                        }
                        1 -> {
                            player("What?")
                            stage++
                        }
                        2 -> {
                            sendNPCDialogue(player, npc.id, "I need you to answer this for me.", facial)
                            stage++
                        }
                        3 -> {
                            end()
                            player.setAttribute("anagram_clue_active", challengeId)
                            player.inventory.add(Item(challengeId))
                            val name = getItemName(challengeId).lowercase()
                            sendItemDialogue(player, Item(challengeId), "${npc.name} has given you a $name!")
                        }
                    }
                }
            })

            return true
        }

        private fun getPuzzleDialogue(npcId: Int): String = when (npcId) {
            NPCs.RAMARA_DU_CROISSANT_3827 -> "I've ze puzzle for you to solve."
            NPCs.UGLUG_NAR_2039 -> "You want puzzle?"
            NPCs.GENERAL_BENTNOZE_4493 -> "Human do puzzle for me!"
            else -> listOf(
                "Oh, I have a puzzle for you to solve.",
                "Oh, I've been expecting you.",
                "The solving of this puzzle could be the key to your treasure."
            ).random()
        }

        /**
         * Gets the appropriate dialogue after completing a puzzle box.
         *
         * @param npcId The NPC id.
         * @return A dialogue string.
         */
        private fun getPuzzleCompleteDialogue(npcId: Int): String = when (npcId) {
            NPCs.RAMARA_DU_CROISSANT_3827 -> "Zat's wonderful!"
            NPCs.UGLUG_NAR_2039 -> "Dere you go!"
            NPCs.GENERAL_BENTNOZE_4493 -> "Thank you human!"
            else -> listOf("Here is your reward!", "Well done, traveller.").random()
        }
    }
}