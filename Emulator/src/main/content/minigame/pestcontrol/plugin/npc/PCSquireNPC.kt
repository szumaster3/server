package content.minigame.pestcontrol.plugin.npc

import content.minigame.pestcontrol.plugin.PestControlSession
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.NPCs

class PCSquireNPC : AbstractNPC {
    private var session: PestControlSession? = null
    private var updateLifepoints = false

    constructor() : super(NPCs.VOID_KNIGHT_3782, null, false)

    constructor(id: Int, location: Location?) : super(id, location, false)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = PCSquireNPC(id, location)

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean = !(DeathTask.isDead(this) || entity is Player)

    override fun init() {
        super.setRespawn(false)
        super.getProperties().isRetaliating = false
        super.init()
        super.getProperties().defenceAnimation = Animation.create(-1)
        super.getProperties().deathAnimation = Animation.create(-1)
        session = getExtension(PestControlSession::class.java)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
    }

    override fun tick() {
        super.tick()
        if (updateLifepoints && session != null && session!!.ticks % 10 == 0) {
            if (getSkills().lifepoints > 50) {
                session!!.sendString(getSkills().lifepoints.toString(), 1)
            } else {
                session!!.sendString("<col=FF0000>" + getSkills().lifepoints.toString(), 1)
            }
            updateLifepoints = false
        }
    }

    override fun onImpact(
        entity: Entity,
        state: BattleState,
    ) {
        FlagInterfaceUpdate()
        super.onImpact(entity, state)
    }

    fun FlagInterfaceUpdate() {
        updateLifepoints = true
    }

    override fun getIds(): IntArray = intArrayOf(3782, 3785)
}
