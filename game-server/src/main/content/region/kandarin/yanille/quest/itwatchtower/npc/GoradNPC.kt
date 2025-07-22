package content.region.kandarin.yanille.quest.itwatchtower.npc

import core.api.addItem
import core.api.freeSlots
import core.api.produceGroundItem
import core.api.getQuestStage
import core.api.sendItemDialogue
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Gorad NPC.
 *
 * # Relations
 * - [Watchtower Quest][content.region.kandarin.yanille.quest.itwatchtower.Watchtower]
 */
@Initializable
class GoradNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = GoradNPC(id, location)

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            val player = killer
            if (getQuestStage(player, Quests.WATCHTOWER) >= 2) {
                sendItemDialogue(player, Items.OGRE_TOOTH_2377, "He's dropped a tooth; I'll keep that!")
                if (freeSlots(player) == 0) {
                    produceGroundItem(player, Items.OGRE_TOOTH_2377, 1, this.location)
                } else {
                    addItem(player, Items.OGRE_TOOTH_2377, 1)
                }
                sendMessage(player, "Gorad has gone.")
            }
        }
        super.finalizeDeath(killer)
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(NPCs.GORAD_856)
    }
}