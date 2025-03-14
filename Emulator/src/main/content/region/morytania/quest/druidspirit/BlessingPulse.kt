package content.region.morytania.quest.druidspirit

import core.api.*
import core.api.quest.setQuestStage
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.*

class BlessingPulse(
    val drezel: NPC,
    val player: Player,
) : Pulse() {
    var ticks = 0

    override fun pulse(): Boolean {
        when (ticks) {
            0 -> {
                animate(drezel, Animations.WALK_1162)
                spawnProjectile(drezel, player, 268)
                playAudio(player, Sounds.PRAYER_RECHARGE_2674)
            }

            2 ->
                visualize(
                    player,
                    Animation(Animations.HUMAN_PRAY_645),
                    Graphics(org.rs.consts.Graphics.BRIGHT_GREEN_PUFF_OF_STUFF_267, 100),
                )

            4 -> {
                unlock(player)
                setQuestStage(player, Quests.NATURE_SPIRIT, 40)
                return true
            }
        }
        ticks++
        return false
    }

    override fun stop() {
        super.stop()
        openDialogue(player, DrezelDialogueFile(), drezel)
    }
}
