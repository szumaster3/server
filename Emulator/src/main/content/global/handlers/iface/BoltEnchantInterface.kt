package content.global.handlers.iface

import content.global.skill.magic.SpellUtils
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Sounds

class BoltEnchantInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.XBOWS_ENCHANT_BOLT_432) { player, _, _, buttonID, _, itemID ->
            val bolts = Bolts.boltMap[buttonID] ?: return@on true
            val staff = SpellUtils.usingStaff(player, itemID)
            closeInterface(player)

            if (getStatLevel(player, Skills.MAGIC) < bolts.level) {
                sendMessage(player, "You need a level of " + bolts.level + " to cast this spell.")
                return@on true
            }

            for (item in bolts.runes) {
                if (!inInventory(player, item.id) && !staff) {
                    sendMessage(player, "You do not have enough runes to cast this spell.")
                    return@on true
                }
            }

            val amount = amountInInventory(player, bolts.bolt)
            if (amount < 1) {
                sendMessage(player, "You don't have any bolts to enchant.")
                return@on true
            }
            val add = Item(bolts.enchanted)
            if (amount > 10 && !hasSpaceFor(player, add)) {
                sendMessage(player, "Not enough space in your inventory.")
                return@on true
            }
            visualize(player, Animations.CRAFT_ENCHANT_4462, 759)
            queueScript(player, 1, QueueStrength.SOFT) {
                var enchant = amount
                if (enchant > 10) {
                    enchant = 10
                }
                add.amount = enchant
                removeItem(player, Item(bolts.bolt, enchant))
                addItem(player, bolts.enchanted, enchant)
                for (item in bolts.runes) {
                    if (!inInventory(player, item.id) && !staff) {
                        sendMessage(player, "You do not have enough " + item.name + "s to cast this spell.")
                        return@queueScript stopExecuting(player)
                    }
                    if (!staff) {
                        removeItem(player, item)
                    }
                }
                rewardXP(player, Skills.MAGIC, bolts.exp)
                playAudio(player, Sounds.ENCHANTED_TIPPING_2921)
                sendMessage(player, "The magic of the runes coaxes out the true nature of the gem tips.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }

    enum class Bolts(
        val button: Int,
        val bolt: Int,
        val level: Int,
        val runes: Array<Item>,
        val exp: Double,
        val enchanted: Int,
    ) {
        OPAL(
            button = 14,
            bolt = Items.OPAL_BOLTS_879,
            level = 4,
            runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.AIR_RUNE.id, 2)),
            exp = 9.0,
            enchanted = Items.OPAL_BOLTS_E_9236,
        ),
        SAPPHIRE(
            button = 29,
            bolt = Items.SAPPHIRE_BOLTS_9337,
            level = 7,
            runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.MIND_RUNE.id, 1), Item(Runes.WATER_RUNE.id, 1)),
            exp = 17.0,
            enchanted = Items.SAPPHIRE_BOLTS_E_9240,
        ),
        JADE(
            button = 18,
            bolt = Items.JADE_BOLTS_9335,
            level = 14,
            runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 2)),
            exp = 19.0,
            enchanted = Items.JADE_BOLTS_E_9237,
        ),
        PEARL(
            button = 22,
            bolt = Items.PEARL_BOLTS_880,
            level = 24,
            runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.WATER_RUNE.id, 2)),
            exp = 29.0,
            enchanted = Items.PEARL_BOLTS_E_9238,
        ),
        EMERALD(
            button = 32,
            bolt = Items.EMERALD_BOLTS_9338,
            level = 27,
            runes = arrayOf(Item(Runes.NATURE_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.AIR_RUNE.id, 3)),
            exp = 37.0,
            enchanted = Items.EMERALD_BOLTS_E_9241,
        ),
        RED_TOPAZ(
            button = 26,
            bolt = Items.TOPAZ_BOLTS_9336,
            level = 29,
            runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 2)),
            exp = 33.0,
            enchanted = Items.TOPAZ_BOLTS_E_9239,
        ),
        RUBY(
            button = 35,
            bolt = Items.RUBY_BOLTS_9339,
            level = 49,
            runes = arrayOf(Item(Runes.BLOOD_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 5)),
            exp = 59.0,
            enchanted = Items.RUBY_BOLTS_E_9242,
        ),
        DIAMOND(
            button = 38,
            bolt = Items.DIAMOND_BOLTS_9340,
            level = 57,
            runes = arrayOf(Item(Runes.LAW_RUNE.id, 2), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 10)),
            exp = 67.0,
            enchanted = Items.DIAMOND_BOLTS_E_9243,
        ),
        DRAGONSTONE(
            button = 41,
            bolt = Items.DRAGON_BOLTS_9341,
            level = 68,
            runes = arrayOf(Item(Runes.SOUL_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 15)),
            exp = 78.0,
            enchanted = Items.DRAGON_BOLTS_E_9244,
        ),
        ONYX(
            button = 44,
            bolt = Items.ONYX_BOLTS_9342,
            level = 87,
            runes = arrayOf(Item(Runes.DEATH_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 20)),
            exp = 97.0,
            enchanted = Items.ONYX_BOLTS_E_9245,
        ),
        ;

        companion object {
            val boltMap = HashMap<Int, Bolts>()

            init {
                for (bolts in values()) {
                    boltMap[bolts.button] = bolts
                }
            }
        }
    }
}
