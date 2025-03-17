package content.custom.handlers

import core.api.sendMessage
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class StormOfArmadylSpell :
    CombatSpell(
        SpellType.STORM_OF_ARMADYL,
        SpellBookManager.SpellBook.MODERN,
        77,
        0.0,
        -1,
        -1,
        null,
        Graphics(1333, 96),
        Projectile.create(null as Entity?, null, 1333, 20, 0, 52, 75, 15, 11),
        Graphics(1334, 96),
        Runes.ARMADYL_RUNE.getItem(1)
    ) {

    override fun cast(entity: Entity, target: Node): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()
            if (player.equipment.getNew(EquipmentContainer.SLOT_WEAPON).id != Items.ARMADYL_BATTLESTAFF_14696 && player.interfaceManager.opened.id != 90) {
                sendMessage(player, "You need to wield the Armadyl Battlestaff to cast this spell.")
                return false
            }
        }
        return true
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: SpellType?): Plugin<SpellType?>? {
        SpellBookManager.SpellBook.forInterface(90)?.register(5, this)
        return this
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int {
        return type.getImpactAmount(entity, victim, 20)
    }

    override fun visualize(entity: Entity, target: Node) {
        super.visualize(entity, target)
    }
}
