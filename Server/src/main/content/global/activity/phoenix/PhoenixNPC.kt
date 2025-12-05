package content.global.activity.phoenix

import com.google.gson.JsonObject
import content.data.GameAttributes
import core.ServerStore
import core.api.sendMessage
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.agg.AggressiveBehavior
import core.game.node.entity.npc.agg.AggressiveHandler
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class PhoenixNPC (id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    companion object
    {
        private val PHOENIX_COMBAT: CombatSwingHandler = PhoenixSwingHandler()
        private val CHAMBER = ZoneBorders(3521, 5184, 3551, 5214)
    }

    private var handler: CombatSwingHandler = PHOENIX_COMBAT
    private var targetFocus: Boolean = false

    override fun init()
    {
        isAggressive = false
        isRespawning = false
        super.init()

        val behavior = object : AggressiveBehavior()
        {
            override fun canSelectTarget(entity: Entity, target: Entity): Boolean
            {
                if (!target.isActive || target.inCombat()) return false
                return CHAMBER.insideBorder(target.location)
            }
        }

        aggressiveHandler = AggressiveHandler(this, behavior)
        aggressiveHandler?.apply {
            chanceRatio = 6
            radius = 28
            isAllowTolerance = false
        }

        properties.isNPCWalkable = true
        properties.combatTimeOut = -1
    }

    override fun tick()
    {
        val pulse = properties.combatPulse
        if (pulse.isAttacking) {
            val e = pulse.victim ?: return
            if (!targetFocus)
            {
                val target = impactHandler.getMostDamageEntity(e)
                if (target != null && target != e && target is Player)
                {
                    pulse.victim = target
                }
            }
            if (!CHAMBER.insideBorder(e.location))
            {
                pulse.stop()
            }
        }
        super.tick()
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler
    {
        return handler
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean
    {
        if (style == CombatStyle.MELEE && entity is Player)
        {
            if (message)
                sendMessage(entity, "The Phoenix is flying too high for Melee attacks.")
            return false
        }
        return super.isAttackable(entity, style, message)
    }


    override fun finalizeDeath(killer: Entity?)
    {
        if (killer is Player)
        {
            val player = killer.asPlayer()
            val store = getStoreFile()
            val username = player?.username?.lowercase() ?: return
            store.addProperty(username, true)
            val npc = NPC.create(NPCs.PHOENIX_8548,Location.create(3536, 5197, 0))
            setAttribute(player, GameAttributes.PHOENIX_LAIR_ACTIVITY_REWARD, true)
            npc.init()
        }
        clear()
    }

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC
    {
        return PhoenixNPC(id, location)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PHOENIX_8549)

    private fun getStoreFile(): JsonObject = ServerStore.getArchive("daily-phoenix-lair-activity")
}
