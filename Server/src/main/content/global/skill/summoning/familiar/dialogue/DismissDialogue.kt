package content.global.skill.summoning.familiar.dialogue

import content.global.skill.summoning.objectpets.Pet
import core.api.sendOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * The type Dismiss dialogue.
 */
@Initializable
class DismissDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = DismissDialogue(player)

    /**
     * Instantiates a new Dismiss dialogue.
     */
    constructor()

    /**
     * Instantiates a new Dismiss dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        if (player.familiarManager.familiar is Pet) {
            sendOptions(player, "Free pet?", "Yes", "No")
        } else {
            sendOptions(player, "Dismiss Familiar?", "Yes", "No")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (buttonId == 1) {
                    if (player.familiarManager.familiar is Pet) {
                        sendDialogue("Run along; I'm setting you free.")
                        val pet = player.familiarManager.familiar as Pet
                        player.familiarManager.removeDetails(pet.itemIdHash)
                    } else {
                        end()
                    }
                    player.familiarManager.dismiss()
                    stage = 1
                } else if (buttonId == 2) {
                    end()
                }

            1 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("dismiss_dial"))
}
