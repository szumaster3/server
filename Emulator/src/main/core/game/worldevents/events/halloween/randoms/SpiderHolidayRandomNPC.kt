package core.game.worldevents.events.halloween.randoms

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.worldevents.events.HolidayRandomEventNPC
import core.game.worldevents.events.HolidayRandoms
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class SpiderHolidayRandomNPC : HolidayRandomEventNPC(NPCs.SPIDER_61) {
    override fun init() {
        super.init()
        this.behavior = SpiderHolidayRandomBehavior()
        playGlobalAudio(this.location, Sounds.SPIDER_4375)
        var stomped = false
        queueScript(this, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    sendChat(player, "Eww a spider!")
                    return@queueScript delayScript(this, 2)
                }

                1 -> {
                    if (withinDistance(player, this.location, 3)) {
                        animate(player, Animations.HUMAN_STOMP_4278)
                        playGlobalAudio(this.location, Sounds.UNARMED_KICK_2565)
                        sendMessage(player, "You stomp the spider.")
                        stomped = true
                    }
                    return@queueScript delayScript(this, 1)
                }

                2 -> {
                    if (stomped) {
                        impact(this, 1, ImpactHandler.HitsplatType.NORMAL)
                    } else {
                        sendMessage(player, "The spider runs away.")
                        playGlobalAudio(this.location, Sounds.SPIDER_4375)
                        HolidayRandoms.terminateEventNpc(player)
                    }
                    return@queueScript stopExecuting(this)
                }

                else -> return@queueScript stopExecuting(this)
            }
        }
    }

    override fun talkTo(npc: NPC) {
    }
}
