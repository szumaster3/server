package content.global.activity.ttrail.anagram

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScrollPlugin
import content.global.activity.ttrail.TreasureTrailManager
import content.global.activity.ttrail.puzzle.PuzzleBox
import core.api.*
import core.api.ui.setInterfaceText
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Representing an anagram clue scroll.
 *
 * @param anagram    the anagram text hinting the next NPC.
 * @param npcId      the NPC related to the clue.
 * @param challenge  the optional challenge id linked to this clue.
 * @param name       the name of the clue scroll.
 * @param clueId     the unique identifier of the clue.
 * @param level      the difficulty level of the clue.
 */
abstract class AnagramClueScroll(
    name: String?,
    clueId: Int,
    private val anagram: String?,
    val npcId: Int,
    level: ClueLevel?,
    val challenge: Int? = null
) : ClueScrollPlugin(name, clueId, level, Components.TRAIL_MAP09_345) {

    /**
     * Handles player interaction with the NPC for this clue.
     */
    override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
        val player = e!!.asPlayer()
        val npc = target!!.asNpc()
        return handleClue(player, npc)
    }

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

    companion object {
        /**
         * Finds an anagram clue scroll in the player's inventory or active attributes matching the NPC.
         *
         * @param player The player whose inventory to check.
         * @param npc The NPC to match with clue.
         * @return The matching AnagramClueScroll if found, null otherwise.
         */
        fun getClueForNpc(player: Player, npc: NPC): AnagramClueScroll? {
            val fromInventory = player.inventory.toArray()
                .filterNotNull()
                .mapNotNull { getClueScrolls()[it.id] as? AnagramClueScroll }
                .firstOrNull { it.npcId == npc.id }
            if (fromInventory != null) return fromInventory
            val clueId = player.getAttribute("anagram_clue_active", -1)
            val fromAttribute = getClueScrolls()[clueId]
            return if (fromAttribute is AnagramClueScroll && fromAttribute.npcId == npc.id) fromAttribute else null
        }

        /**
         * Handles giving the player either a puzzle box or a challenge clue scroll when interacting with an NPC.
         *
         * @param player The player interacting with the NPC.
         * @param npc The NPC involved in the interaction.
         * @return `true` if a clue or puzzle was given or completed; `false` otherwise.
         */
        fun handleClue(player: Player, npc: NPC): Boolean {
            val anagramClue = getClueForNpc(player, npc) ?: return false

            val facialExpression = if (npc.id in intArrayOf(
                    NPCs.UGLUG_NAR_2039,
                    NPCs.GNOME_COACH_2802,
                    NPCs.GNOME_BALL_REFEREE_635
                )
            ) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_ASKING

            if (freeSlots(player) == 0) {
                sendNPCDialogue(player, npc.id, "Your inventory is full, make some room first.", facialExpression)
                return false
            }

            val challengeId = anagramClue.challenge ?: return false

            val puzzleBox = PuzzleBox.fromItemId(challengeId)

            if (puzzleBox != null) {
                val hasPuzzle = player.inventory.contains(puzzleBox.item.id, 1)
                val isComplete = PuzzleBox.hasCompletePuzzleBox(player, puzzleBox.key)

                if (!hasPuzzle) {
                    if (!player.inventory.remove(Item(anagramClue.clueId, 1))) return false
                    player.setAttribute("anagram_clue_active", anagramClue.clueId)
                    player.inventory.add(puzzleBox.item)
                    val messages = listOf(
                        "Oh, I have a puzzle for you to solve.",
                        "Oh, I've been expecting you.",
                        "The solving of this puzzle could be the key to your treasure."
                    )
                    val message = when (npc.id) {
                        NPCs.RAMARA_DU_CROISSANT_3827 -> "I've ze puzzle for you to solve."
                        NPCs.UGLUG_NAR_2039 -> "You want puzzle?"
                        NPCs.GENERAL_BENTNOZE_4493 -> "Human do puzzle for me!"
                        else -> messages.random()
                    }
                    sendNPCDialogue(player, npc.id, message, facialExpression)

                    addDialogueAction(player) { p, btn ->
                        if (btn > 0) sendItemDialogue(p, puzzleBox.item, "${npc.name} has given you a puzzle box!")
                    }
                    return true
                } else if (isComplete) {
                    if (!player.inventory.remove(puzzleBox.item)) return false
                    removeAttributes(
                        player,
                        "${puzzleBox.key}:puzzle:done",
                        "${puzzleBox.key}:puzzle:data",
                        "anagram_clue_active"
                    )
                    val randomMessage = arrayOf("Here is your reward!", "Well done, traveller.").random()
                    val message = when (npc.id) {
                        NPCs.RAMARA_DU_CROISSANT_3827 -> "Zat's wonderful!"
                        NPCs.UGLUG_NAR_2039 -> "Dere you go!"
                        NPCs.GENERAL_BENTNOZE_4493 -> "Thank you human!"
                        else -> randomMessage
                    }
                    sendNPCDialogue(player, npc.id, message, facialExpression)
                    addDialogueAction(player) { p, btn ->
                        if (btn > 0) {
                            val manager = TreasureTrailManager.getInstance(p)
                            val clueScroll = getClueScrolls()[anagramClue.clueId]
                            clueScroll?.reward(p)

                            if (manager.isCompleted) {
                                sendItemDialogue(p, Items.CASKET_405, "You've found a casket!")
                                manager.clearTrail()
                            } else {
                                val newClue = getClue(clueScroll?.level)
                                if (newClue != null) {
                                    sendItemDialogue(p, newClue, "You receive another clue scroll.")
                                    p.inventory.add(newClue)
                                }
                            }
                        }
                    }
                    return true
                }
                return false
            } else {
                if (!player.inventory.contains(challengeId, 1)) {
                        openDialogue(player, object : DialogueFile() {
                            override fun handle(componentID: Int, buttonID: Int) {
                                when (stage) {
                                    0 -> {
                                        sendNPCDialogue(player, npc.id, "Ah! Here you go!", facialExpression)
                                        stage = 1
                                    }

                                    1 -> {
                                        player("What?")
                                        stage++
                                    }

                                    2 -> {
                                        sendNPCDialogue(
                                            player,
                                            npc.id,
                                            "I need you to answer this for me.",
                                            facialExpression
                                        )
                                        stage++
                                    }

                                    3 -> {
                                        end()
                                        player.setAttribute("anagram_clue_active", challengeId)
                                        sendItemDialogue(
                                            player,
                                            Item(challengeId, 1),
                                            "${npc.name} has given you a challenge scroll!"
                                        )
                                        player.inventory.add(Item(challengeId, 1))
                                    }
                                }
                            }
                        })
                        return true
                }
            }

            return false
        }
    }
}