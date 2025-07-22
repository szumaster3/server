package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.applyPoison
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SpiritScorpionNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_SCORPION_6837) :
    Familiar(owner, id, 1700, Items.SPIRIT_SCORPION_POUCH_12055, 6, WeaponInterface.STYLE_CONTROLLED) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritScorpionNPC(owner, id)
    }

    override fun adjustPlayerBattle(state: BattleState) {
        if (state.style == CombatStyle.RANGE) {
            val weapon = state.weapon
            if (isCharged && Item(weapon.id + 6).name.startsWith(weapon.name)) {
                val victim = state.victim
                isCharged = false
                applyPoison(victim, owner, 1)
            }
        }
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (isCharged) {
            return false
        }
        charge()
        owner.graphics(Graphics(1355, 180), 2)
        visualize(Animation(6261), Graphics(1354, 95))
        Projectile.create(this, owner, 1355, 95, 50, 50, 10).send()
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_SCORPION_6837, NPCs.SPIRIT_SCORPION_6838)
    }
}
