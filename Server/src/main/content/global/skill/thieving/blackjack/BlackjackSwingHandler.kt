package content.global.skill.thieving.blackjack

import core.api.stun
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.combat.equipment.Weapon
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Items

@Initializable
class BlackjackSwingHandler : MeleeSwingHandler(), Plugin<Any> {

    override fun newInstance(arg: Any?): Plugin<Any> {
        BlackjackType.values()
            .flatMap { it.itemIds.asList() }
            .forEach { CombatStyle.MELEE.swingHandler.register(it, this) }
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any = Unit

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        val player = entity as? core.game.node.entity.player.Player ?: return -1
        val target = victim ?: return -1
        val combatState = state ?: return -1

        val weaponItem = player.equipment[EquipmentContainer.SLOT_WEAPON] ?: return -1
        combatState.weapon = Weapon(weaponItem)

        combatState.style = CombatStyle.MELEE

        var hit = 0
        if (isAccurateImpact(player, target, CombatStyle.MELEE)) {
            val max = calculateHit(player, target, 1.0)
            combatState.maximumHit = max
            hit = RandomFunction.random(max + 1)
        }
        combatState.estimatedHit = hit

        if (hit > 0) {
            val stunTicks = getStunTicks(weaponItem.id)
            if (stunTicks > 0 && !target.isPlayer) {
                val attackerRoll = RandomFunction.random(hit + 1)
                val victimRoll = RandomFunction.random(target.skills.lifepoints + 1)
                val nearDeath = target.skills.lifepoints <= hit
                if (attackerRoll > victimRoll || nearDeath) {
                    stun(target, stunTicks, false) 
                }
            }
        }

        return 1
    }

    private fun getStunTicks(itemId: Int): Int = when (itemId) {
        Items.OAK_BLACKJACK_4599,
        Items.OAK_BLACKJACKO_6408,
        Items.OAK_BLACKJACKD_6410 -> 1

        Items.WILLOW_BLACKJACK_4600,
        Items.WILLOW_BLACKJACKO_6412,
        Items.WILLOW_BLACKJACKD_6414 -> 2

        Items.MAPLE_BLACKJACK_6416,
        Items.MAPLE_BLACKJACKO_6418,
        Items.MAPLE_BLACKJACKD_6420 -> 3
        else -> 0
    }
}
