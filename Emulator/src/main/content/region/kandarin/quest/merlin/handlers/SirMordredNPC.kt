package content.region.kandarin.quest.merlin.handlers

import content.region.kandarin.quest.merlin.dialogue.MorganLeFayeDialogue
import core.api.openDialogue
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SirMordredNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC {
        return SirMordredNPC(id, location)
    }

    var lockMovementPlr: Player? = null

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_MORDRED_247)
    }

    override fun canAttack(victim: Entity?): Boolean {
        if (lockMovementPlr != null) {
            return false
        }

        return super.canAttack(victim)
    }

    override fun isAttackable(
        entity: Entity?,
        style: CombatStyle?,
        message: Boolean,
    ): Boolean {
        if (lockMovementPlr != null) {
            return false
        }

        return super.isAttackable(entity, style, message)
    }

    override fun tick() {
        if (this.skills.lifepoints == 0) {
            if (this.properties.combatPulse.getVictim() != null) {
                this.lockMovementPlr =
                    this.properties.combatPulse
                        .getVictim()!!
                        .asPlayer()
            } else if (this.properties.combatPulse.lastVictim != null) {
                this.lockMovementPlr =
                    this.properties.combatPulse.lastVictim!!
                        .asPlayer()
            }

            this.locks.lock()
            clearAttributes()
            this.face(null)
            skills.restore()
            this.properties.combatPulse.stop()

            if (lockMovementPlr != null) {
                if (lockMovementPlr!!.getAttribute<NPC>(MerlinUtils.TEMP_ATTR_MORGAN, null) == null) {
                    initMorgan(lockMovementPlr!!)
                }

                if (!lockMovementPlr!!.interfaceManager.hasChatbox()) {
                    openDialogue(lockMovementPlr!!, MorganLeFayeDialogue(), NPCs.MORGAN_LE_FAYE_248)
                }
            }
        }

        if (lockMovementPlr != null) {
            if (!lockMovementPlr!!.isActive || !lockMovementPlr!!.interfaceManager.hasChatbox()) {
                lockMovementPlr = null
                unlock()
            }
        }

        super.tick()
    }

    override fun finalizeDeath(killer: Entity?) {
        super.getProperties().combatPulse.stop()
        super.finalizeDeath(killer)
    }

    private fun initMorgan(player: Player) {
        val morgan = MorganLeFayeNPC(NPCs.MORGAN_LE_FAYE_248, Location.create(2770, 3403, 2))
        player.setAttribute(MerlinUtils.TEMP_ATTR_MORGAN, morgan)
        morgan.player = player
        morgan.graphics(Graphics.create(86))
        morgan.init()
        morgan.moveStep()
    }
}
