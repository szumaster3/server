package content.global.skill.thieving.blackjack.timer

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.system.timer.RSTimer
import core.game.world.update.flag.context.Animation

class BlackjackUnconsciousTimer :
    RSTimer(
        runInterval = 1,
        identifier = BLACKJACK_UNCONSCIOUS,
        isSoft = false
    ) {

    var remainingTicks: Int = 0
    var pickpocketsLeft: Int = 2

    override fun getInitialRunDelay(): Int = 1

    override fun onRegister(entity: Entity) {
        if (entity !is NPC) return

        entity.animator.animate(Animation(838))
        entity.sendChat("Zzzzzzz")
        entity.walkingQueue.reset()
    }

    override fun run(entity: Entity): Boolean {
        if (entity !is NPC) return false
        remainingTicks--
        return remainingTicks > 0
    }

    override fun onRemoval(entity: Entity) {
        if (entity !is NPC) return
        entity.animator.reset()
        entity.sendChat("Arghh my head.")
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val timer = BlackjackUnconsciousTimer()
        timer.remainingTicks = args.getOrNull(0) as? Int ?: 8
        timer.pickpocketsLeft = args.getOrNull(1) as? Int ?: 2
        return timer
    }

    companion object {
        val BLACKJACK_UNCONSCIOUS = "blackjack_unconscious"
    }
}
