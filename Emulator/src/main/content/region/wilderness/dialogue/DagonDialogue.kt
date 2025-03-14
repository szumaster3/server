package content.region.wilderness.dialogue

import content.region.wilderness.handlers.npc.BorkNPC.BorkCutscene
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

class DagonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var cutscene: BorkCutscene? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        cutscene = args[1] as BorkCutscene
        npc(
            "Our Lord Zamorak has power over life and death,",
            player.username + "! He has seen fit to resurrect Bork to",
            "continue his great work...and now you will fall before him",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                val played = player.getSavedData().activityData.hasKilledBork()
                player(if (played) "Uh -oh! Here we go again." else "Oh boy...")
                stage++
            }

            1 -> {
                end()
                cutscene!!.commenceFight()
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DagonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("dagon-dialogue"))
    }
}
