package content.global.skill.summoning.familiar.dialogue.titan

import content.global.skill.summoning.familiar.Familiar
import core.api.openDialogue
import core.api.sendMessage
import core.api.teleport
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location
import core.game.world.map.zone.impl.WildernessZone
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Lava Titan familiar dialogue.
 */
@Initializable
class LavaTitanDialogue : Dialogue {

    override fun newInstance(player: Player?): Dialogue = LavaTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false

        val familiar = npc as? Familiar
        if (familiar == null) {
            return false
        }

        if (familiar.owner != player) {
            sendMessage(player, "This is not your follower.")
            return true
        }

        options("Chat", "Teleport to Lava Maze")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (buttonId) {
            1 -> openDialogue(player, LavaTitanDialogueFile())
            2 -> {
                if (!WildernessZone.checkTeleport(player, 20)) {
                    end()
                } else {
                    teleport(player, Location(3030, 3842, 0), TeleportType.NORMAL)
                    end()
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LAVA_TITAN_7341, NPCs.LAVA_TITAN_7342)
}