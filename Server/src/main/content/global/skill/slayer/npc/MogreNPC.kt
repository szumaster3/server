package content.global.skill.slayer.npc

import core.api.clearHintIcon
import core.api.finishDiaryTask
import core.api.getStatLevel
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.NPCs

class MogreNPC(id: Int, val spawnLocation: Location) : AbstractNPC(id, spawnLocation, true) {

    override fun tick() {
        super.tick()
        val pl: Player? = getAttribute("player", null)
        if (pl == null) {
            clear()
            return
        }

        if (pl.location.getDistance(spawnLocation) > 15) {
            clear()
        }
    }

    override fun clear() {
        super.clear()
        val pl: Player? = getAttribute("player", null)
        pl?.removeAttribute("hasMogre")
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        val player = getAttribute<Player>("player", null)
        if (entity is Player) {
            if (getStatLevel(entity, Skills.SLAYER) < 32) {
                sendMessage(entity, "Mogre is Slayer monster that requires a Slayer level of 32 to kill.")
                return false
            }
        }
        return player == entity && super.isAttackable(entity, style, message)
    }

    override fun finalizeDeath(killer: Entity) {
        if (killer is Player) {
            finishDiaryTask(killer, DiaryType.FALADOR, 2, 7)
            clearHintIcon(killer)
        }
        super.finalizeDeath(killer)
    }

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return MogreNPC(id, location)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MOGRE_114)
}