package content.region.asgarnia.handlers.guilds.warriorsguild

import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction

class AnimatedArmour internal constructor(
    private val player: Player,
    location: Location?,
    private val set: AnimationRoom.ArmourSet,
) : NPC(set.npcId, location) {
    private var running = false

    override fun init() {
        super.init()
        animate(Animation.create(4166))
        sendChat("I'M ALIVE!")
        properties.combatPulse.attack(player)
        HintIconManager.registerHintIcon(player, this)
    }

    override fun clear() {
        super.clear()
        player.hintIconManager.clear()
    }

    override fun handleTickActions() {
        if (!running && !properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(player)
        }
    }

    override fun onImpact(
        entity: Entity,
        state: BattleState,
    ) {
        if (!running) {
            if (getSkills().lifepoints < (getSkills().maximumLifepoints / 10) && RandomFunction.randomize(10) < 2) {
                running = true
                properties.combatPulse.stop()
                Pathfinder.find(location, Location.create(2849, 3534, 0)).walk(this)
                return
            }
            super.onImpact(entity, state)
        }
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        if (entity !== player) {
            if (entity is Player) {
                entity.packetDispatch.sendMessage("This isn't your armour to attack.")
            }
            return false
        }
        return super.isAttackable(entity, style, message)
    }

    override fun finalizeDeath(killer: Entity) {
        clear()
        if (killer != null) {
            var takenPiece = false
            val canTake = RandomFunction.random(180) == 1
            var index = 0
            var takeIndex = 0
            if (canTake) {
                takeIndex = RandomFunction.random(set.pieces.size)
            }
            for (piece in set.pieces) {
                if (canTake && !takenPiece && index == takeIndex) {
                    takenPiece = true
                    sendMessage(player, "Your armour was destroyed in the fight.")
                    continue
                }
                GroundItemManager.create(Item(piece), location, player)
                index++
            }
            if (killer != null) {
                val amount = set.tokenAmount
                GroundItemManager.create(Item(8851, amount), location, player)
            }
            player.logoutListeners.remove("animation-room")
            player.removeAttribute("animated_set")
        }
    }
}
