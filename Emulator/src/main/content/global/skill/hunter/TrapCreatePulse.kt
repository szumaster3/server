package content.global.skill.hunter

import core.api.lock
import core.api.playAudio
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import org.rs.consts.Sounds

class TrapCreatePulse(
    player: Player,
    node: Node,
    val trap: Traps,
) : SkillPulse<Node?>(player, node) {
    private val startLocation: Location = if (node is GroundItem) node.getLocation() else player.location
    private var groundItem: GroundItem? = null
    private var ticks = 0
    private val instance: HunterManager = HunterManager.getInstance(player)

    init {
        if (checkRequirements()) {
            when (trap) {
                Traps.BIRD_SNARE -> playAudio(player, Sounds.HUNTING_SETNOOSE_2646, 40)
                Traps.BOX_TRAP -> playAudio(player, Sounds.HUNTING_LAYBOXTRAP_2636, 20)
                Traps.NET_TRAP -> {
                    lock(player, 3)
                    playAudio(player, Sounds.HUNTING_SET_TWITCHNET_2644)
                }

                Traps.RABBIT_SNARE -> playAudio(player, Sounds.HUNTING_SETSNARE_2647)
                Traps.DEAD_FALL -> {
                    lock(player, 6)
                    playAudio(player, Sounds.HUNTING_SETDEADFALL_2645, 130)
                }

                Traps.IMP_BOX -> playAudio(player, Sounds.HUNTING_BOXTRAP_2627)
            }
        }
    }

    override fun checkRequirements(): Boolean {
        if (player.skills.getStaticLevel(Skills.HUNTER) < trap.settings.level) {
            player.sendMessage(
                "You need a Hunter level of at least " + trap.settings.level + " in order to setup a " +
                    node!!.name.lowercase() +
                    ".",
            )
            return false
        }
        if (instance.exceedsTrapLimit(trap)) {
            player.sendMessage(trap.settings.getLimitMessage(player))
            return false
        }
        if (getObject(player.location) != null) {
            player.sendMessage("You can't lay a trap here.")
            return false
        }
        if (player.location != startLocation) {
            return false
        }
        return !trap.settings.isObjectTrap || trap.settings.hasItems(player)
    }

    override fun animate() {
        if (ticks < 1) {
            player.animator.forceAnimation(trap.settings.setupAnimation)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % (trap.settings.setupAnimation.definition.durationTicks) != 0) {
            return false
        }
        var `object` = trap.settings.buildObject(player, node)
        if (isGroundSetup || groundItem != null) {
            GroundItemManager.destroy(groundItem)
        }
        if (!trap.settings.isObjectTrap) {
            player.moveStep()
        } else {
            SceneryBuilder.remove(node!!.asScenery())
        }
        `object` = SceneryBuilder.add(`object`)
        instance.register(trap, node, `object`)
        return true
    }

    override fun message(type: Int) {
        when (type) {
            0 -> {
                setUp()
                player.packetDispatch.sendMessage("You begin setting up the trap.")
            }
        }
    }

    private fun setUp() {
        player.lock(1)
        player.walkingQueue.reset()
        if (trap.settings.isObjectTrap) {
        } else {
            if (!isGroundSetup) {
                if (player.inventory.remove(node as Item?)) {
                    groundItem = GroundItem(node as Item, player.location, player)
                    GroundItemManager.create(groundItem!!)
                }
                return
            }
            groundItem = node as GroundItem?
        }
    }

    val isGroundSetup: Boolean
        get() = node is GroundItem
}
