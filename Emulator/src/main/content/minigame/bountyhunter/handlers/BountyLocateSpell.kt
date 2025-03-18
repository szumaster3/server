package content.minigame.bountyhunter.handlers

import core.api.event.isStunned
import core.api.hasTimerActive
import core.api.sendMessage
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.TeleportManager
import core.game.world.GameWorld
import core.game.world.map.RegionManager.getTeleportLocation
import core.plugin.Plugin

class BountyLocateSpell :
    MagicSpell(
        SpellBookManager.SpellBook.MODERN,
        32,
        45.0,
        null,
        null,
        null,
        arrayOf(Runes.AIR_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1)),
    ) {
    override fun cast(
        entity: Entity,
        target: Node,
    ): Boolean {
        var target: Node? = target
        val activity = entity.getExtension<BountyHunterActivity>(BountyHunterActivity::class.java)
        val player = entity as Player
        if (activity == null) {
            player.packetDispatch.sendMessage("You can only use this spell in the Bounty Hunter craters.")
            return false
        }
        val entry = activity.players[player]
        if (entry == null || entry.target == null) {
            player.packetDispatch.sendMessage("You don't have a target to teleport to.")
            return true
        }
        if (hasTimerActive(player, "frozen") || isStunned(player)) {
            player.packetDispatch.sendMessage(
                "You can't use this when " + if (isStunned(player)) "stunned." else "frozen.",
            )
            return true
        }
        val combat = player.inCombat()
        if (!super.meetsRequirements(entity, true, combat)) {
            return false
        }
        if (combat) {
            sendMessage(player, "You were fighting recently so you'll run instead of teleport.")
            target = entry.target
            val location = entry.target!!.location
            if (!location.withinDistance(player.location, 30)) {
                var offsetX = location.x - player.location.x
                var offsetY = location.y - player.location.y
                if (offsetX > 30) {
                    offsetX = 30
                } else if (offsetX < -30) {
                    offsetX = -30
                }
                if (offsetY > 30) {
                    offsetY = 30
                } else if (offsetY < -30) {
                    offsetY = -30
                }
                target = player.location.transform(offsetX, offsetY, 0)
            }
            player.pulseManager.run(
                object : MovementPulse(player, target) {
                    override fun pulse(): Boolean = true
                },
                PulseType.STANDARD,
            )
            return true
        }
        val destination = getTeleportLocation(entry.target!!.location, 5)
        if (entity.getTeleporter().send(destination, TeleportManager.TeleportType.NORMAL, -1)) {
            if (!super.meetsRequirements(entity, true, true)) {
                entity.getTeleporter().currentTeleport.stop()
                return false
            }
            entity.setAttribute("magic-delay", GameWorld.ticks + 5)
            return true
        }
        return false
    }

    override fun newInstance(arg: SpellType?): Plugin<SpellType?> {
        SpellBookManager.SpellBook.MODERN.register(60, this)
        return this
    }
}
