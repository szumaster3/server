package content.region.kandarin.miniquest.ctr.npc

import content.data.GameAttributes
import content.region.kandarin.camelot.dialogue.MerlinDialogue
import core.api.*
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import org.rs.consts.NPCs

/**
 * Represents the Merlin NPC that spawns after the final wave of the miniquest.
 */
class MerlinKnightWavesNPC(id: Int = 0, location: Location? = null, ) : AbstractNPC(id, location) {
    private var cleanTime = 0
    private var player: Player? = null

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MerlinKnightWavesNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.MERLIN_213)

    /**
     * Auto-cleans attributes and removes Merlin after a delay.
     */
    override fun handleTickActions() {
        super.handleTickActions()
        if (cleanTime++ > 300) {
            removeAttributes(player!!, GameAttributes.KW_TIER, GameAttributes.KW_BEGIN)
        }
        poofClear(this)
    }

    companion object {
        /**
         * Spawns Merlin for the final dialogue.
         */
        fun spawnMerlin(player: Player) {
            val merlin = MerlinKnightWavesNPC(NPCs.MERLIN_213)
            merlin.location = Location.create(2750, 3505, 2)
            merlin.isWalks = false
            merlin.isAggressive = false
            merlin.isActive = false

            if (merlin.asNpc() != null && merlin.isActive) {
                merlin.properties.teleportLocation = merlin.properties.spawnLocation
            }
            merlin.isActive = true
            Pulser.submit(
                object : Pulse(1, merlin) {
                    override fun pulse(): Boolean {
                        merlin.init()
                        face(findLocalNPC(player, NPCs.MERLIN_213)!!, player, 3)
                        face(player, findLocalNPC(player, NPCs.MERLIN_213)!!)
                        openDialogue(player, MerlinDialogue())
                        return true
                    }
                },
            )
        }
    }
}