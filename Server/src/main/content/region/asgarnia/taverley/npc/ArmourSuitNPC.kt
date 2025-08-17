package content.region.asgarnia.taverley.npc

import core.api.addScenery
import core.api.sendMessage
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Suit of Armour NPC used in Taverley Dungeon.
 */
@Initializable
class ArmourSuitNPC(
    id: Int = NPCs.SUIT_OF_ARMOUR_453,
    location: Location? = null
) : AbstractNPC(id, location) {

    /**
     * Counter used to determine how long the suit has been active.
     */
    private var clearTime = 0

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        ArmourSuitNPC(id, location)

    override fun handleTickActions() {
        super.handleTickActions()
        if (++clearTime >= 100 || (!properties.combatPulse.isAttacking && !inCombat())) {
            clear()
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SUIT_OF_ARMOUR_453)

    override fun clear() {
        super.clear()
        unregister()
        addScenery(Scenery(shared.consts.Scenery.SUIT_OF_ARMOUR_32292, properties.spawnLocation, 1))
    }

    /**
     * Registers this suit in the active/used locations sets.
     */
    private fun register() {
        activeSuits.add(this)
        spawnedLocations.add(location)
    }

    /**
     * Removes this suit from tracking sets.
     */
    private fun unregister() {
        activeSuits.remove(this)
        spawnedLocations.remove(location)
    }

    companion object {
        private const val MAX_SPAWNED = 2

        /**
         * Fixed locations where armour suits may spawn.
         */
        private val spawnLocations = arrayOf(
            Location.create(2887, 9829, 0),
            Location.create(2887, 9832, 0)
        )

        /**
         * Currently active armour suits.
         */
        private val activeSuits = mutableSetOf<ArmourSuitNPC>()

        /**
         * Locations already occupied by spawned suits.
         */
        private val spawnedLocations = mutableSetOf<Location>()

        /**
         * Spawns a suit near the player and attacks them.
         */
        @JvmStatic
        fun spawnArmourSuit(player: Player) {
            if (activeSuits.size >= MAX_SPAWNED) return

            val location = spawnLocations.firstOrNull { it !in spawnedLocations } ?: return

            ArmourSuitNPC(NPCs.SUIT_OF_ARMOUR_453, location).apply {
                init()
                properties.combatPulse.attack(player)
                register()
            }

            getObject(location)?.let(SceneryBuilder::remove)
            sendMessage(player, "Suddenly, the suit of armour comes to life!")
        }
    }
}
