package content.region.islands.quests.lostcity.plugin

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendDialogue
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TreeSpiritNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var target: Player? = null

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = TreeSpiritNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.TREE_SPIRIT_655)

    init {
        isWalks = true
        isRespawn = false
    }

    override fun handleTickActions() {
        if (target == null) {
            clear()
            return
        }
        super.handleTickActions()
        if (!inCombat()) {
            attack(target)
        }
        if (!target!!.isActive || target!!.location.getDistance(getLocation()) > 15) {
            clear()
            target!!.removeAttribute("treeSpawned")
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            if (getQuestStage(killer, Quests.LOST_CITY) == 20) {
                setQuestStage(killer, Quests.LOST_CITY, 21)
                sendDialogue(killer, "With the Tree Spirit defeated you can now chop the tree.")
            }
        }
    }
}
