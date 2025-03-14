package content.region.kandarin.handlers.npc.necro

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import core.tools.SystemLogger
import org.rs.consts.NPCs

val necroId = intArrayOf(NPCs.INVRIGAR_THE_NECROMANCER_173, NPCs.NECROMANCER_194)

class NecromancerNPC : NPCBehavior(*necroId) {
    override fun afterDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        if (attacker is Player) {
            handleZombieSummoning(attacker)
        }
    }

    private fun handleZombieSummoning(player: Player) {
        val zombiesAlive = getAttribute(player, "necro:zombie_alive", 0)
        if (zombiesAlive >= 3) return

        val random = RandomFunction.random(1, 6)
        if (random == 1) {
            SummonedZombiesNPC.summonZombie(player)
            setAttribute(player, "necro:summoned_zombie", true)
            setAttribute(player, "necro:zombie_alive", zombiesAlive + 1)
        }
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        if (killer is Player && getAttribute(killer.asPlayer(), "necro:summoned_zombie", false)) {
            runTask(killer, 3) {
                val zombie = findLocalNPC(killer, NPCs.SUMMONED_ZOMBIE_77)
                zombie?.let {
                    poofClear(it)
                    removeAttributes(killer, "necro:summoned_zombie")
                } ?: SystemLogger.logMS("[TheNecromancer]: Error with cleaning summoned zombies.")
            }
        }
    }
}
