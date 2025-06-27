package core.game.dialogue

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.START_DIALOGUE

/**
 * Placeholder dialogue with optional [DialogueFile].
 *
 * @property player Player involved in the dialogue.
 * @property file Optional associated dialogue file.
 */
class EmptyDialogue(player: Player? = null, val file: DialogueFile?, ) : Dialogue(player) {

    /**
     * Creates a new instance for the given player.
     */
    override fun newInstance(player: Player?): Dialogue = EmptyDialogue(player, null)

    /**
     * Opens the dialogue, optionally setting an NPC and loading a file.
     *
     * @param args Optional arguments, first may be an [NPC].
     * @return Always returns true.
     */
    override fun open(vararg args: Any?): Boolean {
        if (args.isNotEmpty() && args[0] is NPC) {
            npc = args[0] as NPC
        }
        stage = START_DIALOGUE
        loadFile(file)
        interpreter!!.handle(0, 0)
        return true
    }

    /**
     * Handles interaction; always returns true as no action is needed.
     */
    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    /**
     * Gets interface ids this dialogue handles; none in this case.
     */
    override fun getIds(): IntArray = intArrayOf(Integer.MAX_VALUE)
}
