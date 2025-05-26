package content.minigame.pyramidplunder.handlers

import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getSpawnLocation
import core.game.world.map.path.Pathfinder
import core.plugin.Plugin

abstract class PyramidPlunderNPC(
    id: Int,
    location: Location?,
    val player: Player,
) : AbstractNPC(id, location) {
    private val quotes: Array<String>? = null
    private var count = 0
    private var nextSpeech = 0
    var isTimeUp = false
    private val endTime: Int

    init {
        endTime = (ticks + 1000 / 0.6).toInt()
    }

    override fun init() {
        location = getSpawnLocation(player, this)
        if (location == null) {
            clear()
            return
        }
        super.init()
        startFollowing()
    }

    override fun handleTickActions() {
        if (ticks > endTime) {
            clear()
        }
        if (!locks.isMovementLocked()) {
            if (dialoguePlayer == null || !dialoguePlayer.isActive || !dialoguePlayer.interfaceManager.hasChatbox()) {
                dialoguePlayer = null
            }
        }
        if (!player.isActive || !getLocation().withinDistance(player.location, 10)) {
            handlePlayerLeave()
        }
        if (!pulseManager.hasPulseRunning()) {
            startFollowing()
        }
        if (quotes != null) {
            if (nextSpeech < ticks && getDialoguePlayer() == null && !locks.isMovementLocked()) {
                if (count > quotes.size - 1) {
                    return
                }
                nextSpeech = (ticks + 20 / 0.5).toInt()
                if (++count >= quotes.size) {
                    isTimeUp = true
                    handleTimeUp()
                    return
                }
            }
        }
    }

    fun handlePlayerLeave() {
        clear()
    }

    fun handleTimeUp() {}

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        if (entity is Player && entity !== player) {
            entity.packetDispatch.sendMessage("It's not after you.")
            return false
        }
        return super.isAttackable(entity, style, message)
    }

    override fun onRegionInactivity() {
        super.onRegionInactivity()
        clear()
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC? = null

    fun startFollowing() {
        pulseManager.run(
            object : MovementPulse(this, player, Pathfinder.DUMB) {
                override fun pulse(): Boolean = false
            },
            PulseType.STANDARD,
        )
        face(player)
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> = super.newInstance(arg)
}
