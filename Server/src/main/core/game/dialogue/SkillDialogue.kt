package core.game.dialogue

import core.api.clockReady
import core.api.sendInputDialogue
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * Handles skill-related interactions.
 *
 * @property player The player.
 */
@Initializable
class SkillDialogue(player: Player? = null) : Dialogue(player) {

    private lateinit var handler: SkillDialogueHandler

    /**
     * Creates a new instance for the given player.
     */
    override fun newInstance(player: Player?): Dialogue = SkillDialogue(player)

    /**
     * Opens the skill dialogue and initializes the handler.
     *
     * @param args args, expects a [SkillDialogueHandler].
     * @return Always true.
     */
    override fun open(vararg args: Any?): Boolean {
        handler = args[0] as SkillDialogueHandler
        handler.display()
        handler.type?.let { player.interfaceManager.openChatbox(it.interfaceId) }
        return true
    }

    /**
     * Handles button interactions, creating items or requesting input amount.
     *
     * @param interfaceId Interface where interaction happened.
     * @param buttonId The button id.
     * @return True if handled.
     */
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val amount = handler.type!!.getAmount(handler, buttonId)
        val index = handler.type!!.getIndex(handler, buttonId)

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
     * Gets handled interface ids.
     */
    override fun getIds(): IntArray = intArrayOf(SkillDialogueHandler.SKILL_DIALOGUE)
}
