package content.region.desert.handlers.npc

import content.data.God
import core.api.hasGodItem
import core.game.container.Container
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
class DesertBandit(
    id: Int = NPCs.BANDIT_1926,
    location: Location? = null,
) : AbstractNPC(id, location) {
    companion object {
        private val saradominSet = mutableSetOf<Int>()
        private val zamorakSet = mutableSetOf<Int>()
        private val godSet = mutableSetOf<Int>()
        private val nonAffiliatedSet = mutableSetOf<Int>()

        private const val SARADOMIN_FORCE_CHAT = "Time to die, Saradominist filth!"
        private const val ZAMORAK_FORCE_CHAT = "Prepare to suffer, Zamorakian scum!"
        private const val ALLY_ATTACKED_CHAT = "You chose the wrong place to start trouble!"

        init {
            val zamorakProvideProtectionItems = God.ZAMORAK.validItems
            val saradominProvideProtectionItems = God.SARADOMIN.validItems
            val nonAffiliatedItems =
                intArrayOf(
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

            zamorakSet.addAll(zamorakProvideProtectionItems.toList())
            saradominSet.addAll(saradominProvideProtectionItems.toList())
            godSet.addAll(zamorakProvideProtectionItems.toList() + saradominProvideProtectionItems.toList())
            nonAffiliatedSet.addAll(nonAffiliatedItems.toList())
        }
    }

    private val supportRange = 10

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return DesertBandit(id, location)
    }

    override fun tick() {
        if (!inCombat()) {
            val players = RegionManager.getLocalPlayers(this, 5)
            for (player in players) {
                if (player.inCombat()) continue
                if (isWieldingValuableItem(player)) {
                    when {
                        hasGodItem(player, God.SARADOMIN) -> {
                            sendChat(SARADOMIN_FORCE_CHAT)
                            attack(player)
                            break
                        }

                        hasGodItem(player, God.ZAMORAK) -> {
                            sendChat(ZAMORAK_FORCE_CHAT)
                            attack(player)
                            break
                        }
                    }
                }
            }
        }
        super.tick()
    }

    private fun isWieldingValuableItem(player: Player): Boolean {
        val container: Container = player.equipment
        for (slot in 0 until container.capacity()) {
            val item = container[slot] ?: continue
            if (godSet.contains(item.id)) {
                return true
            }
            if (nonAffiliatedSet.contains(item.id)) {
                return false
            }
        }
        return false
    }

    override fun onImpact(
        entity: Entity?,
        state: BattleState?,
    ) {
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
        return if (target is Player) isWieldingValuableItem(target) else super.canSelectTarget(target)
    }

    override fun isAggressive(): Boolean {
        val check = super.isAggressive()
        val target = properties.combatPulse.entity
        if (target is Player) {
            val container: Container = target.equipment
            for (slot in 0 until container.capacity()) {
                val item = container[slot] ?: continue
                when {
                    saradominSet.contains(item.id) -> {
                        sendChat(SARADOMIN_FORCE_CHAT)
                        return true
                    }

                    zamorakSet.contains(item.id) -> {
                        sendChat(ZAMORAK_FORCE_CHAT)
                        return true
                    }

                    nonAffiliatedSet.contains(item.id) -> {
                        return false
                    }
                }
            }
        }
        return check
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BANDIT_1926)
    }
}
