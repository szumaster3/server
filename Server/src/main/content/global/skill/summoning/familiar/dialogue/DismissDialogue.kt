package content.global.skill.summoning.familiar.dialogue

import content.global.skill.summoning.pets.Pet
import core.api.sendOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * Represents the Dismiss interaction dialogue for pets and familiars.
 */
@Initializable
class DismissDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = DismissDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        branch = if (player.familiarManager.familiar is Pet) 0 else 1
        stage = 0

        when (branch) {
            0 -> sendOptions(player, "Free pet?", "Yes", "No")
            1 -> sendOptions(player, "Dismiss Familiar?", "Yes", "No")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            /*
             * Pet.
             */

            0 -> {
                when (stage) {
                    0 -> {
                        if (buttonId == 1) {
                            sendDialogue("Run along; I'm setting you free.")
                            val pet = player.familiarManager.familiar as Pet
                            player.familiarManager.removeDetails(pet.itemIdHash)
                            player.familiarManager.dismiss()
                            stage = 1
                        } else if (buttonId == 2) {
                            end()
                        }
                    }
                    1 -> end()
                }
            }

            /*
             * Familiar.
             */
            1 -> {
                when (stage) {
                    0 -> {
                        if (buttonId == 1) {
                            player.familiarManager.dismiss()
                            stage = 1
                        } else if (buttonId == 2) {
                            end()
                        }
                    }
                    1 -> end()
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("dismiss_dial"))
}
