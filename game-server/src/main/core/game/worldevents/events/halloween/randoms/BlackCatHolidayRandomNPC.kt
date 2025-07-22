package core.game.worldevents.events.halloween.randoms

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.NPC
import core.game.worldevents.events.HolidayRandomEventNPC
import core.game.worldevents.events.HolidayRandoms
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class BlackCatHolidayRandomNPC : HolidayRandomEventNPC(NPCs.BLACK_CAT_4607) {
    override fun init() {
        super.init()
        queueScript(this, 8, QueueStrength.SOFT) {
            playGlobalAudio(this.location, Sounds.CAT_ATTACK_333)
            sendChat("HISS")
            if (withinDistance(player, this.location, 3)) {
                this.face(player)
                sendMessage(player, "The cat scratches you and runs away.")
                val hit = if (player.skills.lifepoints < 5) 0 else 1
                impact(player, hit, HitsplatType.NORMAL)
            }
            HolidayRandoms.terminateEventNpc(player)
            return@queueScript stopExecuting(this)
        }
    }

    override fun talkTo(npc: NPC) {
    }
}
