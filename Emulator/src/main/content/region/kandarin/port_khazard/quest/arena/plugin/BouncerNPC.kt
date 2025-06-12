package content.region.kandarin.port_khazard.quest.arena.plugin

import content.region.kandarin.quest.arena.dialogue.GeneralKhazardDialogue
import core.api.getAttribute
import core.api.location
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BouncerNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = BouncerNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.BOUNCER_269)

    companion object {
        fun spawnBouncer(player: Player) {
            val bouncer = BouncerNPC(NPCs.BOUNCER_269)
            bouncer.location = location(2604, 3160, 0)
            bouncer.isWalks = true
            bouncer.isAggressive = true
            bouncer.isActive = false

            if (bouncer.asNpc() != null && bouncer.isActive && getAttribute(player, "spawn-bouncer", false)) {
                bouncer.properties.teleportLocation = bouncer.properties.spawnLocation
            }
            bouncer.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(2, bouncer) {
                    override fun pulse(): Boolean {
                        bouncer.init()
                        bouncer.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (getQuestStage(killer, Quests.FIGHT_ARENA) >= 89) {
                setQuestStage(killer, Quests.FIGHT_ARENA, 91)
            }
            removeAttribute(killer, "spawn-bouncer")
            openDialogue(killer, GeneralKhazardDialogue())
        }
        clear()
        super.finalizeDeath(killer)
    }
}
