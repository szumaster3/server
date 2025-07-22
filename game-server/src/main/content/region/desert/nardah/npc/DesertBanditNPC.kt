package content.region.desert.nardah.npc

import content.data.God
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class DesertBanditNPC(
    id: Int = NPCs.BANDIT_1926,
    location: Location? = null,
) : AbstractNPC(id, location) {

    companion object {
        private val SARADOMIN_ITEMS = God.SARADOMIN.validItems.toSet()
        private val ZAMORAK_ITEMS = God.ZAMORAK.validItems.toSet()
        private val NON_AFFILIATED_ITEMS = setOf(
            Items.INITIATE_CUISSE_5576,
            Items.INITIATE_HAUBERK_5575,
            Items.INITIATE_SALLET_5574,
            Items.PROSELYTE_CUISSE_9676,
            Items.PROSELYTE_HAUBERK_9674,
            Items.PROSELYTE_SALLET_9672,
            Items.PROSELYTE_TASSET_9678,
            Items.WHITE_BOOTS_6619,
            Items.WHITE_CHAINBODY_6615,
            Items.WHITE_FULL_HELM_6623,
            Items.WHITE_GLOVES_6629,
            Items.WHITE_KITESHIELD_6633,
            Items.WHITE_PLATEBODY_6617,
            Items.WHITE_PLATESKIRT_6627,
            Items.WHITE_SQ_SHIELD_6631,
        )

        private const val SARADOMIN_FORCE_CHAT = "Time to die, Saradominist filth!"
        private const val ZAMORAK_FORCE_CHAT = "Prepare to suffer, Zamorakian scum!"
        private const val ALLY_ATTACKED_CHAT = "You chose the wrong place to start trouble!"
    }

    private val supportRange = 10

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        DesertBanditNPC(id, location)

    private fun checkPlayerEquipment(player: Player): Int {
        /*
         *  0 = none,
         *  1 = Saradomin,
         *  2 = Zamorak,
         * -1 = non-affiliated found.
         */
        val eq = player.equipment
        for (slot in 0 until eq.capacity()) {
            val item = eq[slot] ?: continue
            when {
                NON_AFFILIATED_ITEMS.contains(item.id) -> return -1
                SARADOMIN_ITEMS.contains(item.id) -> return 1
                ZAMORAK_ITEMS.contains(item.id) -> return 2
            }
        }
        return 0
    }

    override fun tick() {
        if (!inCombat()) {
            val players = RegionManager.getLocalPlayers(this, 5)
            loop@ for (player in players) {
                if (player.inCombat()) continue
                when (checkPlayerEquipment(player)) {
                    1 -> {
                        sendChat(SARADOMIN_FORCE_CHAT)
                        attack(player)
                        break@loop
                    }
                    2 -> {
                        sendChat(ZAMORAK_FORCE_CHAT)
                        attack(player)
                        break@loop
                    }
                }
            }
        }
        super.tick()
    }

    override fun onImpact(entity: Entity?, state: BattleState?) {
        if (entity is Player) {
            RegionManager.getLocalNpcs(this, supportRange).forEach { npc ->
                if (npc.id == NPCs.BANDIT_1926 && !npc.properties.combatPulse.isAttacking && npc != this) {
                    npc.sendChat(ALLY_ATTACKED_CHAT)
                    npc.attack(entity)
                }
            }
        }
        super.onImpact(entity, state)
    }

    override fun canSelectTarget(target: Entity?): Boolean {
        if (target is Player) {
            val check = checkPlayerEquipment(target)
            return check == 1 || check == 2
        }
        return super.canSelectTarget(target)
    }

    override fun isAggressive(): Boolean {
        val baseAggressive = super.isAggressive()
        val target = properties.combatPulse.entity
        if (target is Player) {
            return when (checkPlayerEquipment(target)) {
                1 -> {
                    sendChat(SARADOMIN_FORCE_CHAT)
                    true
                }
                2 -> {
                    sendChat(ZAMORAK_FORCE_CHAT)
                    true
                }
                -1 -> false
                else -> baseAggressive
            }
        }
        return baseAggressive
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BANDIT_1926)
}
