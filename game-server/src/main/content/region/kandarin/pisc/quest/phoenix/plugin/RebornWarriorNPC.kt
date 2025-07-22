package content.region.kandarin.pisc.quest.phoenix.plugin

import content.region.kandarin.pisc.quest.phoenix.InPyreNeed
import core.api.getPathableRandomLocalCoordinate
import core.api.sendChat
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction

/**
 * Handles Reborn warrior NPC.
 */
class RebornWarriorNPC : AbstractNPC {
    var ticks = 0
    constructor() : super(0, null)
    constructor(id: Int, location: Location?) : super(id, location)

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
        "Please, help the mistress!",
    )

    override fun handleTickActions() {
        super.handleTickActions()
        if (!InPyreNeed.NON_HOSTILE_IDS.contains(this.id)) return

        ticks++
        if (ticks < 20) return

        ticks = 0
        if (RandomFunction.random(10) < 3) {
            sendChat(this.asNpc(), forceChat.random())
        }
    }

    override fun getIds(): IntArray {
        return InPyreNeed.REBORN_WARRIOR_ID
    }

    companion object {
        private val COMBAT_HANDLER = object : MeleeSwingHandler() {
            override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
                if (entity is NPC && RandomFunction.random(300) < 3) {
                    val npc = entity as AbstractNPC

                    npc.impactHandler.disabledTicks = 3
                    npc.animate(Animation(11133))

                    val loc = getPathableRandomLocalCoordinate(npc, 5, npc.location, 1)
                    npc.teleport(loc, 3)
                    npc.animate(Animation(11136))
                }
                return super.swing(entity, victim, state)
            }
        }
    }
}