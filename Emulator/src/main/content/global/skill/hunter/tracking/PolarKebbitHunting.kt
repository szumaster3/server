package content.global.skill.hunter.tracking

import core.cache.def.impl.SceneryDefinition
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

@Initializable
class PolarKebbitHunting : HunterTracking() {
    init {
        KEBBIT_ANIM = Animation(Animations.CATCH_POLAR_KEBBIT_NOOSE_WAND_5256)
        trailLimit = 3
        attribute = "hunter:tracking:polartrail"
        indexAttribute = "hunter:tracking:polarindex"
        rewards = arrayOf(Item(Items.RAW_BEAST_MEAT_9986), Item(Items.POLAR_KEBBIT_FUR_10117), Item(Items.BONES_526))
        tunnelEntrances =
            arrayOf(
                Location.create(2711, 3819, 1),
                Location.create(2714, 3821, 1),
                Location.create(2718, 3829, 1),
                Location.create(2721, 3827, 1),
                Location.create(2718, 3832, 1),
                Location.create(2715, 3820, 1),
            )
        initialMap =
            hashMapOf(
                19640 to
                    arrayListOf(
                        TrailDefinition(
                            varbit = 3061,
                            type = TrailType.TUNNEL,
                            inverted = false,
                            startLocation = Location.create(2712, 3831, 1),
                            endLocation = Location.create(2718, 3832, 1),
                        ),
                        TrailDefinition(
                            varbit = 3060,
                            type = TrailType.LINKING,
                            inverted = true,
                            startLocation = Location.create(2712, 3831, 1),
                            endLocation = Location.create(2716, 3827, 1),
                            triggerObjectLocation = Location.create(2713, 3827, 1),
                        ),
                        TrailDefinition(
                            varbit = 3057,
                            type = TrailType.LINKING,
                            inverted = false,
                            startLocation = Location.create(2712, 3831, 1),
                            endLocation = Location.create(2708, 3819, 1),
                            triggerObjectLocation = Location.create(2708, 3825, 1),
                        ),
                    ),
                19641 to
                    arrayListOf(
                        TrailDefinition(
                            varbit = 3053,
                            type = TrailType.LINKING,
                            inverted = true,
                            startLocation = Location.create(2718, 3820, 1),
                            endLocation = Location.create(2708, 3819, 1),
                            triggerObjectLocation = Location.create(2712, 3815, 1),
                        ),
                        TrailDefinition(
                            varbit = 3055,
                            type = TrailType.TUNNEL,
                            inverted = false,
                            startLocation = Location.create(2718, 3820, 1),
                            endLocation = Location.create(2715, 3820, 1),
                        ),
                        TrailDefinition(
                            varbit = 3056,
                            type = TrailType.TUNNEL,
                            inverted = false,
                            startLocation = Location.create(2718, 3820, 1),
                            endLocation = Location.create(2721, 3827, 1),
                        ),
                    ),
            )
        linkingTrails =
            arrayListOf(
                TrailDefinition(
                    varbit = 3058,
                    type = TrailType.LINKING,
                    inverted = true,
                    startLocation = Location.create(2714, 3821, 1),
                    endLocation = Location.create(2716, 3827, 1),
                ),
                TrailDefinition(
                    varbit = 3059,
                    type = TrailType.TUNNEL,
                    inverted = true,
                    startLocation = Location.create(2716, 3827, 1),
                    endLocation = Location.create(2718, 3829, 1),
                ),
                TrailDefinition(
                    varbit = 3054,
                    type = TrailType.TUNNEL,
                    inverted = false,
                    startLocation = Location.create(2708, 3819, 1),
                    endLocation = Location.create(2711, 3819, 1),
                ),
            )
        experience = 30.0
        varp = 926
        requiredLevel = 1
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        addExtraTrails()
        SceneryDefinition.forId(Scenery.HOLE_19640).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.HOLE_19641).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.HOLLOW_LOG_36689).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.HOLLOW_LOG_36690).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.TUNNEL_19421).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.TUNNEL_19424).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.TUNNEL_19426).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.TUNNEL_19419).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.TUNNEL_19420).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.TUNNEL_19423).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.HOLLOW_LOG_36688).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.SNOW_DRIFT_19435).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.SNOW_DRIFT_19435).handlers["option:search"] = this
        SceneryDefinition.forId(Scenery.SNOW_DRIFT_19435).handlers["option:attack"] = this
        return this
    }
}
