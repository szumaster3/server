package core.game.dialogue

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.START_DIALOGUE

/**
 * A dialogue class used as a placeholder or empty dialogue.
 * This class doesn't display any actual dialogue but can be used for
 * managing the flow of the dialogue system, such as initiating dialogue stages.
 *
 * @property player The player interacting with the dialogue.
 * @property file The optional [DialogueFile] associated with the dialogue.
 */
class EmptyDialogue(
    player: Player? = null,
    val file: DialogueFile?,
) : Dialogue(player) {

    /**
     * Creates a new instance of the EmptyDialogue for the given player.
     *
     * @param player The player to associate with this dialogue instance.
     * @return A new instance of EmptyDialogue.
     */
    override fun newInstance(player: Player?): Dialogue = EmptyDialogue(player, null)

    /**
     * Opens the empty dialogue, setting up necessary properties such as the NPC
     * and stage. It also loads the associated [DialogueFile] if provided.
     *
     * @param args The arguments passed to open the dialogue, potentially including an NPC.
     * @return A boolean indicating whether the dialogue was successfully opened.
     */
    override fun open(vararg args: Any?): Boolean {
        // Check if the first argument is an NPC and set it accordingly.
        if (args.isNotEmpty() && args[0] is NPC) {
            npc = args[0] as NPC
        }

        // Set the stage to the start dialogue stage.
        stage = START_DIALOGUE

        // Load the provided dialogue file if available.
        loadFile(file)

        // Handle the first stage of the dialogue.
        interpreter.handle(0, 0)
        return true
    }

    /**
     * Handles the player's interaction with the dialogue. Since this is an empty dialogue,
     * it simply returns true without performing any actions.
     *
     * @param interfaceId The ID of the interface where the interaction occurred.
     * @param buttonId The ID of the button that was pressed by the player.
     * @return A boolean indicating whether the interaction was handled successfully.
     */
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean = true

    /**
     * Returns the list of interface IDs that this dialogue can handle.
     * Since this is an empty dialogue, it returns a special value indicating that it
     * doesn't handle any specific interfaces.
     *
     * @return An array of interface IDs, in this case containing only [Integer.MAX_VALUE].
     */
    override fun getIds(): IntArray = intArrayOf(Integer.MAX_VALUE)
}
