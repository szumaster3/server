package content.region.kandarin.quest.ikov.handlers

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FireWarriorOfLesarkusNPC(
    id: Int = 0,
    val player: Player?,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var clearTime = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return FireWarriorOfLesarkusNPC(id, null, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FIRE_WARRIOR_OF_LESARKUS_277)
    }

    override fun handleTickActions() {
        super.handleTickActions()

        if (clearTime++ > 240) {
            removeAttribute(player!!, GameAttributes.QUEST_IKOV_WARRIOR_INST)
            poofClear(this)
        }
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        val player = entity.asPlayer()
        return attackable
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker.asPlayer()
        val opponent = this
        println(state.ammunition)
        if (state.ammunition != null && state.ammunition.itemId == 78) {
        } else {
            player.properties.combatPulse.stop()
            if (state.estimatedHit > -1) {
                state.estimatedHit = 0
            }
            if (state.secondaryHit > -1) {
                state.secondaryHit = 0
            }
            runTask(player, 0) {
                sendNPCDialogue(
                    player,
                    opponent.id,
                    "Your puny weapons do nothing against me human! Come back when you can give me a real fight!",
                    FaceAnim.ANGRY,
                )
            }
        }
    }

    override fun finalizeDeath(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            removeAttribute(player, GameAttributes.QUEST_IKOV_WARRIOR_INST)
            if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) == 3) {
                setQuestStage(player, Quests.TEMPLE_OF_IKOV, 4)
            }
            super.finalizeDeath(player)
        }
    }
}
