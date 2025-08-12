package core.net.packet.out

import core.game.node.entity.skill.Skills
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import shared.consts.Network
import kotlin.math.ceil

/**
 * Handles the update skill outgoing packet.
 *
 * @author Emperor
 */
class SkillLevel : OutgoingPacket<OutgoingContext.SkillContext> {

    override fun send(context: OutgoingContext.SkillContext) {
        val buffer = IoBuffer(Network.SEND_SKILL_LEVEL)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        val skills = context.player.getSkills()
        if (context.skillId == Skills.PRAYER) {
            buffer.putA(ceil(skills.prayerPoints).toInt())
        } else if (context.skillId == Skills.HITPOINTS) {
            buffer.putA(skills.lifepoints)
        } else {
            buffer.putA(skills.getLevel(context.skillId, true))
        }
        buffer.putIntA(skills.getExperience(context.skillId).toInt()).put(context.skillId)
        context.player.details.session.write(buffer)
    }
}