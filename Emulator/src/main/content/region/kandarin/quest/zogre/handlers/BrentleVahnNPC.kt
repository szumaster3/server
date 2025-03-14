package content.region.kandarin.quest.zogre.handlers

import core.api.*
import core.api.item.produceGroundItem
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

@Initializable
class BrentleVahnNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var despawnTime = 0
    private val player: Player? = null

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return BrentleVahnNPC(id, location)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (player != null) {
            if (player.location.getDistance(getLocation()) > 10 || !player.isActive || despawnTime++ > 300) {
                poofClear(this)
                sendMessage(player, "This mindless zombie loses interest in fighting you and wanders off.")
                removeAttribute(player, ZUtils.ZOMBIE_NPC_ACTIVE)
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZOMBIE_1826)
    }

    companion object {
        fun spawnHumanZombie(player: Player) {
            val zombie = BrentleVahnNPC(NPCs.ZOMBIE_1826)
            zombie.location = Location.create(2442, 9458, 2)
            zombie.isWalks = true
            zombie.isAggressive = true
            zombie.isActive = false

            if (zombie.asNpc() != null && zombie.isActive) {
                zombie.properties.teleportLocation = zombie.properties.spawnLocation
            }
            zombie.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, zombie) {
                    override fun pulse(): Boolean {
                        zombie.init()
                        setAttribute(player, ZUtils.ZOMBIE_NPC_ACTIVE, true)
                        registerHintIcon(player, zombie)
                        zombie.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (getVarbit(killer.asPlayer(), Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) in 1..8) {
                produceGroundItem(killer, Items.RUINED_BACKPACK_4810, 1, this.location)
            } else {
                produceGroundItem(killer, Items.BONES_526, 1, this.location)
            }
            clearHintIcon(killer.asPlayer())
            removeAttribute(killer.asPlayer(), ZUtils.ZOMBIE_NPC_ACTIVE)
            clear()
            super.finalizeDeath(killer)
        }
    }
}
