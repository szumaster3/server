package content.global.skill.magic

import core.api.playAudio
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import org.rs.consts.Sounds

class SpellChargeTimer : PersistTimer(700, "magic:spellcharge") {
    override fun run(entity: Entity): Boolean {
        if (entity !is Player) return false
        sendMessage(entity, "Your magical charge fades away.")
        playAudio(entity, Sounds.CHARGE_GONE_1650)
        return false
    }
}
