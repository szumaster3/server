package content.global.random.event.quizmaster

import content.global.random.RandomEventNPC
import core.api.findLocalNPC
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.system.timer.impl.AntiMacro
import core.game.world.update.flag.context.Animation
import shared.consts.NPCs

/**
 * Represents the Quizmaster random event NPC.
 * @author ovenbreado
 */
class QuizMasterNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.QUIZ_MASTER_2477) {
    override fun init() {
        super.init()
        sendChat("It's your lucky day!")
        QuizMaster.init(player).also {
            AntiMacro.terminateEventNpc(player)
        }
    }

    override fun tick() {
        super.tick()
        player.pulseManager.run(
            object : Pulse(2) {
                override fun pulse(): Boolean {
                    player.face(findLocalNPC(player, NPCs.QUIZ_MASTER_2477))
                    player.animate(Animation.create(QuizMaster.SIT_ANIMATION))
                    return false
                }
            },
        )

    }

    override fun talkTo(npc: NPC) {
    }
}
