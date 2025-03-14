package content.region.morytania.handlers.tarnslair.traps

import core.api.getStatLevel
import core.api.impact
import core.api.sendMessage
import core.api.visualize
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.Scenery

class PillarSceneryListener : InteractionListener {
    private val PILLAR_IDS =
        intArrayOf(
            Scenery.PILLAR_20872,
            Scenery.PILLAR_20873,
            Scenery.PILLAR_20874,
            Scenery.PILLAR_20875,
            Scenery.PILLAR_20876,
            Scenery.PILLAR_20877,
            Scenery.PILLAR_20878,
            Scenery.PILLAR_20879,
            Scenery.PILLAR_20880,
            Scenery.PILLAR_20881,
            Scenery.PILLAR_20882,
            Scenery.PILLAR_20883,
            Scenery.PILLAR_20884,
            Scenery.PILLAR_20885,
            Scenery.PILLAR_20886,
            Scenery.PILLAR_20887,
            Scenery.PILLAR_20888,
            Scenery.PILLAR_20889,
        )

    private val LEDGE_IDS =
        intArrayOf(
            Scenery.LEDGE_20890,
            Scenery.LEDGE_20891,
            Scenery.LEDGE_20892,
            Scenery.LEDGE_20893,
            Scenery.LEDGE_20894,
            Scenery.LEDGE_20895,
            Scenery.LEDGE_20896,
            Scenery.LEDGE_20897,
            Scenery.LEDGE_20898,
            Scenery.LEDGE_20899,
            Scenery.LEDGE_20900,
            Scenery.LEDGE_20901,
        )

    private val PILLARS_ZONE_0 = ZoneBorders(3192, 4564, 3178, 4558)
    private val PILLARS_ZONE_1 = ZoneBorders(3152, 4602, 3139, 4592)

    private val PILLARS_ZONE_1_FAIL =
        arrayOf(
            Location.create(3149, 4595, 0),
            Location.create(3149, 4597, 0),
            Location.create(3147, 4599, 0),
            Location.create(3147, 4597, 0),
            Location.create(3147, 4595, 0),
            Location.create(3145, 4595, 0),
            Location.create(3145, 4597, 0),
            Location.create(3145, 4599, 0),
        )

    private val PILLARS_ZONE_0_FAIL_NORTH =
        arrayOf(
            Location.create(3185, 4560, 0),
            Location.create(3185, 4562, 0),
        )

    private val PILLARS_ZONE_0_FAIL_SOUTH =
        arrayOf(
            Location.create(3183, 4562, 0),
            Location.create(3183, 4560, 0),
        )

    private val PILLARS_ZONE_0_IDS =
        arrayOf(
            Location.create(3184, 4558, 1),
            Location.create(3184, 4560, 1),
            Location.create(3184, 4562, 1),
            Location.create(3184, 4564, 1),
        )

    private val PILLARS_ZONE_1_IDS =
        arrayOf(
            Location.create(3144, 4601, 1),
            Location.create(3144, 4599, 1),
            Location.create(3144, 4597, 1),
            Location.create(3144, 4595, 1),
            Location.create(3142, 4595, 1),
            Location.create(3140, 4595, 1),
            Location.create(3146, 4595, 1),
            Location.create(3148, 4595, 1),
            Location.create(3150, 4595, 1),
            Location.create(3150, 4597, 1),
            Location.create(3148, 4597, 1),
        )

    private val WALL_TRAP_IDS =
        intArrayOf(
            Scenery.WALL_20920,
            Scenery.WALL_20921,
            Scenery.WALL_20922,
            Scenery.WALL_20923,
            Scenery.WALL_20924,
            Scenery.WALL_20925,
            Scenery.WALL_20926,
            Scenery.WALL_20927,
            Scenery.WALL_20928,
            Scenery.WALL_20929,
            Scenery.WALL_20930,
            Scenery.WALL_20931,
            Scenery.WALL_20932,
            Scenery.WALL_20933,
            Scenery.WALL_20934,
            Scenery.WALL_20935,
            Scenery.WALL_20936,
            Scenery.WALL_20937,
            Scenery.WALL_20938,
            Scenery.WALL_20939,
            Scenery.WALL_20940,
            Scenery.WALL_20941,
            Scenery.WALL_20942,
            Scenery.WALL_20943,
            Scenery.WALL_20944,
            Scenery.WALL_20945,
            Scenery.WALL_20946,
            Scenery.WALL_20947,
            Scenery.WALL_20948,
            Scenery.WALL_20949,
            Scenery.WALL_20950,
            Scenery.WALL_20951,
            Scenery.WALL_20952,
            Scenery.WALL_20953,
            Scenery.WALL_20954,
            Scenery.WALL_20955,
        )

    private val FLOOR_TRAP_IDS =
        intArrayOf(
            Scenery.FLOOR_20915,
            Scenery.FLOOR_20916,
            Scenery.FLOOR_20917,
            Scenery.FLOOR_20918,
            Scenery.FLOOR_20919,
            Scenery.FLOOR_20956,
            Scenery.FLOOR_20957,
            Scenery.FLOOR_20958,
            Scenery.FLOOR_20959,
            Scenery.FLOOR_20960,
            Scenery.FLOOR_20961,
            Scenery.FLOOR_20962,
            Scenery.FLOOR_20963,
            Scenery.FLOOR_20964,
            Scenery.FLOOR_20965,
            Scenery.FLOOR_20966,
            Scenery.FLOOR_20967,
        )

    private val TRAP_BYPASS = Scenery.TRAP_BYPASS_20902

    override fun defineListeners() {
        on(intArrayOf(*PILLAR_IDS, *LEDGE_IDS), IntType.SCENERY, "jump-to") { player, node ->
            val agilityLevel = getStatLevel(player, Skills.AGILITY)
            val chance = 50 + ((agilityLevel - 1) / 98.0) * 50
            val damage = (player.skills.lifepoints * 0.4).toInt()
            impact(player, damage)
            visualize(player, Animations.JUMPING_OVER_SOMETHING_AND_FAILING_THEN_FALLING_3068, Graphics.NOTHING_61)
            sendMessage(player, "The huge log knocks you off!")
            return@on true
        }
    }
}
