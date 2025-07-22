package content.global.skill.summoning.familiar.dialogue

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.RemoteViewer
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * The type Remote view dialogue.
 */
@Initializable
class RemoteViewDialogue : Dialogue {
    private var familiar: Familiar? = null

    override fun newInstance(player: Player?): Dialogue = RemoteViewDialogue(player)

    /**
     * Instantiates a new Remote view dialogue.
     */
    constructor()

    /**
     * Instantiates a new Remote view dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        familiar = args[0] as Familiar
        options("North", "East", "South", "West", "Straight up")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        end()
        RemoteViewer
            .create(
                player,
                familiar,
                familiar!!.viewAnimation,
                RemoteViewer.ViewType.values()[buttonId - 1],
            ).startViewing()
        return true
    }

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey(RemoteViewer.DIALOGUE_NAME))
}
