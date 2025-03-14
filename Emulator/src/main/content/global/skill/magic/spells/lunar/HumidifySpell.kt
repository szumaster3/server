package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.node.item.Item
import core.game.system.command.Privilege
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.Items
import org.rs.consts.Sounds

class HumidifySpell :
    SpellListener("lunar"),
    Commands {
    override fun defineListeners() {
        onCast(LunarSpells.HUMIDIFY, NONE) { player, _ ->
            requires(
                player,
                68,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 1), Item(Items.WATER_RUNE_555, 3), Item(Items.FIRE_RUNE_554, 1)),
            )
            val playerEmpties = ArrayDeque<Item>()

            for (item in player.inventory.toArray()) {
                if (item == null) continue
                if (!HumidifyItems.emptyContains(item.id)) continue
                playerEmpties.add(item)
            }

            if (playerEmpties.isEmpty()) {
                sendMessage(player, "You have nothing in your inventory that this spell can humidify.")
                return@onCast
            }

            removeRunes(player)
            delayEntity(player, Animation(Animations.LUNAR_HUMIDIFY_6294).duration)
            visualizeSpell(
                player,
                Animations.LUNAR_HUMIDIFY_6294,
                Graphics.HUMIDIFY_GFX_1061,
                20,
                Sounds.LUNAR_HUMIDIFY_3614,
            )
            for (item in playerEmpties) {
                val filled = HumidifyItems.forId(item.id)
                removeItem(player, item.id) && addItem(player, filled)
            }
            addXP(player, 65.0)
        }
    }

    private enum class HumidifyItems(
        val empty: Int,
        val full: Int,
    ) {
        VIAL(Items.VIAL_229, Items.VIAL_OF_WATER_227),
        WATERSKIN0(Items.WATERSKIN0_1831, Items.WATERSKIN4_1823),
        WATERSKIN1(Items.WATERSKIN1_1829, Items.WATERSKIN4_1823),
        WATERSKIN2(Items.WATERSKIN2_1827, Items.WATERSKIN4_1823),
        WATERSKIN3(Items.WATERSKIN3_1825, Items.WATERSKIN4_1823),
        BUCKET(Items.BUCKET_1925, Items.BUCKET_OF_WATER_1929),
        BOWL(Items.BOWL_1923, Items.BOWL_OF_WATER_1921),
        JUG(Items.JUG_1935, Items.JUG_OF_WATER_1937),
        WATERING_CAN0(Items.WATERING_CAN_5331, Items.WATERING_CAN8_5340),
        WATERING_CAN1(Items.WATERING_CAN1_5333, Items.WATERING_CAN8_5340),
        WATERING_CAN2(Items.WATERING_CAN2_5334, Items.WATERING_CAN8_5340),
        WATERING_CAN3(Items.WATERING_CAN3_5335, Items.WATERING_CAN8_5340),
        WATERING_CAN4(Items.WATERING_CAN4_5336, Items.WATERING_CAN8_5340),
        WATERING_CAN5(Items.WATERING_CAN5_5337, Items.WATERING_CAN8_5340),
        WATERING_CAN6(Items.WATERING_CAN6_5338, Items.WATERING_CAN8_5340),
        WATERING_CAN7(Items.WATERING_CAN7_5339, Items.WATERING_CAN8_5340),
        FISHBOWL(Items.FISHBOWL_6667, Items.FISHBOWL_6668),
        KETTLE(Items.KETTLE_7688, Items.FULL_KETTLE_7690),
        ENCHANTED_VIAL(Items.ENCHANTED_VIAL_731, Items.HOLY_WATER_732),
        CUP(Items.EMPTY_CUP_1980, Items.CUP_OF_WATER_4458),
        ;

        companion object {
            private val productOfFill = values().associate { it.empty to it.full }

            fun forId(id: Int): Int {
                return productOfFill[id]!!
            }

            fun emptyContains(id: Int): Boolean {
                return productOfFill.contains(id)
            }
        }
    }

    override fun defineCommands() {
        define("humidifykit", privilege = Privilege.ADMIN) { player, _ ->
            if (freeSlots(player) < 24) {
                sendMessage(player, "Not enough free space.")
                return@define
            } else {
                addItem(player, Items.ASTRAL_RUNE_9075, 100)
                addItem(player, Items.WATER_RUNE_555, 300)
                addItem(player, Items.FIRE_RUNE_554, 100)
                for (item in HumidifyItems.values()) {
                    addItem(player, item.empty)
                }
            }
        }
    }
}
