package content.global.activity.ttrail.anagram

import content.global.activity.ttrail.ClueScrollPlugin
import content.global.activity.ttrail.TreasureTrailManager
import content.global.activity.ttrail.puzzle.PuzzleBox
import core.api.addDialogueAction
import core.api.sendItemDialogue
import core.api.sendNPCDialogue
import core.api.ui.setInterfaceText
import core.game.dialogue.FaceAnim
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Plugin
import org.rs.consts.Items

class AnagramCluePlugin(
    private val clue: AnagramClue
) : ClueScrollPlugin(clue.name, clue.clueId, clue.level, 345) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        val clue = arg as? AnagramClue ?: error("Expected AnagramClue")
        return AnagramCluePlugin(clue)
    }

    override fun configure() {

    }

    override fun read(player: Player) {
        repeat(8) { setInterfaceText(player, "", interfaceId, it + 1) }

        val text = buildString {
            append("<br><br>This anagram reveals<br>who to speak to next:<br><br><br>")
            append(clue.anagram.replace("<br>", "<br><br>"))
        }
        setInterfaceText(player, text, interfaceId, 1)
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        val player = e as? Player ?: return false
        val npc = target as? NPC ?: return false
        if (npc.id != clue.npcId || !player.inventory.contains(clue.clueId, 1)) return false

        val puzzle = clue.challenge?.let { PuzzleBox.fromKey(it.toString()) }
        if (puzzle != null) {
            val hasPuzzle = player.inventory.contains(puzzle.item.id, 1)
            val puzzleComplete = PuzzleBox.hasCompletePuzzleBox(player, puzzle.key)

            if (!puzzleComplete) {
                if (!hasPuzzle) {
                    val msg = listOf(
                        "Oh, I have a puzzle for you to solve.",
                        "Oh, I've been expecting you.",
                        "The solving of this puzzle could be the key to your treasure."
                    ).random()
                    sendNPCDialogue(player, clue.npcId, msg, FaceAnim.HALF_GUILTY)
                    player.inventory.add(puzzle.item)
                    addDialogueAction(player) { p, btn ->
                        if (btn > 0) sendItemDialogue(p, puzzle.item, "${npc.name} has given you a puzzle box!")
                    }
                }
                return true
            }

            if (player.inventory.remove(Item(puzzle.item.id, 1))) {
                sendNPCDialogue(player, clue.npcId, "Well done, traveller.", FaceAnim.HALF_GUILTY)
                val manager = TreasureTrailManager.getInstance(player)
                val clueScroll = getClueScrolls()[clue.clueId]
                clueScroll?.let {
                    it.reward(player)
                    val newClue = getClue(it.level)
                    if (manager.isCompleted) {
                        sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
                        manager.clearTrail()
                        newClue?.let { c -> player.inventory.add(c); sendItemDialogue(player, c, "You receive another clue scroll.") }
                    } else {
                        newClue?.let { c -> player.inventory.add(c); sendItemDialogue(player, c, "You receive another clue scroll.") }
                    }
                }
            }
            return true
        }

        return false
    }
}
