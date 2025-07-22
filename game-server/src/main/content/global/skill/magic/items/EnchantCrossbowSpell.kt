package content.global.skill.magic.items

import core.game.component.Component
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Items

@Initializable
class EnchantCrossbowSpell : MagicSpell(SpellBook.MODERN, 4, 0.0, null, null, null, null) {
    init {
        SpellBook.MODERN.register(3, this)
    }

    override fun cast(
        entity: Entity,
        target: Node,
    ): Boolean {
        val player = entity as Player
        player.interfaceManager.open(Component(Components.XBOWS_ENCHANT_BOLT_432))

        val boltData: MutableMap<Int, Int> =
            mutableMapOf(
                17 to Items.OPAL_BOLTS_879,
                21 to Items.JADE_BOLTS_9335,
                25 to Items.PEARL_BOLTS_880,
                28 to Items.TOPAZ_BOLTS_9336,
                31 to Items.SAPPHIRE_BOLTS_9337,
                34 to Items.EMERALD_BOLTS_9338,
                37 to Items.RUBY_BOLTS_9339,
                40 to Items.DIAMOND_BOLTS_9340,
                43 to Items.DRAGON_BOLTS_9341,
                46 to Items.ONYX_BOLTS_9342,
            )

        for ((key, value) in boltData) {
            player.packetDispatch.sendItemZoomOnInterface(
                value,
                10,
                270,
                Components.XBOWS_ENCHANT_BOLT_432,
                key,
            )
        }

        return true
    }

    override fun newInstance(arg: SpellType?): Plugin<SpellType?> = this
}
