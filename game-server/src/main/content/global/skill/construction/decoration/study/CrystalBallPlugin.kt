package content.global.skill.construction.decoration.study

import core.api.addDialogueAction
import core.api.sendDialogueOptions
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class CrystalBallPlugin : InteractionListener {
    private enum class Staff(
        val staffId: Int,
        val start: Animation,
        val end: Animation,
        val cost: Item? = null,
        var type: StaffType,
    ) {
        STAFF_OF_AIR(Items.STAFF_OF_AIR_1381, Animation(Animations.STAFF_OF_AIR_4043), Animation(Animations.STAFF_OF_AIR_4044), type = StaffType.REGULAR),
        STAFF_OF_WATER(Items.STAFF_OF_WATER_1383, Animation(Animations.STAFF_OF_WATER_4047), Animation(Animations.STAFF_OF_WATER_4048), type = StaffType.REGULAR),
        STAFF_OF_EARTH(Items.STAFF_OF_EARTH_1385, Animation(Animations.STAFF_OF_EARTH_4045), Animation(Animations.STAFF_OF_EARTH_4046), type = StaffType.REGULAR),
        STAFF_OF_FIRE(Items.STAFF_OF_FIRE_1387, Animation(Animations.STAFF_OF_FIRE_4049), Animation(Animations.STAFF_OF_FIRE_4050), type = StaffType.REGULAR),

        AIR_BATTLESTAFF(Items.AIR_BATTLESTAFF_1397, Animation(Animations.AIR_BATTLESTAFF_4051), Animation(Animations.AIR_BATTLESTAFF_4052), Item(Items.AIR_RUNE_556, 100), type = StaffType.BATTLE),
        WATER_BATTLESTAFF(Items.WATER_BATTLESTAFF_1395, Animation(Animations.WATER_BATTLESTAFF_4055), Animation(Animations.WATER_BATTLESTAFF_4056), Item(Items.WATER_RUNE_555, 100), type = StaffType.BATTLE),
        EARTH_BATTLESTAFF(Items.EARTH_BATTLESTAFF_1399, Animation(Animations.EARTH_STAFF_4053), Animation(Animations.EARTH_STAFF_4054), Item(Items.EARTH_RUNE_557, 100), type = StaffType.BATTLE),
        FIRE_BATTLESTAFF(Items.FIRE_BATTLESTAFF_1393, Animation(Animations.FIRE_BATTLESTAFF_4057), Animation(Animations.FIRE_BATTLESTAFF_4058), Item(Items.FIRE_RUNE_554, 100), type = StaffType.BATTLE),

        MYSTIC_AIR_STAFF(Items.MYSTIC_AIR_STAFF_1405, Animation(Animations.MYSTIC_AIR_STAFF_4059), Animation(Animations.MYSTIC_AIR_STAFF_4060), Item(Items.AIR_RUNE_556, 1000), type = StaffType.MYSTIC),
        MYSTIC_WATER_STAFF(Items.MYSTIC_WATER_STAFF_1403, Animation(Animations.MYSTIC_WATER_STAFF_4063), Animation(Animations.MYSTIC_WATER_STAFF_4064), Item(Items.WATER_RUNE_555, 1000), type = StaffType.MYSTIC),
        MYSTIC_EARTH_STAFF(Items.MYSTIC_EARTH_STAFF_1407, Animation(Animations.MYSTIC_EARTH_STAFF_4061), Animation(Animations.MYSTIC_EARTH_STAFF_4062), Item(Items.EARTH_RUNE_557, 1000), type = StaffType.MYSTIC),
        MYSTIC_FIRE_STAFF(Items.MYSTIC_FIRE_STAFF_1401, Animation(Animations.MYSTIC_FIRE_STAFF_4065), Animation(Animations.MYSTIC_FIRE_STAFF_4066), Item(Items.FIRE_RUNE_554, 1000), type = StaffType.MYSTIC),
        ;

        companion object {
            val VALUES = values()
            val MAP = VALUES.associateBy { it.staffId }
            val ALL_STAFFS = VALUES.map { it.staffId }.toIntArray()

            fun getProduct(
                type: StaffType,
                element: String,
            ): Int? = VALUES.firstOrNull { it.type == type && it.name.contains(element, ignoreCase = true) }?.staffId

            fun getCost(staffId: Int): Item? = MAP[staffId]?.cost
        }
    }

    private enum class StaffType {
        REGULAR,
        BATTLE,
        MYSTIC,
    }

    private val crystalBallObjects =
        intArrayOf(
            Scenery.CRYSTAL_BALL_13659,
            Scenery.ELEMENTAL_SPHERE_13660,
            Scenery.CRYSTAL_OF_POWER_13661,
        )

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Staff.ALL_STAFFS, *crystalBallObjects) { player, staff, scenery ->
            val staff = Staff.MAP[staff.id] ?: return@onUseWith false
            val staffType = staff.type
            val requiredCost =
                when (scenery.id) {
                    Scenery.CRYSTAL_BALL_13659 ->
                        if (staffType == StaffType.REGULAR) {
                            null
                        } else {
                            sendMessage(player, "You can only change the element of basic staves on this crystal ball.")
                            return@onUseWith false
                        }
                    Scenery.ELEMENTAL_SPHERE_13660 ->
                        when (staffType) {
                            StaffType.REGULAR -> null
                            StaffType.BATTLE -> Item(Items.AIR_RUNE_556, 100)
                            else -> {
                                sendMessage(
                                    player,
                                    "You can only change the element of basic staves and battlestaves on this sphere.",
                                )
                                return@onUseWith false
                            }
                        }
                    Scenery.CRYSTAL_OF_POWER_13661 ->
                        when (staffType) {
                            StaffType.REGULAR -> null
                            StaffType.BATTLE -> Item(Items.AIR_RUNE_556, 100)
                            StaffType.MYSTIC -> Item(Items.AIR_RUNE_556, 1000)
                        }
                    else -> return@onUseWith false
                }

            handleElementSelection(player, staff, requiredCost)
            return@onUseWith true
        }
    }

    private fun handleElementSelection(
        player: Player,
        staff: Staff,
        cost: Item?,
    ) {
        val elements = arrayOf("Air", "Water", "Earth", "Fire")
        sendDialogueOptions(player, "Select an element", *elements)
        addDialogueAction(player) { _, buttonID ->
            val index = buttonID - 2
            if (index in elements.indices) {
                val selectedElement = elements[index]
                changeStaffElement(player, staff, selectedElement)
            }
        }
    }

    private fun changeStaffElement(
        player: Player,
        staff: Staff,
        element: String,
    ) {
        val elementRunes =
            mapOf(
                "Air" to Items.AIR_RUNE_556,
                "Water" to Items.WATER_RUNE_555,
                "Earth" to Items.EARTH_RUNE_557,
                "Fire" to Items.FIRE_RUNE_554,
            )

        val runeId = elementRunes[element] ?: return
        val product = Staff.getProduct(staff.type, element) ?: return
        val baseCost = staff.cost?.amount ?: 0

        val requiredRunes = if (baseCost > 0) Item(runeId, baseCost) else null

        if (requiredRunes != null && !player.inventory.contains(requiredRunes.id, requiredRunes.amount)) {
            sendMessage(player, "You need ${requiredRunes.amount} ${element.lowercase()} runes to change this staff.")
            return
        }

        player.lock(3)
        player.animate(staff.start)
        player.animate(staff.end, 2)

        requiredRunes?.let { player.inventory.remove(it) }
        player.inventory.remove(Item(staff.staffId))
        player.inventory.add(Item(product))

        sendMessage(player, "The staff feels very light for a moment.")
    }
}
