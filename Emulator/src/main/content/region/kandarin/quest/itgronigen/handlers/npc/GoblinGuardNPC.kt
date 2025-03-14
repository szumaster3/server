package content.region.kandarin.quest.itgronigen.handlers.npc

import core.api.item.produceGroundItem
import core.api.quest.setQuestStage
import core.api.sendChat
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GoblinGuardNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return GoblinGuardNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOBLIN_GUARD_489)
    }

    override fun handleTickActions() {
        super.handleTickActions()
    }

    companion object {
        fun spawnGoblinGuard(player: Player) {
            val guard = GoblinGuardNPC(NPCs.GOBLIN_GUARD_489)
            guard.location = Location(2327, 9394, 0)
            guard.isWalks = false
            guard.isRespawn = false
            guard.isAggressive = true
            guard.isActive = false

            if (guard.asNpc() != null && guard.isActive) {
                guard.properties.teleportLocation = guard.properties.spawnLocation
            }
            guard.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, guard) {
                    override fun pulse(): Boolean {
                        guard.init()
                        sendChat(guard, "Oi, how dare you wake me up!")
                        guard.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            setQuestStage(killer, Quests.OBSERVATORY_QUEST, 10)
            produceGroundItem(killer, Items.BONES_526, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}
