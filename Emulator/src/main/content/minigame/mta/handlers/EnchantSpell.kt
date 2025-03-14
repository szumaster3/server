package content.minigame.mta.handlers

import content.minigame.mta.handlers.room.EnchantmentChamber
import core.api.inZone
import core.api.playAudio
import core.api.removeItem
import core.api.sendMessage
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

class EnchantSpell : MagicSpell {
    private val jewellery: Map<Int, Item>?

    constructor() {
        jewellery = null
    }

    constructor(level: Int, experience: Double, jewellery: Map<Int, Item>, runes: Array<Item?>?) : super(
        SpellBookManager.SpellBook.MODERN,
        level,
        experience,
        null,
        null,
        Audio(-1, 1, 0),
        runes,
    ) {
        this.jewellery = jewellery
    }

    private val spellDataMap =
        mapOf(
            Items.SAPPHIRE_RING_1637 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_SAPPHIRE_RING_147,
                ),
            Items.SAPPHIRE_BRACELET_11072 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_SAPPHIRE_RING_147,
                ),
            Items.EMERALD_RING_1639 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_EMERALD_RING_142,
                ),
            Items.EMERALD_BRACELET_11076 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_EMERALD_RING_142,
                ),
            Items.RUBY_RING_1641 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_RUBY_RING_146,
                ),
            Items.RUBY_BRACELET_11085 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_RUBY_RING_146,
                ),
            Items.DIAMOND_RING_1643 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_DIAMOND_RING_138,
                ),
            Items.DIAMOND_BRACELET_11092 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_DIAMOND_RING_138,
                ),
            Items.DRAGONSTONE_RING_1645 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_DRAGON_RING_140,
                ),
            Items.DRAGON_BRACELET_11115 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_DRAGON_RING_140,
                ),
            Items.ONYX_RING_6575 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_ONYX_RING_144,
                ),
            Items.ONYX_BRACELET_11130 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_722,
                    graphic = org.rs.consts.Graphics.ENCHANT_RING_238,
                    sound = Sounds.ENCHANT_ONYX_RING_144,
                ),
            Items.SAPPHIRE_NECKLACE_1656 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_719,
                    graphic = org.rs.consts.Graphics.ENCHANT_1_114,
                    sound = Sounds.ENCHANT_SAPPHIRE_AMULET_136,
                ),
            Items.SAPPHIRE_AMULET_1694 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_719,
                    graphic = org.rs.consts.Graphics.ENCHANT_1_114,
                    sound = Sounds.ENCHANT_SAPPHIRE_AMULET_136,
                ),
            Items.EMERALD_NECKLACE_1658 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_719,
                    graphic = org.rs.consts.Graphics.ENCHANT_1_114,
                    sound = Sounds.ENCHANT_EMERALD_AMULET_141,
                ),
            Items.EMERALD_AMULET_1696 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_719,
                    graphic = org.rs.consts.Graphics.ENCHANT_1_114,
                    sound = Sounds.ENCHANT_EMERALD_AMULET_141,
                ),
            Items.RUBY_NECKLACE_1660 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_720,
                    graphic = org.rs.consts.Graphics.ENCHANT_2_115,
                    sound = Sounds.ENCHANT_RUBY_AMULET_145,
                ),
            Items.RUBY_AMULET_1698 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_720,
                    graphic = org.rs.consts.Graphics.ENCHANT_2_115,
                    sound = Sounds.ENCHANT_RUBY_AMULET_145,
                ),
            Items.DIAMOND_NECKLACE_1662 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_720,
                    graphic = org.rs.consts.Graphics.ENCHANT_2_115,
                    sound = Sounds.ENCHANT_DIAMOND_AMULET_137,
                ),
            Items.DIAMOND_AMULET_1700 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_720,
                    graphic = org.rs.consts.Graphics.ENCHANT_2_115,
                    sound = Sounds.ENCHANT_DIAMOND_AMULET_137,
                ),
            Items.DRAGON_NECKLACE_1664 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_721,
                    graphic = org.rs.consts.Graphics.ENCHANT_3_116,
                    sound = Sounds.ENCHANT_DRAGON_AMULET_139,
                ),
            Items.DRAGONSTONE_AMMY_1702 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_721,
                    graphic = org.rs.consts.Graphics.ENCHANT_3_116,
                    sound = Sounds.ENCHANT_DRAGON_AMULET_139,
                ),
            Items.ONYX_AMULET_6581 to
                SpellData(
                    animation = Animations.ENCHANT_JEWEL_721,
                    graphic = org.rs.consts.Graphics.ONYX_AMMY_ENCHANTING_452,
                    sound = Sounds.ENCHANT_ONYX_AMULET_143,
                ),
        )

    override fun cast(
        entity: Entity,
        target: Node,
    ): Boolean {
        if (target !is Item || entity !is Player) {
            return false
        }

        entity.interfaceManager.setViewedTab(6)

        val enchanted =
            jewellery?.getOrDefault(target.id, null) ?: run {
                sendMessage(entity, "You can't use this spell on this item.")
                return false
            }

        if (!meetsRequirements(caster = entity, message = true, remove = true)) {
            return false
        }

        val effect = spellDataMap[target.id] ?: return false

        playAudio(entity.asPlayer(), effect.sound)

        if (removeItem(entity, target)) {
            core.api.visualize(
                entity = entity.asPlayer(),
                anim = effect.animation,
                gfx = Graphics(effect.graphic, 92),
            )
            entity.inventory.add(enchanted)
        }

        handleMTAZone(entity, target)

        return true
    }

    private fun handleMTAZone(
        entity: Entity,
        target: Node,
    ) {
        if (inZone(entity, "Enchantment Chamber")) {
            entity.graphics(Graphics.create(237, 110))
            var pizazz = calculatePizazz(entity, target)
            if (pizazz != 0) {
                EnchantmentChamber.ZONE.incrementPoints(entity.asPlayer(), MTAType.ENCHANTERS.ordinal, pizazz)
            }
        }
    }

    private fun calculatePizazz(
        entity: Entity,
        target: Node,
    ): Int {
        var pizazz = 0
        if (target.id == 6903) {
            pizazz = when (spellId) {
                5 -> 1
                16 -> 2
                28 -> 3
                36 -> 4
                51 -> 5
                else -> 6
            } * 2
        } else {
            val shape = EnchantmentChamber.Shapes.forItem(target.asItem())
            if (shape != null) {
                var convert = entity.getAttribute("mta-convert", 0)
                convert += 1
                if (convert >= 10) {
                    pizazz =
                        when (spellId) {
                            5 -> 1
                            16 -> 2
                            28 -> 3
                            36 -> 4
                            51 -> 5
                            else -> 6
                        }
                    convert = 0
                }
                entity.setAttribute("mta-convert", convert)
                if (shape == EnchantmentChamber.BONUS_SHAPE) {
                    pizazz += 1
                    sendMessage(entity.asPlayer(), "You get $pizazz bonus point${if (pizazz != 1) "s" else ""}!")
                }
            }
        }
        return pizazz
    }

    override val delay: Int
        get() = super.delay

    override fun getExperience(player: Player): Double {
        return if (player.zoneMonitor.isInZone("Enchantment Chamber")) {
            experience - experience * 0.25
        } else {
            experience
        }
    }

    override fun newInstance(arg: SpellType?): Plugin<SpellType?>? {
        SpellBookManager.SpellBook.MODERN.register(
            buttonId = 5,
            spell =
                EnchantSpell(
                    level = 7,
                    experience = 17.5,
                    jewellery =
                        mapOf(
                            Items.SAPPHIRE_RING_1637 to Item(Items.RING_OF_RECOIL_2550),
                            Items.SAPPHIRE_NECKLACE_1656 to Item(Items.GAMES_NECKLACE8_3853),
                            Items.SAPPHIRE_AMULET_1694 to Item(Items.AMULET_OF_MAGIC_1727),
                            Items.SAPPHIRE_BRACELET_11072 to Item(Items.BRACELET_OF_CLAY_11074),
                            Items.CUBE_6899 to Item(Items.ORB_6902),
                            Items.CYLINDER_6898 to Item(Items.ORB_6902),
                            Items.ICOSAHEDRON_6900 to Item(Items.ORB_6902),
                            Items.PENTAMID_6901 to Item(Items.ORB_6902),
                            Items.DRAGONSTONE_6903 to Item(Items.ORB_6902),
                        ),
                    runes = arrayOf(Item(Items.COSMIC_RUNE_564, 1), Item(Items.WATER_RUNE_555, 1)),
                ),
        )
        SpellBookManager.SpellBook.MODERN.register(
            buttonId = 16,
            spell =
                EnchantSpell(
                    level = 27,
                    experience = 37.0,
                    jewellery =
                        mapOf(
                            Items.EMERALD_RING_1639 to Item(Items.RING_OF_DUELLING8_2552),
                            Items.EMERALD_NECKLACE_1658 to Item(Items.BINDING_NECKLACE_5521),
                            Items.EMERALD_AMULET_1696 to Item(Items.AMULET_OF_DEFENCE_1729),
                            Items.EMERALD_BRACELET_11076 to Item(Items.CASTLEWAR_BRACE3_11079),
                            Items.CUBE_6899 to Item(Items.ORB_6902),
                            Items.CYLINDER_6898 to Item(Items.ORB_6902),
                            Items.ICOSAHEDRON_6900 to Item(Items.ORB_6902),
                            Items.PENTAMID_6901 to Item(Items.ORB_6902),
                            Items.DRAGONSTONE_6903 to Item(Items.ORB_6902),
                        ),
                    runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.AIR_RUNE.id, 3)),
                ),
        )
        SpellBookManager.SpellBook.MODERN.register(
            buttonId = 28,
            spell =
                EnchantSpell(
                    level = 49,
                    experience = 59.0,
                    jewellery =
                        mapOf(
                            Items.RUBY_RING_1641 to Item(Items.RING_OF_FORGING_2568),
                            Items.RUBY_NECKLACE_1660 to Item(Items.DIGSITE_PENDANT_5_11194),
                            Items.RUBY_AMULET_1698 to Item(Items.AMULET_OF_STRENGTH_1725),
                            Items.RUBY_BRACELET_11085 to Item(Items.INOCULATION_BRACE_11088),
                            Items.CUBE_6899 to Item(Items.ORB_6902),
                            Items.CYLINDER_6898 to Item(Items.ORB_6902),
                            Items.ICOSAHEDRON_6900 to Item(Items.ORB_6902),
                            Items.PENTAMID_6901 to Item(Items.ORB_6902),
                            Items.DRAGONSTONE_6903 to Item(Items.ORB_6902),
                        ),
                    runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 5)),
                ),
        )
        SpellBookManager.SpellBook.MODERN.register(
            buttonId = 36,
            spell =
                EnchantSpell(
                    level = 57,
                    experience = 67.0,
                    jewellery =
                        mapOf(
                            Items.DIAMOND_RING_1643 to Item(Items.RING_OF_LIFE_2570),
                            Items.DIAMOND_NECKLACE_1662 to Item(Items.PHOENIX_NECKLACE_11090),
                            Items.DIAMOND_AMULET_1700 to Item(Items.AMULET_OF_POWER_1731),
                            Items.DIAMOND_BRACELET_11092 to Item(Items.FORINTHRY_BRACE5_11095),
                            Items.CUBE_6899 to Item(Items.ORB_6902),
                            Items.CYLINDER_6898 to Item(Items.ORB_6902),
                            Items.ICOSAHEDRON_6900 to Item(Items.ORB_6902),
                            Items.PENTAMID_6901 to Item(Items.ORB_6902),
                            Items.DRAGONSTONE_6903 to Item(Items.ORB_6902),
                        ),
                    runes = arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 10)),
                ),
        )
        SpellBookManager.SpellBook.MODERN.register(
            buttonId = 51,
            spell =
                EnchantSpell(
                    level = 68,
                    experience = 78.0,
                    jewellery =
                        mapOf(
                            Items.DRAGONSTONE_RING_1645 to Item(Items.RING_OF_WEALTH_2572),
                            Items.DRAGON_NECKLACE_1664 to Item(Items.SKILLS_NECKLACE4_11105),
                            Items.DRAGONSTONE_AMMY_1702 to Item(Items.AMULET_OF_GLORY4_1712),
                            Items.DRAGON_BRACELET_11115 to Item(Items.COMBAT_BRACELET4_11118),
                            Items.CUBE_6899 to Item(Items.ORB_6902),
                            Items.CYLINDER_6898 to Item(Items.ORB_6902),
                            Items.ICOSAHEDRON_6900 to Item(Items.ORB_6902),
                            Items.PENTAMID_6901 to Item(Items.ORB_6902),
                            Items.DRAGONSTONE_6903 to Item(Items.ORB_6902),
                        ),
                    runes =
                        arrayOf(
                            Item(Runes.COSMIC_RUNE.id, 1),
                            Item(Runes.WATER_RUNE.id, 15),
                            Item(Runes.EARTH_RUNE.id, 15),
                        ),
                ),
        )
        SpellBookManager.SpellBook.MODERN.register(
            buttonId = 61,
            spell =
                EnchantSpell(
                    level = 87,
                    experience = 97.0,
                    jewellery =
                        mapOf(
                            Items.ONYX_RING_6575 to Item(Items.RING_OF_STONE_6583),
                            Items.ONYX_NECKLACE_6577 to Item(Items.BERSERKER_NECKLACE_11128),
                            Items.ONYX_AMULET_6581 to Item(Items.AMULET_OF_FURY_6585),
                            Items.ONYX_BRACELET_11130 to Item(Items.REGEN_BRACELET_11133),
                            Items.CUBE_6899 to Item(Items.ORB_6902),
                            Items.CYLINDER_6898 to Item(Items.ORB_6902),
                            Items.ICOSAHEDRON_6900 to Item(Items.ORB_6902),
                            Items.PENTAMID_6901 to Item(Items.ORB_6902),
                            Items.DRAGONSTONE_6903 to Item(Items.ORB_6902),
                        ),
                    runes =
                        arrayOf(
                            Item(Runes.COSMIC_RUNE.id, 1),
                            Item(Runes.FIRE_RUNE.id, 20),
                            Item(Runes.EARTH_RUNE.id, 20),
                        ),
                ),
        )
        return this
    }

    private data class SpellData(
        val animation: Int,
        val graphic: Int,
        val sound: Int,
    )
}
