package content.global.handlers.scenery

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
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Sounds

@Initializable
class FieldPickingOptionHandler : OptionHandler() {
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
                        plant == PickingPlant.FUNGI_ON_LOG ||
                            plant == PickingPlant.BUDDING_BRANCH ||
                            plant == PickingPlant.GOLDEN_PEAR_BUSH
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

    private enum class PickingPlant(
        val objectId: Int,
        val reward: Int,
        val respawn: Int,
    ) {
        POTATO(objectId = Scenery.POTATO_312, reward = Items.POTATO_1942, respawn = 30),
        WHEAT_0(objectId = Scenery.WHEAT_313, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_1(objectId = Scenery.WHEAT_5583, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_2(objectId = Scenery.WHEAT_5584, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_3(objectId = Scenery.WHEAT_5585, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_4(objectId = Scenery.WHEAT_15506, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_5(objectId = Scenery.WHEAT_15507, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_6(objectId = Scenery.WHEAT_15508, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_7(objectId = Scenery.WHEAT_22300, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_8(objectId = Scenery.WHEAT_22473, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_9(objectId = Scenery.WHEAT_22474, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_10(objectId = Scenery.WHEAT_22475, reward = Items.GRAIN_1947, respawn = 30),
        WHEAT_11(objectId = Scenery.WHEAT_22476, reward = Items.GRAIN_1947, respawn = 30),
        CABBAGE_0(objectId = Scenery.CABBAGE_1161, reward = Items.CABBAGE_1965, respawn = 30),
        CABBAGE_1(objectId = Scenery.CABBAGE_11494, reward = Items.CABBAGE_1967, respawn = 30),
        CABBAGE_2(objectId = Scenery.CABBAGE_22301, reward = Items.CABBAGE_1965, respawn = 30),
        NETTLES_0(objectId = Scenery.NETTLES_1181, reward = Items.NETTLES_4241, respawn = 30),
        NETTLES_1(objectId = Scenery.NETTLES_5253, reward = Items.NETTLES_4241, respawn = 30),
        NETTLES_2(objectId = Scenery.NETTLES_5254, reward = Items.NETTLES_4241, respawn = 30),
        NETTLES_3(objectId = Scenery.NETTLES_5255, reward = Items.NETTLES_4241, respawn = 30),
        NETTLES_4(objectId = Scenery.NETTLES_5256, reward = Items.NETTLES_4241, respawn = 30),
        NETTLES_5(objectId = Scenery.NETTLES_5257, reward = Items.NETTLES_4241, respawn = 30),
        NETTLES_6(objectId = Scenery.NETTLES_5258, reward = Items.NETTLES_4241, respawn = 30),
        PINEAPPLE_PLANT_0(objectId = Scenery.PINEAPPLE_PLANT_1408, reward = Items.PINEAPPLE_2114, respawn = 30),
        PINEAPPLE_PLANT_1(objectId = Scenery.PINEAPPLE_PLANT_1409, reward = Items.PINEAPPLE_2114, respawn = 30),
        PINEAPPLE_PLANT_2(objectId = Scenery.PINEAPPLE_PLANT_1410, reward = Items.PINEAPPLE_2114, respawn = 30),
        PINEAPPLE_PLANT_3(objectId = Scenery.PINEAPPLE_PLANT_1411, reward = Items.PINEAPPLE_2114, respawn = 30),
        PINEAPPLE_PLANT_4(objectId = Scenery.PINEAPPLE_PLANT_1412, reward = Items.PINEAPPLE_2114, respawn = 30),
        PINEAPPLE_PLANT_5(objectId = Scenery.PINEAPPLE_PLANT_1413, reward = Items.PINEAPPLE_2114, respawn = 30),
        PINEAPPLE_PLANT_6(objectId = Scenery.PINEAPPLE_PLANT_4827, reward = Items.PINEAPPLE_2114, respawn = 30),
        BANANA_TREE_0(objectId = Scenery.BANANA_TREE_2073, reward = Items.BANANA_1963, respawn = 2074),
        BANANA_TREE_1(objectId = Scenery.BANANA_TREE_2074, reward = Items.BANANA_1963, respawn = 2075),
        BANANA_TREE_2(objectId = Scenery.BANANA_TREE_2075, reward = Items.BANANA_1963, respawn = 2076),
        BANANA_TREE_3(objectId = Scenery.BANANA_TREE_2076, reward = Items.BANANA_1963, respawn = 2077),
        BANANA_TREE_4(objectId = Scenery.BANANA_TREE_2077, reward = Items.BANANA_1963, respawn = 2078),
        BANANA_TREE_5(objectId = Scenery.BANANA_TREE_12606, reward = Items.BANANA_1963, respawn = 2079),
        BANANA_TREE_6(objectId = Scenery.BANANA_TREE_12607, reward = Items.BANANA_1963, respawn = 2080),
        FLAX(objectId = Scenery.FLAX_2646, reward = Items.FLAX_1779, respawn = 30),
        ONION_0(objectId = Scenery.ONION_3366, reward = Items.ONION_1957, respawn = 30),
        ONION_1(objectId = Scenery.ONION_5538, reward = Items.ONION_1957, respawn = 30),
        FUNGI_ON_LOG(objectId = Scenery.FUNGI_ON_LOG_3509, reward = Items.MORT_MYRE_FUNGUS_2970, respawn = -1),
        BUDDING_BRANCH(objectId = Scenery.BUDDING_BRANCH_3511, reward = Items.TEAM_29_CAPE_4372, respawn = -1),
        GOLDEN_PEAR_BUSH(objectId = Scenery.A_GOLDEN_PEAR_BUSH_3513, reward = Items.MORT_MYRE_PEAR_2974, respawn = -1),
        GLOWING_FUNGUS_0(objectId = Scenery.GLOWING_FUNGUS_4932, reward = Items.GLOWING_FUNGUS_4075, respawn = 30),
        GLOWING_FUNGUS_1(objectId = Scenery.GLOWING_FUNGUS_4933, reward = Items.GLOWING_FUNGUS_4075, respawn = 30),
        RARE_FLOWERS(objectId = Scenery.TROLLWEISS_FLOWERS_5006, reward = Items.FLOWERS_2460, respawn = 30),
        BLACK_MUSHROOMS(objectId = Scenery.BLACK_MUSHROOMS_6311, reward = Items.BLACK_MUSHROOM_4620, respawn = 30),
        KELP(objectId = Scenery.KELP_12478, reward = Items.KELP_7516, respawn = 30),
        RED_BANANA_TREE(objectId = Scenery.RED_BANANA_TREE_12609, reward = Items.RED_BANANA_7572, respawn = 30),
        BUSH(objectId = Scenery.BUSH_12615, reward = Items.TCHIKI_MONKEY_NUTS_7573, respawn = 30),
        RED_FLOWERS(objectId = Scenery.RED_FLOWERS_15846, reward = Items.RED_FLOWERS_8938, respawn = 30),
        BLUE_FLOWERS(objectId = Scenery.BLUE_FLOWERS_15872, reward = Items.BLUE_FLOWERS_8936, respawn = 30),
        HARDY_GOUTWEED(objectId = Scenery.HARDY_GOUTWEED_18855, reward = Items.GOUTWEED_3261, respawn = 30),
        HERBS_0(objectId = Scenery.HERBS_21668, reward = Items.GRIMY_GUAM_199, respawn = 30),
        HERBS_1(objectId = Scenery.HERBS_21669, reward = Items.GRIMY_MARRENTILL_201, respawn = 30),
        HERBS_2(objectId = Scenery.HERBS_21670, reward = Items.GRIMY_TARROMIN_203, respawn = 30),
        HERBS_3(objectId = Scenery.HERBS_21671, reward = Items.GRIMY_HARRALANDER_205, respawn = 30),
        FEVER_GRASS(objectId = Scenery.FEVER_GRASS_29113, reward = Items.FEVER_GRASS_12574, respawn = 30),
        LAVENDER(objectId = Scenery.LAVENDER_29114, reward = Items.LAVENDER_12572, respawn = 30),
        TANSYMUM(objectId = Scenery.TANSYMUM_29115, reward = Items.TANSYMUM_12576, respawn = 30),
        PRIMWEED(objectId = Scenery.PRIMWEED_29116, reward = Items.PRIMWEED_12588, respawn = 30),
        STINKBLOOM(objectId = Scenery.STINKBLOOM_29117, reward = Items.STINKBLOOM_12590, respawn = 30),
        TROLLWEISS_FLOWERS(objectId = Scenery.TROLLWEISS_FLOWERS_37328, reward = Items.TROLLWEISS_4086, respawn = 30),
        HOLLOW_LOG(objectId = Scenery.HOLLOW_LOG_37830, reward = Items.MUSHROOMS_10968, respawn = 30),
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
        private val ANIMATION = Animation(Animations.MULTI_BEND_OVER_827)
    }
}
