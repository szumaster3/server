package content.minigame.barrows.handlers

import core.api.clearHintIcon
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

class BarrowBrotherNPC(
    val player: Player,
    id: Int,
    location: Location?,
) : NPC(id, location) {
    override fun init() {
        super.init()
        super.setRespawn(false)
        if (location.z == 3) {
            sendChat("You dare disturb my rest!")
        } else {
            sendChat("You dare steal from us!")
        }
        properties.combatPulse.attack(player)
        HintIconManager.registerHintIcon(player, this)
    }

    override fun handleTickActions() {
        if (DeathTask.isDead(player)) {
            return
        }
        if (!player.isActive || !player.location.withinDistance(location)) {
            clear()
            return
        }
        if (!properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(player)
        }
    }

    override fun sendImpact(state: BattleState) {
        var maxHit = 0
        when (id) {
            NPCs.AHRIM_THE_BLIGHTED_2025 -> maxHit = 25
            NPCs.DHAROK_THE_WRETCHED_2026 -> maxHit = 60
            NPCs.GUTHAN_THE_INFESTED_2027 -> maxHit = 24
            NPCs.KARIL_THE_TAINTED_2028 -> maxHit = 20
            NPCs.TORAG_THE_CORRUPTED_2029 -> maxHit = 23
            NPCs.VERAC_THE_DEFILED_2030 -> maxHit = 25
        }
        if (state.estimatedHit > maxHit) {
            state.estimatedHit = RandomFunction.random(maxHit - 10, maxHit)
        }
        if (state.secondaryHit > maxHit) {
            state.secondaryHit = RandomFunction.random(maxHit - 10, maxHit)
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer === player) {
            player.getSavedData().activityData.barrowBrothers[brotherIndex] = true
            BarrowsActivityPlugin.sendConfiguration(player)
        }
    }

    override fun clear() {
        super.clear()
        if (player.isActive) {
            player.hintIconManager.clear()
        }
        removeAttribute(player, "barrow:npc")
        removeAttribute(player, "brother:" + brotherIndex)
        clearHintIcon(player)
    }

    private val brotherIndex: Int
        private get() = id - 2025
}
