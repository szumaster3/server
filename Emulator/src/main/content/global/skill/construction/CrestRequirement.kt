package content.global.skill.construction

import core.game.node.entity.player.Player

interface CrestRequirement {
    fun eligible(player: Player) = true
}
