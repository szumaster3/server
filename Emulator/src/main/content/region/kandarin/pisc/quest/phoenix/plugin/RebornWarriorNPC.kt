package content.region.kandarin.pisc.quest.phoenix.plugin

import content.region.kandarin.pisc.quest.phoenix.InPyreNeed
import core.api.getPathableRandomLocalCoordinate
import core.api.sendChat
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction

/**
 * Handles Reborn warrior NPC.
 */
class RebornWarriorNPC(id: Int = 0, location: Location? = null, private val targetPlayer: Player? = null) : AbstractNPC(id, location) {

    private var ticks = 0

    init {
        this.isRespawn = false
        this.isWalks = true
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return RebornWarriorNPC(id, location)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return COMBAT_HANDLER
    }

    private val forceChat = arrayOf(
        "The mistress is fading!",
        "You must help the mistress!",
        "Save the mistress!",
        "The mistress is in pain!",
        "Please, help the mistress!"
    )

    override fun handleTickActions() {
        super.handleTickActions()

        if (InPyreNeed.NON_HOSTILE_IDS.contains(this.id)) {
            ticks++
            if (ticks >= 20) {
                ticks = 0
                if (RandomFunction.random(10) < 3) {
                    sendChat(this.asNpc(), forceChat.random())
                }
            }
        }

        if (targetPlayer != null && !properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(targetPlayer)
        }
    }

    override fun isAttackable(entity: Entity, style: core.game.node.entity.combat.CombatStyle, message: Boolean): Boolean {
        return targetPlayer == null || entity === targetPlayer
    }

    override fun canSelectTarget(target: Entity): Boolean {
        return targetPlayer == null || target === targetPlayer
    }

    override fun getIds(): IntArray = InPyreNeed.REBORN_WARRIOR_ID

    companion object {
        private val COMBAT_HANDLER = object : MeleeSwingHandler() {
            override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
                if (entity is AbstractNPC && RandomFunction.random(300) < 3) {
                    entity.impactHandler.disabledTicks = 3
                    entity.animate(Animation(11133))
                    val loc = getPathableRandomLocalCoordinate(entity, 5, entity.location, 1)
                    entity.teleport(loc, 3)
                    entity.animate(Animation(11136))
                }
                return super.swing(entity, victim, state)
            }
        }
    }
}
