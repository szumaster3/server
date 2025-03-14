package content.region.asgarnia.quest.troll.handlers.npc

import content.region.asgarnia.quest.troll.dialogue.DadDialogueFile
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendMessage
import core.api.submitWorldPulse
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DadNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return DadNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAD_1125)
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
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
            submitWorldPulse(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            1 -> {
                                openDialogue(player.asPlayer(), DadDialogueFile(3), opponent.asNpc())
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        if (getQuestStage(killer!!.asPlayer(), Quests.TROLL_STRONGHOLD) == 4) {
            setQuestStage(killer.asPlayer(), Quests.TROLL_STRONGHOLD, 5)
        }
    }
}
