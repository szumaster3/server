package core.game.dialogue

import core.api.sendInputDialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * A class representing a skill dialogue in the game, used for handling skill-related interactions
 * with the player, such as creating items or selecting quantities.
 *
 * @property player The player interacting with the skill dialogue.
 */
@Initializable
class SkillDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private lateinit var handler: SkillDialogueHandler

    /**
     * Creates a new instance of the SkillDialogue for the given player.
     *
     * @param player The player to associate with this dialogue instance.
     * @return A new instance of SkillDialogue.
     */
    override fun newInstance(player: Player): Dialogue = SkillDialogue(player)

    /**
     * Opens the skill dialogue for the player, using the provided arguments.
     * This method sets up the handler and displays the appropriate dialogue.
     *
     * @param args The arguments passed to the dialogue, including the handler.
     * @return A boolean indicating if the dialogue was successfully opened.
     */
    override fun open(vararg args: Any): Boolean {
        handler = args[0] as SkillDialogueHandler

        handler.display()
        handler.type?.let { player.interfaceManager.openChatbox(it.interfaceId) }
        return true
    }

    /**
     * Handles the player's interaction with the skill dialogue.
     * This includes processing button presses and determining whether an input
     * amount is needed from the player.
     *
     * @param interfaceId The ID of the interface where the button was pressed.
     * @param buttonId The ID of the button that was pressed.
     * @return A boolean indicating whether the interaction was handled successfully.
     */
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        var amount = handler.type!!.getAmount(handler, buttonId)
        var index = handler.type!!.getIndex(handler, buttonId)

        end()

        return if (amount != -1) {
            handler.create(amount, index)
            true
        } else {
            sendInputDialogue(player, true, "Enter the amount:") { value ->
                if (value is String) {
                    handler.create(value.toInt(), index)
                } else {
                    handler.create(value as Int, index)
                }
            }
            true
        }
    }

    /**
     * Returns the list of interface IDs that this dialogue can handle.
     *
     * @return An array of interface IDs.
     */
    override fun getIds(): IntArray = intArrayOf(SkillDialogueHandler.SKILL_DIALOGUE)
}
