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
class CommonKebbitEast : HunterTracking() {
    init {
        initialMap =
            hashMapOf(
                19439 to
                    arrayListOf(
                        TrailDefinition(
                            varbit = 2974,
                            type = TrailType.LINKING,
                            inverted = false,
                            startLocation = Location.create(2354, 3595, 0),
                            endLocation = Location.create(2360, 3602, 0),
                        ),
                        TrailDefinition(
                            varbit = 2975,
                            type = TrailType.LINKING,
                            inverted = false,
                            startLocation = Location.create(2354, 3595, 0),
                            endLocation = Location.create(2355, 3601, 0),
                        ),
                        TrailDefinition(
                            varbit = 2976,
                            type = TrailType.LINKING,
                            inverted = false,
                            startLocation = Location.create(2354, 3594, 0),
                            endLocation = Location.create(2349, 3604, 0),
                        ),
                    ),
                19440 to
                    arrayListOf(
                        TrailDefinition(
                            varbit = 2980,
                            type = TrailType.LINKING,
                            inverted = true,
                            startLocation = Location.create(2361, 3611, 0),
                            endLocation = Location.create(2360, 3602, 0),
                        ),
                        TrailDefinition(
                            varbit = 2981,
                            type = TrailType.LINKING,
                            inverted = true,
                            startLocation = Location.create(2360, 3612, 0),
                            endLocation = Location.create(2357, 3607, 0),
                        ),
                    ),
            )

        linkingTrails =
            arrayListOf(
                TrailDefinition(
                    varbit = 2982,
                    type = TrailType.LINKING,
                    inverted = false,
                    startLocation = Location.create(2357, 3607, 0),
                    endLocation = Location.create(2354, 3609, 0),
                    triggerObjectLocation = Location.create(2355, 3608, 0),
                ),
                TrailDefinition(
                    varbit = 2983,
                    type = TrailType.LINKING,
                    inverted = false,
                    startLocation = Location.create(2354, 3609, 0),
                    endLocation = Location.create(2349, 3604, 0),
                    triggerObjectLocation = Location.create(2351, 3608, 0),
                ),
                TrailDefinition(
                    varbit = 2977,
                    type = TrailType.LINKING,
                    inverted = false,
                    startLocation = Location.create(2360, 3602, 0),
                    endLocation = Location.create(2355, 3601, 0),
                    triggerObjectLocation = Location.create(2358, 3599, 0),
                ),
                TrailDefinition(
                    varbit = 2978,
                    type = TrailType.LINKING,
                    inverted = false,
                    startLocation = Location.create(2355, 3601, 0),
                    endLocation = Location.create(2349, 3604, 0),
                    triggerObjectLocation = Location.create(2352, 3603, 0),
                ),
                TrailDefinition(
                    varbit = 2979,
                    type = TrailType.LINKING,
                    inverted = false,
                    startLocation = Location.create(2360, 3602, 0),
                    endLocation = Location.create(2357, 3607, 0),
                    triggerObjectLocation = Location.create(2358, 3603, 0),
                ),
            )
        experience = 36.0
        varp = 919
        trailLimit = 3
        attribute = "hunter:tracking:commontrail"
        indexAttribute = "hunter:tracking:commonIndex"
        rewards = arrayOf(Item(Items.COMMON_KEBBIT_FUR_10121), Item(Items.BONES_526), Item(Items.RAW_BEAST_MEAT_9986))
        KEBBIT_ANIM = Animation(Animations.CATCH_KEBBIT_NOOSE_WAND_5259)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        if (!linkingTrails.contains(initialMap.values.random()[0])) {
            addExtraTrails()
        }
        SceneryDefinition.forId(Scenery.PLANT_19356).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19357).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19358).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19359).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19360).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19361).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19362).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19363).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19364).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19365).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19372).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19373).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19374).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19375).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19376).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19377).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19378).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19379).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.PLANT_19380).handlers["option:inspect"] = this

        SceneryDefinition.forId(Scenery.BURROW_19439).handlers["option:inspect"] = this
        SceneryDefinition.forId(Scenery.BURROW_19440).handlers["option:inspect"] = this

        SceneryDefinition.forId(Scenery.BUSH_19428).handlers["option:attack"] = this
        SceneryDefinition.forId(Scenery.BUSH_19428).handlers["option:search"] = this

        return this
    }
}
