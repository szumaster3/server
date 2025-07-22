package content.minigame.mta.plugin

import core.api.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds
import org.rs.consts.Graphics as Graphic

/**
 * Represents configuration of jewelry enchantment spells.
 */
enum class EnchantSpellDefinition(
    val buttonId: Int,
    val level: Int,
    val experience: Double,
    val jewellery: Map<Int, Item>,
    val runes: Array<Item?>
) {
    SAPPHIRE(
        5, 7, 17.5,
        jewellery = mapOf(
            Items.SAPPHIRE_RING_1637 to Item(Items.RING_OF_RECOIL_2550),
            Items.SAPPHIRE_NECKLACE_1656 to Item(Items.GAMES_NECKLACE8_3853),
            Items.SAPPHIRE_AMULET_1694 to Item(Items.AMULET_OF_MAGIC_1727),
            Items.SAPPHIRE_BRACELET_11072 to Item(Items.BRACELET_OF_CLAY_11074)
        ),
        runes = arrayOf(Item(Items.COSMIC_RUNE_564, 1), Item(Items.WATER_RUNE_555, 1))
    ),
    EMERALD(16, 27, 37.0,
        jewellery = mapOf(
            Items.EMERALD_RING_1639 to Item(Items.RING_OF_DUELLING8_2552),
            Items.EMERALD_NECKLACE_1658 to Item(Items.BINDING_NECKLACE_5521),
            Items.EMERALD_AMULET_1696 to Item(Items.AMULET_OF_DEFENCE_1729),
            Items.EMERALD_BRACELET_11076 to Item(Items.CASTLEWAR_BRACE3_11079)
        ),
        runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.AIR_RUNE.id, 3))
    ),
    RUBY(
        28, 49, 59.0,
        jewellery = mapOf(
            Items.RUBY_RING_1641 to Item(Items.RING_OF_FORGING_2568),
            Items.RUBY_NECKLACE_1660 to Item(Items.DIGSITE_PENDANT_5_11194),
            Items.RUBY_AMULET_1698 to Item(Items.AMULET_OF_STRENGTH_1725),
            Items.RUBY_BRACELET_11085 to Item(Items.INOCULATION_BRACE_11088)
        ),
        runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 5))
    ),
    DIAMOND(
        36, 57, 67.0,
        jewellery = mapOf(
            Items.DIAMOND_RING_1643 to Item(Items.RING_OF_LIFE_2570),
            Items.DIAMOND_NECKLACE_1662 to Item(Items.PHOENIX_NECKLACE_11090),
            Items.DIAMOND_AMULET_1700 to Item(Items.AMULET_OF_POWER_1731),
            Items.DIAMOND_BRACELET_11092 to Item(Items.FORINTHRY_BRACE5_11095)
        ),
        runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 10))
    ),
    DRAGON(
        51, 68, 78.0,
        jewellery = mapOf(
            Items.DRAGONSTONE_RING_1645 to Item(Items.RING_OF_WEALTH_2572),
            Items.DRAGON_NECKLACE_1664 to Item(Items.SKILLS_NECKLACE4_11105),
            Items.DRAGONSTONE_AMMY_1702 to Item(Items.AMULET_OF_GLORY4_1712),
            Items.DRAGON_BRACELET_11115 to Item(Items.COMBAT_BRACELET4_11118)
        ),
        runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.WATER_RUNE.id, 15), Item(Runes.EARTH_RUNE.id, 15))
    ),
    ONYX(
        61, 87, 97.0,
        jewellery = mapOf(
            Items.ONYX_RING_6575 to Item(Items.RING_OF_STONE_6583),
            Items.ONYX_NECKLACE_6577 to Item(Items.BERSERKER_NECKLACE_11128),
            Items.ONYX_AMULET_6581 to Item(Items.AMULET_OF_FURY_6585),
            Items.ONYX_BRACELET_11130 to Item(Items.REGEN_BRACELET_11133)
        ),
        runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 20), Item(Runes.EARTH_RUNE.id, 20))
    );

    companion object {
        val orbs =
            arrayOf(
                Items.CUBE_6899,
                Items.CYLINDER_6898,
                Items.ICOSAHEDRON_6900,
                Items.PENTAMID_6901,
                Items.DRAGONSTONE_6903
            )
                .associateWith { Item(Items.ORB_6902) }
    }
}

/**
 * Represents the enchant spell effects.
 */
enum class EnchantSpellEffect(
    val itemId: Int,
    val animation: Int,
    val graphic: Int,
    val sound: Int
) {
    SAPPHIRE_AMULET(Items.SAPPHIRE_AMULET_1694, Animations.ENCHANT_JEWEL_719, Graphic.ENCHANT_1_114, Sounds.ENCHANT_SAPPHIRE_AMULET_136),
    SAPPHIRE_NECKLACE(Items.SAPPHIRE_NECKLACE_1656, Animations.ENCHANT_JEWEL_719, Graphic.ENCHANT_1_114, Sounds.ENCHANT_SAPPHIRE_AMULET_136),
    SAPPHIRE_RING(Items.SAPPHIRE_RING_1637, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_SAPPHIRE_RING_147),
    SAPPHIRE_BRACELET(Items.SAPPHIRE_BRACELET_11072, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_SAPPHIRE_RING_147),

    EMERALD_AMULET(Items.EMERALD_AMULET_1696, Animations.ENCHANT_JEWEL_719, Graphic.ENCHANT_1_114, Sounds.ENCHANT_EMERALD_AMULET_141),
    EMERALD_NECKLACE(Items.EMERALD_NECKLACE_1658, Animations.ENCHANT_JEWEL_719, Graphic.ENCHANT_1_114, Sounds.ENCHANT_EMERALD_AMULET_141),
    EMERALD_RING(Items.EMERALD_RING_1639, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_EMERALD_RING_142),
    EMERALD_BRACELET(Items.EMERALD_BRACELET_11076, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_EMERALD_RING_142),

    RUBY_AMULET(Items.RUBY_AMULET_1698, Animations.ENCHANT_JEWEL_720, Graphic.ENCHANT_2_115, Sounds.ENCHANT_RUBY_AMULET_145),
    RUBY_NECKLACE(Items.RUBY_NECKLACE_1660, Animations.ENCHANT_JEWEL_720, Graphic.ENCHANT_2_115, Sounds.ENCHANT_RUBY_AMULET_145),
    RUBY_RING(Items.RUBY_RING_1641, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_RUBY_RING_146),
    RUBY_BRACELET(Items.RUBY_BRACELET_11085, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_RUBY_RING_146),

    DIAMOND_AMULET(Items.DIAMOND_AMULET_1700, Animations.ENCHANT_JEWEL_720, Graphic.ENCHANT_2_115, Sounds.ENCHANT_DIAMOND_AMULET_137),
    DIAMOND_NECKLACE(Items.DIAMOND_NECKLACE_1662, Animations.ENCHANT_JEWEL_720, Graphic.ENCHANT_2_115, Sounds.ENCHANT_DIAMOND_AMULET_137),
    DIAMOND_RING(Items.DIAMOND_RING_1643, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_DIAMOND_RING_138),
    DIAMOND_BRACELET(Items.DIAMOND_BRACELET_11092, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_DIAMOND_RING_138),

    DRAGON_AMULET(Items.DRAGONSTONE_AMMY_1702, Animations.ENCHANT_JEWEL_721, Graphic.ENCHANT_3_116, Sounds.ENCHANT_DRAGON_AMULET_139),
    DRAGON_NECKLACE(Items.DRAGON_NECKLACE_1664, Animations.ENCHANT_JEWEL_721, Graphic.ENCHANT_3_116, Sounds.ENCHANT_DRAGON_AMULET_139),
    DRAGON_RING(Items.DRAGONSTONE_RING_1645, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_DRAGON_RING_140),
    DRAGON_BRACELET(Items.DRAGON_BRACELET_11115, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_DRAGON_RING_140),

    ONYX_AMULET(Items.ONYX_AMULET_6581, Animations.ENCHANT_JEWEL_721, Graphic.ONYX_AMMY_ENCHANTING_452, Sounds.ENCHANT_ONYX_AMULET_143),
    ONYX_NECKLACE(Items.ONYX_NECKLACE_6565, Animations.ENCHANT_JEWEL_721, Graphic.ONYX_AMMY_ENCHANTING_452,Sounds.ENCHANT_ONYX_AMULET_143),
    ONYX_RING(Items.ONYX_RING_6575, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_ONYX_RING_144),
    ONYX_BRACELET(Items.ONYX_BRACELET_11130, Animations.ENCHANT_JEWEL_722, Graphic.ENCHANT_RING_238, Sounds.ENCHANT_ONYX_RING_144);

    companion object {
        private val lookup = values().associateBy(EnchantSpellEffect::itemId)
        fun fromItemId(id: Int): EnchantSpellEffect? = lookup[id]
    }
}

/**
 * Represents the Enchant spell for MTA.
 */
class EnchantSpell(
    level: Int = 0,
    experience: Double = 0.0,
    private val jewellery: Map<Int, Item> = emptyMap(),
    runes: Array<Item?>? = null,
) : MagicSpell(SpellBookManager.SpellBook.MODERN, level, experience, null, null, Audio(-1, 1, 0), runes) {

    override fun cast(entity: Entity, target: Node): Boolean {
        if (entity !is Player || target !is Item) return false
        entity.interfaceManager.setViewedTab(6)
        val enchanted = jewellery[target.id]

        if (enchanted == null) {
            sendMessage(entity, "You can't use this spell on this item.")
            return false
        }

        if (!meetsRequirements(caster = entity, message = true, remove = true)) return false
        val effect = EnchantSpellEffect.fromItemId(target.id) ?: return false

        if (removeItem(entity, target)) {
            playAudio(entity, effect.sound)
            visualize(entity, effect.animation, Graphics(effect.graphic, 92))
            entity.inventory.add(enchanted)
        }

        if (inZone(entity, "Enchantment Chamber")) {
            entity.graphics(Graphics.create(237, 110))
            val pizazz = calculatePizazz(entity, target)
            if (pizazz != 0) {
                EnchantmentChamberPlugin.ZONE.incrementPoints(entity, MTAType.ENCHANTERS.ordinal, pizazz)
            }
        }

        return true
    }

    private fun calculatePizazz(entity: Entity, target: Node): Int {
        val spellPoints = when (spellId) {
            5 -> 1
            16 -> 2
            28 -> 3
            36 -> 4
            51 -> 5
            else -> 6
        }

        return if (target.id == 6903) {
            spellPoints * 2
        } else {
            val shape = EnchantmentChamberPlugin.Shapes.forItem(target.asItem())
            var pizazz = 0
            if (shape != null) {
                var convert = entity.getAttribute("mta-convert", 0) + 1
                if (convert >= 10) {
                    pizazz = spellPoints
                    convert = 0
                }
                entity.setAttribute("mta-convert", convert)
                if (shape == EnchantmentChamberPlugin.BONUS_SHAPE) {
                    pizazz += 1
                    sendMessage(entity.asPlayer(), "You get $pizazz bonus point${if (pizazz != 1) "s" else ""}!")
                }
            }
            pizazz
        }
    }

    override val delay: Int get() = super.delay

    override fun getExperience(player: Player): Double =
        if (player.zoneMonitor.isInZone("Enchantment Chamber")) experience * 0.75
        else experience

    override fun newInstance(arg: SpellType?): Plugin<SpellType?>? {
        EnchantSpellDefinition.values().forEach { def ->
            SpellBookManager.SpellBook.MODERN.register(
                buttonId = def.buttonId,
                spell = EnchantSpell(
                    def.level,
                    def.experience,
                    def.jewellery + EnchantSpellDefinition.orbs,
                    def.runes
                )
            )
        }
        return this
    }
}
