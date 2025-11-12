package content.global.plugins.scenery

import core.api.freeSlots
import core.api.playAudio
import core.api.sendMessage
import core.api.setAttribute
import core.cache.def.impl.SceneryDefinition
import core.game.container.impl.EquipmentContainer
import core.game.event.ResourceProducedEvent
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Sounds

@Initializable
class FieldPickingPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (p in PickingPlant.values().map { it.objectId }.toIntArray()) {
            SceneryDefinition.forId(p).handlers["option:pick"] = this
        }
        SceneryDefinition.forId(Scenery.BUDDING_BRANCH_3511).handlers["option:take-cutting"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (player.getAttribute("delay:picking", -1) > ticks) {
            return true
        }
        val scenery = node as core.game.node.scenery.Scenery
        val plant = PickingPlant.forId(scenery.id) ?: return false
        if (!scenery.isActive) {
            return true
        }
        val reward = Item(if (plant == PickingPlant.POTATO && RandomFunction.random(10) == 0) 5318 else plant.reward)
        if (!player.inventory.hasSpaceFor(reward)) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return true
        }
        if (freeSlots(player) == 0) {
            return true
        }
        player.lock(1)
        setAttribute(player, "delay:picking", ticks + (if (plant == PickingPlant.FLAX) 2 else 3))
        player.animate(ANIMATION)
        playAudio(player, Sounds.PICK_2581, 30)
        player.dispatch(ResourceProducedEvent(reward.id, reward.amount, node, -1))
        if (plant.name.startsWith(
                "NETTLES",
                true,
            ) &&
            (
                    player.equipment[EquipmentContainer.SLOT_HANDS] == null ||
                            player.equipment[EquipmentContainer.SLOT_HANDS] != null &&
                            !player.equipment[EquipmentContainer.SLOT_HANDS].name.contains(
                                "glove",
                                true,
                            )
                    )
        ) {
            player.packetDispatch.sendMessage("You have been stung by the nettles!")
            player.impactHandler.manualHit(player, 2, HitsplatType.POISON)
            return true
        }
        if (plant.respawn != -1 && plant != PickingPlant.FLAX) {
            scenery.isActive = false
        }
        Pulser.submit(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    if (!player.inventory.add(reward)) {
                        sendMessage(player, "You don't have enough space in your inventory.")
                        return true
                    }
                    if (plant == PickingPlant.FLAX) {
                        handleFlaxPick(player, scenery, plant)
                        return true
                    }
                    val banana = plant.name.startsWith("BANANA", true)
                    var full: core.game.node.scenery.Scenery? = null
                    if (plant == PickingPlant.BANANA_TREE_4) {
                        full = scenery.transform(Scenery.BANANA_TREE_2073)
                        SceneryBuilder.replace(scenery, full)
                    }
                    val isBloomPlant =
                        plant == PickingPlant.FUNGI_ON_LOG || plant == PickingPlant.BUDDING_BRANCH || plant == PickingPlant.GOLDEN_PEAR_BUSH || plant == PickingPlant.GOLDEN_PEAR_BUSH
                    if (isBloomPlant) {
                        full = scenery.transform(scenery.id - 1)
                        SceneryBuilder.replace(scenery, full)
                    }
                    if (!isBloomPlant) {
                        SceneryBuilder.replace(
                            if (plant == PickingPlant.BANANA_TREE_4) full else scenery,
                            scenery.transform(if (banana) plant.respawn else 0),
                            if (banana) 300 else plant.respawn,
                        )
                    }
                    if (plant.name.startsWith("MORT", true)) {
                        sendMessage(player, "You pick a mushroom from the log.")
                    } else if (plant.name.startsWith("GLOWING", true)) {
                        sendMessage(player, "You pull the fungus from the water. It is very cold to the touch.")
                    } else if (!plant.name.startsWith("NETTLES", true)) {
                        sendMessage(player, "You pick a " + reward.name.lowercase() + ".")
                    } else {
                        sendMessage(player, "You pick a handful of nettles.")
                    }
                    return true
                }
            },
        )
        return true
    }

    private fun handleFlaxPick(
        player: Player,
        scenery: core.game.node.scenery.Scenery,
        plant: PickingPlant,
    ) {
        val charge = scenery.charge
        playAudio(player, Sounds.PICK_2581)
        player.packetDispatch.sendMessage("You pick some flax.")

        if (charge > 1000 + RandomFunction.random(2, 8)) {
            scenery.isActive = false
            scenery.charge = 1000
            SceneryBuilder.replace(scenery, scenery.transform(0), plant.respawn)
            return
        }
        scenery.charge = charge + 1
    }

    private enum class PickingPlant(val objectId: Int, val reward: Int, val respawn: Int) {
        POTATO(Scenery.POTATO_312, Items.POTATO_1942, 30),
        WHEAT_0(Scenery.WHEAT_313, Items.GRAIN_1947, 30),
        WHEAT_1(Scenery.WHEAT_5583, Items.GRAIN_1947, 30),
        WHEAT_2(Scenery.WHEAT_5584, Items.GRAIN_1947, 30),
        WHEAT_3(Scenery.WHEAT_5585, Items.GRAIN_1947, 30),
        WHEAT_4(Scenery.WHEAT_15506, Items.GRAIN_1947, 30),
        WHEAT_5(Scenery.WHEAT_15507, Items.GRAIN_1947, 30),
        WHEAT_6(Scenery.WHEAT_15508, Items.GRAIN_1947, 30),
        WHEAT_7(Scenery.WHEAT_22300, Items.GRAIN_1947, 30),
        WHEAT_8(Scenery.WHEAT_22473, Items.GRAIN_1947, 30),
        WHEAT_9(Scenery.WHEAT_22474, Items.GRAIN_1947, 30),
        WHEAT_10(Scenery.WHEAT_22475, Items.GRAIN_1947, 30),
        WHEAT_11(Scenery.WHEAT_22476, Items.GRAIN_1947, 30),
        CABBAGE_0(Scenery.CABBAGE_1161, Items.CABBAGE_1965, 30),
        CABBAGE_1(Scenery.CABBAGE_11494, Items.CABBAGE_1967, 30),
        CABBAGE_2(Scenery.CABBAGE_22301, Items.CABBAGE_1965, 30),
        NETTLES_0(Scenery.NETTLES_1181, Items.NETTLES_4241, 30),
        NETTLES_1(Scenery.NETTLES_5253, Items.NETTLES_4241, 30),
        NETTLES_2(Scenery.NETTLES_5254, Items.NETTLES_4241, 30),
        NETTLES_3(Scenery.NETTLES_5255, Items.NETTLES_4241, 30),
        NETTLES_4(Scenery.NETTLES_5256, Items.NETTLES_4241, 30),
        NETTLES_5(Scenery.NETTLES_5257, Items.NETTLES_4241, 30),
        NETTLES_6(Scenery.NETTLES_5258, Items.NETTLES_4241, 30),
        PINEAPPLE_PLANT_0(Scenery.PINEAPPLE_PLANT_1408, Items.PINEAPPLE_2114, 30),
        PINEAPPLE_PLANT_1(Scenery.PINEAPPLE_PLANT_1409, Items.PINEAPPLE_2114, 30),
        PINEAPPLE_PLANT_2(Scenery.PINEAPPLE_PLANT_1410, Items.PINEAPPLE_2114, 30),
        PINEAPPLE_PLANT_3(Scenery.PINEAPPLE_PLANT_1411, Items.PINEAPPLE_2114, 30),
        PINEAPPLE_PLANT_4(Scenery.PINEAPPLE_PLANT_1412, Items.PINEAPPLE_2114, 30),
        PINEAPPLE_PLANT_5(Scenery.PINEAPPLE_PLANT_1413, Items.PINEAPPLE_2114, 30),
        PINEAPPLE_PLANT_6(Scenery.PINEAPPLE_PLANT_4827, Items.PINEAPPLE_2114, 30),
        BANANA_TREE_0(Scenery.BANANA_TREE_2073, Items.BANANA_1963, 2074),
        BANANA_TREE_1(Scenery.BANANA_TREE_2074, Items.BANANA_1963, 2075),
        BANANA_TREE_2(Scenery.BANANA_TREE_2075, Items.BANANA_1963, 2076),
        BANANA_TREE_3(Scenery.BANANA_TREE_2076, Items.BANANA_1963, 2077),
        BANANA_TREE_4(Scenery.BANANA_TREE_2077, Items.BANANA_1963, 2078),
        BANANA_TREE_5(Scenery.BANANA_TREE_12606, Items.BANANA_1963, 2079),
        BANANA_TREE_6(Scenery.BANANA_TREE_12607, Items.BANANA_1963, 2080),
        FLAX(Scenery.FLAX_2646, Items.FLAX_1779, 30),
        ONION_0(Scenery.ONION_3366, Items.ONION_1957, 30),
        ONION_1(Scenery.ONION_5538, Items.ONION_1957, 30),
        FUNGI_ON_LOG(Scenery.FUNGI_ON_LOG_3509, Items.MORT_MYRE_FUNGUS_2970, -1),
        BUDDING_BRANCH(Scenery.BUDDING_BRANCH_3511, Items.MORT_MYRE_STEM_2972, -1),
        GOLDEN_PEAR_BUSH(Scenery.A_GOLDEN_PEAR_BUSH_3513, Items.MORT_MYRE_PEAR_2974, -1),
        GLOWING_FUNGUS_0(Scenery.GLOWING_FUNGUS_4932, Items.GLOWING_FUNGUS_4075, 30),
        GLOWING_FUNGUS_1(Scenery.GLOWING_FUNGUS_4933, Items.GLOWING_FUNGUS_4075, 30),
        RARE_FLOWERS(Scenery.TROLLWEISS_FLOWERS_5006, Items.FLOWERS_2460, 30),
        BLACK_MUSHROOMS(Scenery.BLACK_MUSHROOMS_6311, Items.BLACK_MUSHROOM_4620, 30),
        KELP(Scenery.KELP_12478, Items.KELP_7516, 30),
        RED_BANANA_TREE(Scenery.RED_BANANA_TREE_12609, Items.RED_BANANA_7572, 30),
        BUSH(Scenery.BUSH_12615, Items.TCHIKI_MONKEY_NUTS_7573, 30),
        RED_FLOWERS(Scenery.RED_FLOWERS_15846, Items.RED_FLOWERS_8938, 1),
        BLUE_FLOWERS(Scenery.BLUE_FLOWERS_15872, Items.BLUE_FLOWERS_8936, 1),
        HARDY_GOUTWEED(Scenery.HARDY_GOUTWEED_18855, Items.GOUTWEED_3261, 30),
        HERBS_0(Scenery.HERBS_21668, Items.GRIMY_GUAM_199, 30),
        HERBS_1(Scenery.HERBS_21669, Items.GRIMY_MARRENTILL_201, 30),
        HERBS_2(Scenery.HERBS_21670, Items.GRIMY_TARROMIN_203, 30),
        HERBS_3(Scenery.HERBS_21671, Items.GRIMY_HARRALANDER_205, 30),
        FEVER_GRASS(Scenery.FEVER_GRASS_29113, Items.FEVER_GRASS_12574, 30),
        LAVENDER(Scenery.LAVENDER_29114, Items.LAVENDER_12572, 30),
        TANSYMUM(Scenery.TANSYMUM_29115, Items.TANSYMUM_12576, 30),
        PRIMWEED(Scenery.PRIMWEED_29116, Items.PRIMWEED_12588, 30),
        STINKBLOOM(Scenery.STINKBLOOM_29117, Items.STINKBLOOM_12590, 30),
        TROLLWEISS_FLOWERS(Scenery.TROLLWEISS_FLOWERS_37328, Items.TROLLWEISS_4086, 30),
        HOLLOW_LOG(Scenery.HOLLOW_LOG_37830, Items.MUSHROOMS_10968, 30),
        ;

        companion object {
            @JvmStatic
            fun forId(objectId: Int): PickingPlant? {
                for (plant in values()) if (plant.objectId == objectId) return plant
                return null
            }
        }
    }

    companion object {
        private val ANIMATION = Animation(Animations.HUMAN_BURYING_BONES_827)
    }
}
