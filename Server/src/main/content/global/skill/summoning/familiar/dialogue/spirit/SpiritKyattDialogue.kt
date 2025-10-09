package content.global.skill.summoning.familiar.dialogue.spirit

import content.global.skill.summoning.familiar.Familiar
import core.api.openDialogue
import core.api.sendOptions
import core.api.sendMessage
import core.api.teleport
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.impl.WildernessZone
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * The type Spirit kyatt dialogue.
 */
@Initializable
class SpiritKyattDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = SpiritKyattDialogue(player)

    /**
     * Instantiates a new Spirit kyatt dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit kyatt dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (npc !is Familiar) {
            return false
        }
        val f = npc as Familiar
        if (f.owner != player) {
            sendMessage(player, "This is not your follower.")
            return true
        } else {
            sendOptions(player, "Select an Option", "Chat", "Teleport")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (buttonId) {
            1 -> openDialogue(player, SpiritKyattDialogueFile())
            2 ->
                if (!WildernessZone.checkTeleport(player, 20)) {
                    end()
                } else {
                    teleport(player, Location(2326, 3634, 0), TeleportManager.TeleportType.NORMAL)
                    end()
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_KYATT_7365, NPCs.SPIRIT_KYATT_7366)
}
