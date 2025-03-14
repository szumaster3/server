package content.global.skill.construction.decoration.portalchamber

import core.api.sendDialogueOptions
import core.api.setTitle
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class PortalChamberDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var portal = "nowhere"

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> directPortal("varrock")
                    2 -> directPortal("lumbridge")
                    3 -> directPortal("falador")
                    4 -> {
                        setTitle(player, 4)
                        sendDialogueOptions(
                            player,
                            "Select a destination",
                            "Camelot",
                            "Ardougne",
                            "Yanille",
                            "Kharyrll",
                        )
                        stage++
                    }
                }

            1 ->
                when (buttonId) {
                    1 -> directPortal("camelot")
                    2 -> directPortal("ardougne")
                    3 -> directPortal("yanille")
                    4 -> directPortal("kharyrll")
                }
        }
        return true
    }

    fun directPortal(portal: String) {
        PortalChamberPlugin.direct(
            player,
            portal.uppercase(),
        )
        end()
    }

    override fun open(vararg args: Any?): Boolean {
        setTitle(player, 4)
        sendDialogueOptions(player, "Select a destination", "Varrock", "Lumbridge", "Falador", "More...")
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return PortalChamberDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(394857)
    }
}
