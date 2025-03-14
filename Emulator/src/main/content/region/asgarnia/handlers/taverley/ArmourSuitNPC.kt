package content.region.asgarnia.handlers.taverley

import core.api.addScenery
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Suit of Armour NPC used in Taverley Dungeon.
 */
@Initializable
class ArmourSuitNPC(
    id: Int = NPCs.SUIT_OF_ARMOUR_453, location: Location? = null
) : AbstractNPC(id, location) {

    private var clearTime = 0

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return ArmourSuitNPC(id, location)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (++clearTime >= 100 || (!properties.combatPulse.isAttacking && !inCombat())) {
            clear()
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SUIT_OF_ARMOUR_453)

    override fun clear() {
        super.clear()
        activeSuits.remove(this)
        spawnedLocations.remove(this.location)
        addScenery(Scenery(org.rs.consts.Scenery.SUIT_OF_ARMOUR_32292, properties.spawnLocation, 1))
    }

    companion object {
        val activeSuits = mutableListOf<ArmourSuitNPC>()
        private const val MAX_SPAWNED = 2
        private val spawnLocations = arrayOf(Location.create(2887, 9829, 0), Location.create(2887, 9832, 0))
        private val spawnedLocations = mutableSetOf<Location>()

        @JvmStatic
        fun spawnArmourSuit(player: Player) {
            if (activeSuits.size >= MAX_SPAWNED) return

            spawnLocations.firstOrNull { it !in spawnedLocations }?.let { availableLocation ->
                ArmourSuitNPC(NPCs.SUIT_OF_ARMOUR_453, availableLocation).apply {
                    init()
                    properties.combatPulse.attack(player)
                    activeSuits.add(this)
                    spawnedLocations.add(availableLocation)
                }

                getObject(availableLocation)?.let(SceneryBuilder::remove)
                player.packetDispatch.sendMessage("Suddenly, the suit of armour comes to life!")
            }
        }
    }
}
