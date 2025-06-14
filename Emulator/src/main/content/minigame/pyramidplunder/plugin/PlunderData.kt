package content.minigame.pyramidplunder.plugin

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Holds data and state for the [PyramidPlunderMinigame].
 */
object PlunderData {

    /**
     * A map of players to their respective rooms.
     */
    val playerLocations = HashMap<Player, PlunderRoom>()

    /**
     * A list of players currently participating.
     */
    val players = ArrayList<Player>()

    /**
     * The varbits associated with each door.
     */
    val doorVarbits = arrayOf(2366, 2367, 2368, 2369)

    /**
     * An array representing the state of the doors. Each entry corresponds to a specific door's state.
     */
    val doors = Array(8) { doorVarbits[0] }

    /**
     * A map of players to the remaining time.
     */
    val timeLeft = HashMap<Player, Int>()

    /**
     * The varbits for the pyramid entrances.
     */
    val pyramidEntranceVarbits = arrayOf(2371, 2372, 2373, 2374)

    /**
     * The current entrance being used for the minigame.
     */
    var currentEntrance = pyramidEntranceVarbits.random()

    /**
     * The timestamp for when the entrance will switch in the minigame.
     */
    var nextEntranceSwitch = 0L

    /**
     * The NPC representing the guardian mummy.
     */
    val mummy = NPC(NPCs.GUARDIAN_MUMMY_4476, Location.create(1968, 4427, 2)).also {
        it.isNeverWalks = true
        it.init()
    }

    /**
     * A collection of artifacts found in the Pyramid Plunder minigame, grouped by type.
     */
    val artifacts = arrayOf(
        arrayOf(Items.IVORY_COMB_9026, Items.POTTERY_SCARAB_9032, Items.POTTERY_STATUETTE_9036),
        arrayOf(Items.STONE_SCARAB_9030, Items.STONE_STATUETTE_9038, Items.STONE_SEAL_9042),
        arrayOf(Items.GOLDEN_SCARAB_9028, Items.GOLDEN_STATUETTE_9034, Items.GOLD_SEAL_9040),
    )

    /**
     * A list of rooms in the minigame.
     */
    val rooms = arrayOf(
        PlunderRoom(1, Location.create(1927, 4477, 0), Location.create(1929, 4469, 0), Direction.SOUTH),
        PlunderRoom(2, Location.create(1954, 4477, 0), Location.create(1955, 4467, 0), Direction.SOUTH),
        PlunderRoom(3, Location.create(1977, 4471, 0), Location.create(1975, 4458, 0), Direction.SOUTH),
        PlunderRoom(4, Location.create(1927, 4453, 0), Location.create(1937, 4454, 0), Direction.EAST),
        PlunderRoom(5, Location.create(1965, 4444, 0), Location.create(1955, 4449, 0), Direction.WEST),
        PlunderRoom(6, Location.create(1927, 4424, 0), Location.create(1925, 4433, 0), Direction.NORTH),
        PlunderRoom(7, Location.create(1943, 4421, 0), Location.create(1950, 4427, 0), Direction.NORTH),
        PlunderRoom(8, Location.create(1974, 4420, 0), Location.create(1971, 4431, 0), Direction.NORTH),
    )
}

/**
 * Represents a room within the Pyramid Plunder minigame, containing information about
 * the room number, entrance location, mummy's location, and the direction of the spear.
 *
 * @property room The room number.
 * @property entrance The location of the entrance to the room.
 * @property mummyLoc The location of the guardian mummy in the room.
 * @property spearDirection The direction in which the spear is pointing.
 */
data class PlunderRoom(
    val room: Int,
    val entrance: Location,
    val mummyLoc: Location,
    val spearDirection: Direction,
)
