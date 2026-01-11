package content.global.skill.slayer.dungeon.waterbirth_dungeon

import core.api.playAudio
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.settings
import core.game.world.map.Location
import core.game.world.map.RegionManager
import shared.consts.NPCs

/**
 * Handles spawn dagannoths from egg.
 */
class DagannothSpawnEggNPC : AbstractNPC {

    private var transforming = false

    constructor() : super(-1, null)
    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return DagannothSpawnEggNPC(id, location)
    }

    override fun init() {
        super.init()
        isWalks = false
        isAggressive = false
    }

    override fun handleTickActions() {
        super.handleTickActions()

        if (transforming) return

        val target = RegionManager.getLocalPlayers(location, 1).firstOrNull() ?: return

        transforming = true
        Pulser.submit(object : Pulse(1) {
            private var stage = 0
            private val spawns = mutableListOf<NPC>()
            val spawn = NPC.create(NPCs.DAGANNOTH_SPAWN_2454, location.transform(-1, 0, 0))
            override fun pulse(): Boolean {
                stage++
                when (stage) {
                    1 -> {
                        playAudio(target, 1063)
                        transform(id + 1)
                    }
                    3 -> {
                        transform(id + 1)
                        spawn.init()
                        spawn.isWalks = true
                        spawn.isAggressive = true
                        spawn.isRespawn = false
                        spawn.attack(target)
                        spawns.add(spawn)

                        Pulser.submit(object : Pulse(if (settings!!.isDevMode) 10 else 45) {
                            override fun pulse(): Boolean {
                                reTransform()
                                for (s in spawns) {
                                    if (s.isActive && !s.inCombat()) {
                                        s.clear()
                                    }
                                }
                                transforming = false
                                return true
                            }
                        })
                    }
                }
                return stage == 3
            }
        })
    }

    override fun canStartCombat(victim: Entity): Boolean = false

    override fun getIds(): IntArray = intArrayOf(NPCs.EGG_2449)
}