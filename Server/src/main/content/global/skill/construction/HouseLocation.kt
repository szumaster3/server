package content.global.skill.construction

import core.api.getStatLevel
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.tools.StringUtils
import org.rs.consts.Scenery

enum class HouseLocation(
    val portalId: Int,
    val exitLocation: Location?,
    val cost: Int,
    val levelRequirement: Int
) {
    NOWHERE(-1, null, 0, 0),
    RIMMINGTON(Scenery.PORTAL_15478, Location.create(2953, 3224, 0), 5000, 1),
    TAVERLY(Scenery.PORTAL_15477, Location.create(2893, 3465, 0), 5000, 10),
    POLLNIVNEACH(Scenery.PORTAL_15479, Location.create(3340, 3003, 0), 7500, 20),
    RELLEKKA(Scenery.PORTAL_15480, Location.create(2670, 3631, 0), 10000, 30),
    BRIMHAVEN(Scenery.PORTAL_15481, Location.create(2757, 3178, 0), 15000, 40),
    YANILLE(Scenery.PORTAL_15482, Location.create(2544, 3096, 0), 25000, 50);

    fun hasLevel(player: Player): Boolean =
        getStatLevel(player, Skills.CONSTRUCTION) >= levelRequirement

    fun getName(): String = StringUtils.formatDisplayName(name.lowercase())
}