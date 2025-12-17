package content.region.kandarin.camelot.quest.arthur.npc

import content.data.GameAttributes
import content.region.kandarin.camelot.quest.arthur.dialogue.MorganLeFayeDialogue
import core.api.openDialogue
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class SirMordredNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC = SirMordredNPC(id, location)

    var lockMovement: Player? = null

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_MORDRED_247)

    override fun canAttack(victim: Entity?): Boolean {
        if (lockMovement != null) {
            return false
        }

        return super.canAttack(victim)
    }

    override fun isAttackable(
        entity: Entity?,
        style: CombatStyle?,
        message: Boolean,
    ): Boolean {
        if (lockMovement != null) {
            return false
        }

        return super.isAttackable(entity, style, message)
    }

    override fun tick() {
        if (this.skills.lifepoints == 0) {
            if (this.properties.combatPulse.getVictim() != null) {
                this.lockMovement = this.properties.combatPulse.getVictim()!!.asPlayer()
            } else if (this.properties.combatPulse.lastVictim != null) {
                this.lockMovement = this.properties.combatPulse.lastVictim!!.asPlayer()
            }

            this.locks.lock()
            clearAttributes()
            this.face(null)
            skills.restore()
            this.properties.combatPulse.stop()

            if (lockMovement != null) {
                if (lockMovement!!.getAttribute<NPC>(GameAttributes.TEMP_ATTR_MORGAN, null) == null) {
                    initMorgan(lockMovement!!)
                }

                if (!lockMovement!!.interfaceManager.hasChatbox()) {
                    openDialogue(lockMovement!!, MorganLeFayeDialogue(), NPCs.MORGAN_LE_FAYE_248)
                }
            }
        }

        if (lockMovement != null) {
            if (!lockMovement!!.isActive || !lockMovement!!.interfaceManager.hasChatbox()) {
                lockMovement = null
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
        player.setAttribute(GameAttributes.TEMP_ATTR_MORGAN, morgan)
        morgan.player = player
        morgan.graphics(Graphics.create(shared.consts.Graphics.RE_PUFF_86))
        morgan.init()
        morgan.moveStep()
    }
}

