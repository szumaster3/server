package content.global.skill.magic.spells.modern

import content.minigame.mage_training.plugin.MTAType
import content.minigame.mage_training.plugin.rooms.EnchantmentChamberPlugin
import core.api.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import core.plugin.Plugin

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
