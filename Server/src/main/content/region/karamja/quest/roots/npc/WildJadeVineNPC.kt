package content.region.karamja.quest.roots.npc

import core.api.clearLogoutListener
import core.api.getQuestStage
import core.api.setQuestStage
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Quests

class WildJadeVineNPC(id: Int = 0,location: Location? = null) : AbstractNPC(id, location) {
    var target: Player? = null

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = WildJadeVineNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.WILD_JADE_VINE_3409)

    init {
        isWalks = false
        isRespawn = false
        isAggressive = true
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
        if (!target!!.isActive || !target!!.location.isInRegion(10547)) {
            clear()
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)

        if (killer is Player) {
            if (getQuestStage(killer, Quests.BACK_TO_MY_ROOTS) == 7) {
                setQuestStage(killer, Quests.BACK_TO_MY_ROOTS, 8)
                clearLogoutListener(killer, "jade-vine-fight")
            }
        }
    }
}