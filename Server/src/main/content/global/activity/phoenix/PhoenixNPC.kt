package content.global.activity.phoenix

import com.google.gson.JsonObject
import content.data.GameAttributes
import core.ServerStore
import core.ServerStore.Companion.getBoolean
import core.api.getStatLevel
import core.api.rewardXP
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
import core.game.node.entity.skill.Skills
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
            val e = pulse.getVictim() ?: return
            if (!targetFocus)
            {
                val target = impactHandler.getMostDamageEntity(e)
                if (target != null && target != e && target is Player)
                {
                    pulse.setVictim(target)
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

        if (entity is Player && getStatLevel(entity, Skills.SLAYER) < 51)
        {
            if (message)
                sendMessage(entity, "Phoenix is a Slayer monster that requires a Slayer level of 51 to kill.")
            return false
        }

        return super.isAttackable(entity, style, message)
    }

    override fun finalizeDeath(killer: Entity?)
    {
        super.finalizeDeath(killer)

        if (killer !is Player) return
        val player = killer.asPlayer()
        val username = player.username.lowercase()
        val store = getStoreFile()

        if (!store.getBoolean(username))
        {
            store.addProperty(username, true)
            val npc = NPC.create(NPCs.PHOENIX_8548, Location.create(3536, 5197, 0))
            setAttribute(player, GameAttributes.PHOENIX_LAIR_ACTIVITY_REWARD, true)
            npc.init()
        }

        val firstKill = player.getAttribute(GameAttributes.PHOENIX_LAIR_FIRST_KILL, false)
        if (!firstKill)
        {
            rewardXP(player, Skills.SLAYER, 5000.0)
            setAttribute(player, GameAttributes.PHOENIX_LAIR_FIRST_KILL, true)
        }
        else
        {
            rewardXP(player, Skills.SLAYER, 500.0)
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC
    {
        return PhoenixNPC(id, location)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PHOENIX_8549)

    private fun getStoreFile(): JsonObject = ServerStore.getArchive("daily-phoenix-lair-activity")
}
