package content.minigame.pestcontrol.plugin.npc

import content.minigame.pestcontrol.plugin.PestControlSession
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.NPCs

class PCPortalNPC(
    id: Int = NPCs.PORTAL_6142,
    l: Location? = null,
) : AbstractNPC(id, l) {
    var updateLifepoints = true
    private var session: PestControlSession? = null
    private val spinners = arrayOfNulls<PCSpinnerNPC>(3)
    private val brawlers = arrayOfNulls<NPC>(2)

    override fun init() {
        super.setWalks(false)
        super.setRespawn(false)
        super.getProperties().isRetaliating = false
        super.init()
        properties.attackAnimation = Animation.create(-1)
        properties.defenceAnimation = Animation.create(-1)
        properties.deathAnimation = Animation.create(-1)
        session = getExtension(PestControlSession::class.java)
    }

    override fun tick() {
        super.tick()
        if (session != null) {
            val plane = viewport.currentPlane
            if (plane != null && session!!.ticks % 35 - plane.players.size == 0 && plane.npcs.size < 100) {
                spawnNPCs()
            }
            if (updateLifepoints && session!!.ticks % 10 == 0) {
                val color = if (getSkills().lifepoints > 50) "<col=00FF00>" else "<col=FF0000>"
                session!!.sendString(color + getSkills().lifepoints, 13 + portalIndex)
                updateLifepoints = false
            }
        }
    }

    override fun shouldPreventStacking(mover: Entity): Boolean = true

    override fun onImpact(
        entity: Entity,
        state: BattleState,
    ) {
        updateLifepoints = true
        super.onImpact(entity, state)
        if (session != null && state != null && entity is Player) {
            var total = 0
            if (state.estimatedHit > 0) {
                total += state.estimatedHit
            }
            if (state.secondaryHit > 0) {
                total += state.secondaryHit
            }
            session!!.addZealGained(entity, total)
        }
    }

    fun spawnNPCs() {
        val dir = Direction.getLogicalDirection(getLocation(), session!!.squire!!.location)
        val index = difficultyIndex
        val r = RandomFunction.RANDOM
        var amount = index + 1
        if (viewport.currentPlane != null) {
            amount += viewport.currentPlane.players.size / 10
        }
        if (difficultyIndex == 0) {
            amount += (difficultyIndex + 1) * 3 / 2
        }
        for (i in 0 until amount) {
            var ids = SHIFTERS[index]
            when (r.nextInt(7)) {
                0 -> ids = SPLATTERS[index]
                1 -> ids = SHIFTERS[index]
                2 -> ids = RAVAGERS[index]
                3 -> {
                    var spawn = false
                    for (npc in spinners) {
                        if (npc == null || !npc.isActive) {
                            spawn = true
                            break
                        }
                    }
                    ids =
                        if (spawn) {
                            SPINNERS[index]
                        } else {
                            TORCHERS[index]
                        }
                }

                4 -> ids = TORCHERS[index]
                5 -> ids = DEFILERS[index]
                6 -> {
                    var spawn = false
                    for (npc in brawlers) {
                        if (npc == null || !npc.isActive) {
                            spawn = true
                            break
                        }
                    }
                    ids =
                        if (spawn) {
                            BRAWLERS[index]
                        } else {
                            DEFILERS[index]
                        }
                }
            }
            var x = dir.stepX * 3
            var y = dir.stepY * 3
            if (x == 0) {
                x = i
            } else {
                y = i
            }
            val n = session!!.addNPC(create(ids[r.nextInt(ids.size)], getLocation().transform(x, y, 0)))
            if (ids == RAVAGERS[index]) {
                (n as PCRavagerNPC).portalIndex = portalIndex
            } else if (ids == SPINNERS[index]) {
                for (j in spinners.indices) {
                    if (spinners[j] == null || !spinners[j]!!.isActive) {
                        spinners[j] = (n as PCSpinnerNPC).setPortalIndex(portalIndex)
                        break
                    }
                }
            }
            n.isWalks = true
            n.isRespawn = false
        }
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = PCPortalNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (session != null) {
            var value = 0
            var endSession = true
            for (i in 0..3) {
                if (!session!!.portals[i]!!.isActive) {
                    value = value or (1 shl i)
                } else {
                    endSession = false
                }
            }
            if (value > 0) {
                session!!.sendConfig(value shl 28)
                if (endSession) {
                    session!!.activity.end(session!!, true)
                    return
                }
            }
            for (npc in spinners) {
                if (npc != null && npc.isActive) {
                    npc.explode()
                }
            }
            session!!.sendString("<col=FF0000>0", 13 + portalIndex)
            session!!.squire!!.getSkills().heal(50)
            (session!!.squire as PCSquireNPC).FlagInterfaceUpdate()
        }
    }

    override fun getIds(): IntArray = PORTAL

    val difficultyIndex: Int
        get() {
            if (id > 7550) {
                return 2
            }
            return if (id > 6149) {
                1
            } else {
                0
            }
        }
    val portalIndex: Int
        get() {
            if (id > 7550) {
                return (id - 7551) % 4
            }
            return if (id > 6149) {
                (id - 6150) % 4
            } else {
                (id - 6142) % 4
            }
        }

    companion object {
        private val SPLATTERS =
            arrayOf(
                intArrayOf(NPCs.SPLATTER_3727, NPCs.SPLATTER_3728, NPCs.SPLATTER_3729),
                intArrayOf(NPCs.SPLATTER_3728, NPCs.SPLATTER_3729, NPCs.SPLATTER_3730),
                intArrayOf(NPCs.SPLATTER_3729, NPCs.SPLATTER_3730, NPCs.SPLATTER_3731),
            )
        private val SHIFTERS =
            arrayOf(
                intArrayOf(
                    NPCs.SHIFTER_3732,
                    NPCs.SHIFTER_3733,
                    NPCs.SHIFTER_3734,
                    NPCs.SHIFTER_3735,
                    NPCs.SHIFTER_3736,
                    NPCs.SHIFTER_3737,
                ),
                intArrayOf(
                    NPCs.SHIFTER_3734,
                    NPCs.SHIFTER_3735,
                    NPCs.SHIFTER_3736,
                    NPCs.SHIFTER_3737,
                    NPCs.SHIFTER_3738,
                    NPCs.SHIFTER_3739,
                ),
                intArrayOf(
                    NPCs.SHIFTER_3736,
                    NPCs.SHIFTER_3737,
                    NPCs.SHIFTER_3738,
                    NPCs.SHIFTER_3739,
                    NPCs.SHIFTER_3740,
                    NPCs.SHIFTER_3741,
                ),
            )
        private val RAVAGERS =
            arrayOf(
                intArrayOf(NPCs.RAVAGER_3742, NPCs.RAVAGER_3743, NPCs.RAVAGER_3744),
                intArrayOf(NPCs.RAVAGER_3743, NPCs.RAVAGER_3744, NPCs.RAVAGER_3745),
                intArrayOf(NPCs.RAVAGER_3744, NPCs.RAVAGER_3745, NPCs.RAVAGER_3746),
            )
        private val SPINNERS =
            arrayOf(
                intArrayOf(NPCs.SPINNER_3747, NPCs.SPINNER_3748, NPCs.SPINNER_3749),
                intArrayOf(NPCs.SPINNER_3748, NPCs.SPINNER_3749, NPCs.SPINNER_3750),
                intArrayOf(NPCs.SPINNER_3749, NPCs.SPINNER_3750, NPCs.SPINNER_3751),
            )
        private val TORCHERS =
            arrayOf(
                intArrayOf(
                    NPCs.TORCHER_3752,
                    NPCs.TORCHER_3753,
                    NPCs.TORCHER_3754,
                    NPCs.TORCHER_3755,
                    NPCs.TORCHER_3756,
                    NPCs.TORCHER_3757,
                ),
                intArrayOf(
                    NPCs.TORCHER_3754,
                    NPCs.TORCHER_3755,
                    NPCs.TORCHER_3756,
                    NPCs.TORCHER_3757,
                    NPCs.TORCHER_3758,
                    NPCs.TORCHER_3759,
                ),
                intArrayOf(
                    NPCs.TORCHER_3756,
                    NPCs.TORCHER_3757,
                    NPCs.TORCHER_3758,
                    NPCs.TORCHER_3759,
                    NPCs.TORCHER_3760,
                    NPCs.TORCHER_3761,
                ),
            )
        private val DEFILERS =
            arrayOf(
                intArrayOf(
                    NPCs.DEFILER_3762,
                    NPCs.DEFILER_3763,
                    NPCs.DEFILER_3764,
                    NPCs.DEFILER_3765,
                    NPCs.DEFILER_3766,
                    NPCs.DEFILER_3767,
                ),
                intArrayOf(
                    NPCs.DEFILER_3764,
                    NPCs.DEFILER_3765,
                    NPCs.DEFILER_3766,
                    NPCs.DEFILER_3767,
                    NPCs.DEFILER_3768,
                    NPCs.DEFILER_3769,
                ),
                intArrayOf(
                    NPCs.DEFILER_3766,
                    NPCs.DEFILER_3767,
                    NPCs.DEFILER_3768,
                    NPCs.DEFILER_3769,
                    NPCs.DEFILER_3770,
                    NPCs.DEFILER_3771,
                ),
            )
        private val BRAWLERS =
            arrayOf(
                intArrayOf(NPCs.BRAWLER_3772, NPCs.BRAWLER_3773, NPCs.BRAWLER_3774),
                intArrayOf(NPCs.BRAWLER_3773, NPCs.BRAWLER_3774, NPCs.BRAWLER_3775),
                intArrayOf(NPCs.BRAWLER_3774, NPCs.BRAWLER_3775, NPCs.BRAWLER_3776),
            )

        val PORTAL =
            intArrayOf(
                NPCs.PORTAL_6142,
                NPCs.PORTAL_6143,
                NPCs.PORTAL_6144,
                NPCs.PORTAL_6145,
                NPCs.PORTAL_6146,
                NPCs.PORTAL_6147,
                NPCs.PORTAL_6148,
                NPCs.PORTAL_6149,
                NPCs.PORTAL_6150,
                NPCs.PORTAL_6151,
                NPCs.PORTAL_6152,
                NPCs.PORTAL_6153,
                NPCs.PORTAL_6154,
                NPCs.PORTAL_6155,
                NPCs.PORTAL_6156,
                NPCs.PORTAL_6157,
                NPCs.PORTAL_7551,
                NPCs.PORTAL_7552,
                NPCs.PORTAL_7553,
                NPCs.PORTAL_7554,
                NPCs.PORTAL_7555,
                NPCs.PORTAL_7556,
                NPCs.PORTAL_7557,
                NPCs.PORTAL_7558,
            )
    }
}
