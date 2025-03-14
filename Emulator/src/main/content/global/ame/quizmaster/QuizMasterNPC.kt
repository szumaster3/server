package content.global.ame.quizmaster

import content.global.ame.RandomEventNPC
import core.api.face
import core.api.findLocalNPC
import core.api.runTask
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.system.timer.impl.AntiMacro
import core.game.world.update.flag.context.Animation
import org.rs.consts.NPCs

class QuizMasterNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.QUIZ_MASTER_2477) {
    override fun init() {
        super.init()
        sendChat("It's your lucky day!")
        QuizMaster.init(player)
    }

    override fun tick() {
        super.tick()
        runTask(player, 1) {
            player.pulseManager.run(
                object : Pulse(2) {
                    override fun pulse(): Boolean {
                        face(player, findLocalNPC(player, NPCs.QUIZ_MASTER_2477)!!)
                        player.animate(Animation.create(QuizMaster.SIT_ANIMATION))
                        return false
                    }
                },
            )
        }
        AntiMacro.terminateEventNpc(player)
    }

    override fun talkTo(npc: NPC) {
    }
}
