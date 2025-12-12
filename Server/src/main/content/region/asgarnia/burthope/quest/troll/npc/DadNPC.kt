package content.region.asgarnia.burthope.quest.troll.npc

import content.region.asgarnia.burthope.quest.troll.dialogue.DadDialogueFile
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class DadNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = DadNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.DAD_1125)

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        val player = entity.asPlayer()

        when (getQuestStage(player, Quests.TROLL_STRONGHOLD)) {
            3 -> openDialogue(player, DadDialogueFile(2), this.asNpc()).also { return false }
            4 -> {
                return attackable
            }

            in 5..100 -> sendMessage(player, "You don't need to fight him again.").also { return false }
        }
        return attackable
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker
        val opponent = state.victim

        if (opponent.skills.lifepoints < 30) {
            player.properties.combatPulse.stop()
            opponent.properties.combatPulse.stop()
            if (getQuestStage(player!!.asPlayer(), Quests.TROLL_STRONGHOLD) == 4) {
                setQuestStage(player.asPlayer(), Quests.TROLL_STRONGHOLD, 5)
            }
            queueScript(player, 3, QueueStrength.SOFT) {
                openDialogue(player.asPlayer(), DadDialogueFile(3), opponent.asNpc())
                return@queueScript stopExecuting(player)
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        if (getQuestStage(killer!!.asPlayer(), Quests.TROLL_STRONGHOLD) == 4) {
            setQuestStage(killer.asPlayer(), Quests.TROLL_STRONGHOLD, 5)
        }
    }
}
