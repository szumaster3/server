package core.game.dialogue

import core.api.sendInputDialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class SkillDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private lateinit var handler: SkillDialogueHandler

    override fun newInstance(player: Player): Dialogue {
        return SkillDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        handler = args[0] as SkillDialogueHandler

        handler.display()
        handler.type?.let { player.interfaceManager.openChatbox(it.interfaceId) }
        return true
    }

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

    override fun getIds(): IntArray {
        return intArrayOf(SkillDialogueHandler.SKILL_DIALOGUE)
    }
}
