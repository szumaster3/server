package content.global.skill.construction.decoration.study

import core.api.addDialogueAction
import core.api.sendOptions
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class CrystalBallPlugin : InteractionListener {

    private enum class Element(val runeId: Int) {
        AIR(Items.AIR_RUNE_556),
        WATER(Items.WATER_RUNE_555),
        EARTH(Items.EARTH_RUNE_557),
        FIRE(Items.FIRE_RUNE_554);

        override fun toString(): String = name.lowercase().replaceFirstChar { it.uppercase() }
    }

    private enum class StaffType {
        REGULAR, BATTLE, MYSTIC;

        /**
         * Gets the cost (if required) for a given type of staff on the selected object.
         */
        fun getCostFor(sceneryId: Int): Item? = when (sceneryId) {
            Scenery.CRYSTAL_BALL_13659 -> if (this == REGULAR) null else null
            Scenery.ELEMENTAL_SPHERE_13660 -> when (this) {
                REGULAR -> null
                BATTLE -> Item(Items.AIR_RUNE_556, 100)
                MYSTIC -> null
            }
            Scenery.CRYSTAL_OF_POWER_13661 -> when (this) {
                REGULAR -> null
                BATTLE -> Item(Items.AIR_RUNE_556, 100)
                MYSTIC -> Item(Items.AIR_RUNE_556, 1000)
            }
            else -> null
        }

        fun isAllowedOn(sceneryId: Int): Boolean = getCostFor(sceneryId) != null || this == REGULAR
    }

    private enum class Staff(
        val staffId: Int,
        val element: Element,
        val type: StaffType,
        val start: Animation,
        val end: Animation,
        val baseCost: Item? = null,
    ) {
        STAFF_OF_AIR(Items.STAFF_OF_AIR_1381, Element.AIR, StaffType.REGULAR, Animation(Animations.STAFF_OF_AIR_4043), Animation(Animations.STAFF_OF_AIR_4044)),
        STAFF_OF_WATER(Items.STAFF_OF_WATER_1383, Element.WATER, StaffType.REGULAR, Animation(Animations.STAFF_OF_WATER_4047), Animation(Animations.STAFF_OF_WATER_4048)),
        STAFF_OF_EARTH(Items.STAFF_OF_EARTH_1385, Element.EARTH, StaffType.REGULAR, Animation(Animations.STAFF_OF_EARTH_4045), Animation(Animations.STAFF_OF_EARTH_4046)),
        STAFF_OF_FIRE(Items.STAFF_OF_FIRE_1387, Element.FIRE, StaffType.REGULAR, Animation(Animations.STAFF_OF_FIRE_4049), Animation(Animations.STAFF_OF_FIRE_4050)),

        AIR_BATTLESTAFF(Items.AIR_BATTLESTAFF_1397, Element.AIR, StaffType.BATTLE, Animation(Animations.AIR_BATTLESTAFF_4051), Animation(Animations.AIR_BATTLESTAFF_4052), Item(Items.AIR_RUNE_556, 100)),
        WATER_BATTLESTAFF(Items.WATER_BATTLESTAFF_1395, Element.WATER, StaffType.BATTLE, Animation(4059), Animation(4060), Item(Items.WATER_RUNE_555, 100)),
        EARTH_BATTLESTAFF(Items.EARTH_BATTLESTAFF_1399, Element.EARTH, StaffType.BATTLE, Animation(4055), Animation(4056), Item(Items.EARTH_RUNE_557, 100)),
        FIRE_BATTLESTAFF(Items.FIRE_BATTLESTAFF_1393, Element.FIRE, StaffType.BATTLE, Animation(Animations.FIRE_BATTLESTAFF_4057), Animation(Animations.FIRE_BATTLESTAFF_4058), Item(Items.FIRE_RUNE_554, 100)),

        MYSTIC_AIR_STAFF(Items.MYSTIC_AIR_STAFF_1405, Element.AIR, StaffType.MYSTIC, Animation(4061), Animation(4062), Item(Items.AIR_RUNE_556, 1000)),
        MYSTIC_WATER_STAFF(Items.MYSTIC_WATER_STAFF_1403, Element.WATER, StaffType.MYSTIC, Animation(4069), Animation(4070), Item(Items.WATER_RUNE_555, 1000)),
        MYSTIC_EARTH_STAFF(Items.MYSTIC_EARTH_STAFF_1407, Element.EARTH, StaffType.MYSTIC, Animation(4065), Animation(4066), Item(Items.EARTH_RUNE_557, 1000)),
        MYSTIC_FIRE_STAFF(Items.MYSTIC_FIRE_STAFF_1401, Element.FIRE, StaffType.MYSTIC, Animation(4071), Animation(4072), Item(Items.FIRE_RUNE_554, 1000));

        companion object {
            val MAP = values().associateBy { it.staffId }
            val IDS = values().map { it.staffId }.toIntArray()

            fun productFor(type: StaffType, element: Element): Int? =
                values().firstOrNull { it.type == type && it.element == element }?.staffId
        }
    }

    private val crystalBallObjects = intArrayOf(
        Scenery.CRYSTAL_BALL_13659,
        Scenery.ELEMENTAL_SPHERE_13660,
        Scenery.CRYSTAL_OF_POWER_13661
    )

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Staff.IDS, *crystalBallObjects) { player, item, scenery ->
            val staff = Staff.MAP[item.id] ?: return@onUseWith false

            if (!staff.type.isAllowedOn(scenery.id)) {
                sendMessage(player, "You cannot change this type of staff on this device.")
                return@onUseWith false
            }

            val cost = staff.type.getCostFor(scenery.id)
            handleElementSelection(player, staff, cost)
            return@onUseWith true
        }
    }

    private fun handleElementSelection(player: Player, staff: Staff, cost: Item?) {
        sendOptions(player, "Select an element", *Element.values().map { it.toString() }.toTypedArray())
        addDialogueAction(player) { _, buttonID ->
            val index = buttonID - 2
            if (index in Element.values().indices) {
                val element = Element.values()[index]
                changeStaffElement(player, staff, element)
            }
        }
    }

    private fun changeStaffElement(player: Player, staff: Staff, element: Element) {
        val productId = Staff.productFor(staff.type, element) ?: return
        val baseCost = staff.baseCost?.amount ?: 0
        val requiredRunes = if (baseCost > 0) Item(element.runeId, baseCost) else null

        if (requiredRunes != null && !player.inventory.contains(requiredRunes.id, requiredRunes.amount)) {
            sendMessage(player, "You need ${requiredRunes.amount} ${element.toString().lowercase()} runes to change this staff.")
            return
        }

        player.lock(3)
        player.animate(staff.start)
        player.animate(staff.end, 2)

        requiredRunes?.let { player.inventory.remove(it) }
        player.inventory.remove(Item(staff.staffId))
        player.inventory.add(Item(productId))

        sendMessage(player, "The staff feels very light for a moment.")
    }
}
